package com.light.oauth2.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.light.exception.AuthorizationException;
import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.model.AuthorizationCode;
import com.light.oauth2.repository.AuthorizationCodeRepository;
import com.light.oauth2.repository.Oauth2TokenRepository;
import com.light.oauth2.service.Oauth2Service;
import com.light.util.CommonUtils;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {

	@Value("${oauth2.auth_code.timeout:300}")
	private int authCodeTimeout;
	@Value("${oauth2.access_token.timeout:1800}")
	private int accessTokenTimeout;
	@Value("${oauth2.refresh_token.timeout:7200}")
	private int refreshTokenTimeout;
	
	@Autowired
	private AuthorizationCodeRepository authorizationCodeRepository;
	@Autowired
	private Oauth2TokenRepository oauth2TokenRepository;

	@Override
	public AuthorizationCode createAuthCode(String userId, String clientId) {
		String authorizationCode = CommonUtils.md5(UUID.randomUUID().toString());
		AuthorizationCode code = new AuthorizationCode();
		code.setCode(authorizationCode);
		code.setUserId(userId);
		code.setClientId(clientId);
		authorizationCodeRepository.save(code, authCodeTimeout);
		return code;
	}

	@Override
	public AuthorizationCode getAuthCode(String authCode, String clientId) {
		AuthorizationCode code = authorizationCodeRepository.findByCode(authCode);
		if (code == null || !code.getClientId().equals(clientId)) {
			return null;
		}
		authorizationCodeRepository.deleteByCode(authCode);
		return code;
	}

	@Override
	public TokenBean createAccessToken(String userId, String clientId, String scope) {
		String accessToken = CommonUtils.md5(UUID.randomUUID().toString());
		TokenBean token = new TokenBean();
		token.setScope(scope);
		token.setToken(accessToken);
		token.setUserId(userId);
		token.setClient(clientId);
		token.setTimeout(accessTokenTimeout);
		oauth2TokenRepository.saveAccessToken(token, accessTokenTimeout);
		return token;
	}
	
	@Override
	public TokenBean createRefreshToken(String userId, String clientId, String scope) {
		String accessToken = CommonUtils.md5(UUID.randomUUID().toString());
		TokenBean token = new TokenBean();
		token.setScope(scope);
		token.setToken(accessToken);
		token.setUserId(userId);
		token.setClient(clientId);
		token.setTimeout(refreshTokenTimeout);
		oauth2TokenRepository.saveAccessToken(token, refreshTokenTimeout);
		return token;
	}

	@Override
	public TokenBean refreshAccessToken(String refreshToken) {
		TokenBean t = oauth2TokenRepository.getRefreshToken(refreshToken);
		if (t == null) {
			throw AuthorizationException.of("无效的令牌").code("invalid_refresh_token");
		}
		return createAccessToken(t.getUserId(), t.getClient(), t.getScope());
	}

}
