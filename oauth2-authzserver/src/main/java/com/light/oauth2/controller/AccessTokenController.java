package com.light.oauth2.controller;

import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.light.exception.AuthorizationException;
import com.light.oauth2.Oauth2Constants;
import com.light.oauth2.authc.UsernamePasswordToken;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.dto.AccessTokenResponse;
import com.light.oauth2.model.AuthorizationCode;
import com.light.oauth2.model.Oauth2Client;
import com.light.oauth2.repository.Oauth2ClientRepository;
import com.light.oauth2.service.Oauth2Service;
import com.light.oauth2.service.impl.UsernamePasswordAuthorizationService;
import com.light.oauth2.type.GrantType;

import io.swagger.annotations.Api;

/**
 * Oauth2 访问令牌接口
 * 
 * @author liuyg
 */
@RestController
@Api(tags = { "Oauth2 访问令牌API" })
public class AccessTokenController {

	@Autowired
	private Oauth2Service oauth2Service;
	@Autowired
	private Oauth2ClientRepository oauth2ClientRepository;
	@Autowired
	private UsernamePasswordAuthorizationService usernamePasswordAuthenticateService;
	
	@PostMapping("/token")
	public ResponseEntity<AccessTokenResponse> token(
			@RequestParam String grant_type, 
			@RequestParam String client_id,
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String redirect_uri,
			@RequestParam(required = false) String scope,
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String time,
			@RequestParam(required = false) String signature,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String password,
			@RequestParam(required = false) String refresh_token)
			throws URISyntaxException {
		
		if (GrantType.AUTHORIZATION_CODE.equals(grant_type)) {
			return createTokenByAuthCode(client_id, code, scope);
		} else if (GrantType.CLIENT_CREDENTIALS.equals(grant_type)) {
			return createTokenByClientCredentials(client_id, time, signature);
		} else if (GrantType.PASSWORD.equals(grant_type)) {
			return createTokenByPassword(client_id, username, password);
		} else if (GrantType.IMPLICIT.equals(grant_type)) {
			throw AuthorizationException.of("不支持的授权类型").code("unsupported_grant_type");
		} else if (GrantType.JWT_BEARER.equals(grant_type)) {
			throw AuthorizationException.of("不支持的授权类型").code("unsupported_grant_type");
		} else if (GrantType.REFRESH_TOKEN.equals(grant_type)) {
			return refreshAccessToken(refresh_token);
		} else {
			throw AuthorizationException.of("不支持的授权类型").code("unsupported_grant_type");
		}
	}

	private ResponseEntity<AccessTokenResponse> refreshAccessToken(String refresh_token) {
		if (StringUtils.isEmpty(refresh_token)) {
			throw AuthorizationException.of("无效的令牌").code("invalid_refresh_token");
		}
		TokenBean token = oauth2Service.refreshAccessToken(refresh_token);
		return createAccessTokenResponse(token);
	}

	private ResponseEntity<AccessTokenResponse> createTokenByPassword(String clientId, String username, String password) {
		UsernamePasswordToken authcToken = new UsernamePasswordToken(username, password);
		try {
			LoginUser user = usernamePasswordAuthenticateService.getAuthenticationInfo(authcToken);
			TokenBean token = oauth2Service.createAccessToken(user.getId(), clientId, Oauth2Constants.USER_SELF_SCOPE);
			return createAccessTokenResponse(token);
		} catch (AuthenticationException e) {
			throw AuthorizationException.of("用户名密码错误").code("invalid_client");
		}
		
	}
	
	private ResponseEntity<AccessTokenResponse> createTokenByClientCredentials(String clientId, String time, String signature) {
		Optional<Oauth2Client> client = oauth2ClientRepository.findByClientId(clientId);
		Oauth2Client c = client.orElseThrow(() -> AuthorizationException.of("无效的客户端").code("invalid_client"));
		String secret = c.getClientSecret();
		byte[] bs = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, secret.getBytes()).doFinal(time.getBytes());
		String newSignature = Base64Utils.encodeToString(bs);
		if (newSignature.equals(signature)) {
			TokenBean token = oauth2Service.createAccessToken(clientId, clientId, Oauth2Constants.CLIENT_SELF_SCOPE);
			TokenBean refreshToken = oauth2Service.createRefreshToken(clientId, clientId, Oauth2Constants.CLIENT_SELF_SCOPE);
			return createAccessTokenResponse(token, refreshToken);
		} else {
			throw AuthorizationException.of("无效的客户端").code("invalid_client");
		}
	}

	private ResponseEntity<AccessTokenResponse> createTokenByAuthCode(String clientId, String authCode, String scope) {
		Subject subject = SecurityUtils.getSubject();
		// 如果客户端没有登录，返回错误
		// TODO: 根据AccessToken判定是否合法的client
//		if (!subject.isAuthenticated()) {
//			return createErrorResponse(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_CLIENT, "客户端未授权");
//		}
		// 检查提交的客户端id是否正确
		Optional<Oauth2Client> client = oauth2ClientRepository.findByClientId(clientId);
		LoginUser loginClient = (LoginUser)subject.getPrincipal();
		if (!loginClient.getId().equals(clientId) || !client.isPresent()) {
			throw AuthorizationException.of("无效的客户端").code("invalid_client");
		}
		AuthorizationCode code = oauth2Service.getAuthCode(authCode, clientId);
		if (code == null) {
			throw AuthorizationException.of("无效的的授权码").code("invalid_grant");
		}
		//生成Access Token 
		// TODO: scope 应该根据client注册的权限范围来设置
		TokenBean accessToken = oauth2Service.createAccessToken(code.getUserId(), clientId, client.get().getScope());
		//生成OAuth响应
		return createAccessTokenResponse(accessToken);
	}
	

	private ResponseEntity<AccessTokenResponse> createAccessTokenResponse(TokenBean accessToken, TokenBean refreshToken) {
		AccessTokenResponse response = new AccessTokenResponse();
		response.setAccess_token(accessToken.getToken());
		response.setExpires_in(accessToken.getTimeout());
		response.setToken_type("bearer");
		response.setRefresh_token(refreshToken.getToken());
		return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(response);
	}

	private ResponseEntity<AccessTokenResponse> createAccessTokenResponse(TokenBean accessToken) {
		return createAccessTokenResponse(accessToken, null);
	}
}
