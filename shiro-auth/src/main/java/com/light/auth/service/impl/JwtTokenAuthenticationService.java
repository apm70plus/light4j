package com.light.auth.service.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.light.auth.authc.JwtToken;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import com.light.auth.token.JwtTokenService;

public class JwtTokenAuthenticationService implements AuthenticationService {

	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		JwtToken token = (JwtToken) authcToken;
		String accessToken = token.getJwtToken();
		LoginUser user = null;
		try {
			user = jwtTokenService.getUserByToken(accessToken);
		} catch (Exception e) {
		}
		if (user == null) {
			throw new AuthenticationException("无效的AccessToken");
		}
		return user;
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return JwtToken.class;
	}

}