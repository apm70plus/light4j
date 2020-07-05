package com.light.oauth2.service.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import com.light.oauth2.authc.UsernamePasswordToken;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.service.AuthenticationService;

public abstract class UsernamePasswordAuthorizationService implements AuthenticationService {

	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		return authenticate(token.getUsername(), token.getPassword());
	}
	
	abstract protected LoginUser authenticate(String username, String password) throws AuthenticationException;

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return UsernamePasswordToken.class;
	}

}
