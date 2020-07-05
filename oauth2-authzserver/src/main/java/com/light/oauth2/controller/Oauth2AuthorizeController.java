package com.light.oauth2.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.light.exception.AuthorizationException;
import com.light.oauth2.Oauth2Constants;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.model.AuthorizationCode;
import com.light.oauth2.model.Oauth2Client;
import com.light.oauth2.repository.Oauth2ClientRepository;
import com.light.oauth2.service.Oauth2Service;

import io.swagger.annotations.Api;

/**
 * Oauth2 授权API
 *
 * @author daliu
 */
@RestController
@Api(tags = { "Oauth2 授权API" })
public class Oauth2AuthorizeController {

	@Autowired
	private Oauth2Service oauthService;
	@Autowired
	private Oauth2ClientRepository oauth2ClientRepository;

	
	@RequestMapping("/authorize")
	public Object authorize(
			@RequestParam String response_type,
			@RequestParam String client_id,
			@RequestParam(required = false) String redirect_uri,
			@RequestParam(required = false) String scope,
			@RequestParam(required = false) String state,
			HttpServletRequest request)
			throws URISyntaxException {
		
		// 目前仅支持CODE“授权码模式”，另外还有TOKEN方式属于“简化模式”
		if (!Oauth2Constants.RESPONSE_TYPE_CODE.equals(response_type)) {
			throw AuthorizationException.of("仅支持code授权码模式").code("unsupported_response_type");
		}
        if (StringUtils.isEmpty(redirect_uri)) {
        	throw AuthorizationException.of("重定向地址不能为空").code("invalid_request");
        }
		// 检查传入的客户端id是否存在
		Optional<Oauth2Client> client = oauth2ClientRepository.findByClientId(client_id);
		if (!client.isPresent()) {
			throw AuthorizationException.of("无效的客户端").code("invalid_client");
		}
		Subject subject = SecurityUtils.getSubject();
		// 如果用户没有登录，跳转到登陆页面
		if (!subject.isAuthenticated()) {
			return "redirect:oauth2login";
		}
		
		LoginUser user = (LoginUser)subject.getPrincipal();
		return createAuthorizationCode(user.getId(), client_id, state, redirect_uri);
	}
	
	private ResponseEntity<?> createAuthorizationCode(String userId, String clientId, String state, String redirectURI)
			throws URISyntaxException {
		AuthorizationCode authCode = oauthService.createAuthCode(userId, clientId);
        StringBuilder redirect = new StringBuilder();
        redirect.append(redirectURI);
        if (redirectURI.contains("?")) {
            redirect.append("&");
        } else {
        	redirect.append("?");
        }
        redirect.append(Oauth2Constants.RESPONSE_TYPE_CODE).append("=").append(authCode.getCode());
        redirect.append("&state=").append(state);
        //根据OAuthResponse返回ResponseEntity响应
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(redirect.toString()));
        return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
	}	
}
