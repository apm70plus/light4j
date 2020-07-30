package com.light.auth.service.impl;

import com.light.auth.authc.JwtToken;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import com.light.auth.token.JwtTokenService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

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
	public void onAuthenticationSuccess(MutablePrincipalCollection principals) {
		applyJwtTokenPrincipals(principals);
	}

	private void applyJwtTokenPrincipals(MutablePrincipalCollection principals) {
		LoginUser user = (LoginUser) principals.getPrimaryPrincipal();
		JwtTokenService.JwtToken token = jwtTokenService.allocateToken(user);
		principals.add(token, "JwtToken");
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return JwtToken.class;
	}

}