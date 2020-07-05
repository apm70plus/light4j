package com.light.auth.token.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;

import com.light.auth.bean.LoginUser;
import com.light.auth.token.AccessTokenService;
import com.light.auth.token.TokenRepository;
import com.light.util.JsonUtils;

public class AccessTokenServiceImpl implements AccessTokenService {

	private TokenRepository tokenRepository;
	private int expirSeconds;
	
	public AccessTokenServiceImpl(TokenRepository tokenRepository, int expirSeconds) {
		this.tokenRepository = tokenRepository;
		this.expirSeconds = expirSeconds;
	}
	
	@Override
	public AccessToken newToken() {
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		AccessToken token = new AccessToken();
		token.setToken(accessToken);
		token.setExpires(this.expirSeconds);
		return token;
	}

	@Override
	public AccessToken allocateToken(LoginUser user) {
		AccessToken token = newToken();
		user.getExtension().put("access_token", token.getToken());
		String userJson = JsonUtils.pojoToJson(user);
		this.tokenRepository.put(token.getToken(), userJson);
		return token;
	}

	@Override
	public LoginUser getUserByToken(String accessToken) {
		String value = this.tokenRepository.getValue(accessToken);
		if (value == null) {
			return null;
		}
		LoginUser user = JsonUtils.jsonToPojo(value, LoginUser.class);
		return user;
	}

	@Override
	public void invalidUserToken(LoginUser user) {
		String token = (String)user.getExtension().get("access_token");
		if (token != null) {
			this.tokenRepository.clearToken(token);
		}
	}

	@Override
	public AccessToken refreshToken(String accessToken) {
		String value = this.tokenRepository.getValue(accessToken);
		if (StringUtils.isBlank(value)) {
			throw new AuthenticationException();
		}
		if (value != null) {
			this.tokenRepository.put(accessToken, value);
		}
		AccessToken token = new AccessToken();
		token.setExpires(expirSeconds);
		token.setToken(accessToken);
		return token;
	}

}
