package com.light.auth.service.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.light.auth.authc.OAuth2Token;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import com.light.auth.token.AccessTokenService;

public class AccessTokenAuthenticationService implements AuthenticationService {

	@Autowired
	private AccessTokenService accessTokenService;
	
	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		OAuth2Token token = (OAuth2Token) authcToken;
		String accessToken = token.getAccessToken();
		LoginUser user = accessTokenService.getUserByToken(accessToken);
		if (user == null) {
			throw new AuthenticationException("无效的AccessToken");
		}
		return user;
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return OAuth2Token.class;
	}

}