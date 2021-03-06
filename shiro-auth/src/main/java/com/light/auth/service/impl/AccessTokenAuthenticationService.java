package com.light.auth.service.impl;

import com.light.auth.authc.OAuth2Token;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import com.light.auth.token.AccessTokenService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

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
	public void onAuthenticationSuccess(MutablePrincipalCollection principals) {
		applyAccessTokenPrincipals(principals);
	}

	private void applyAccessTokenPrincipals(MutablePrincipalCollection principals) {
		LoginUser user = (LoginUser) principals.getPrimaryPrincipal();
		AccessTokenService.AccessToken token = accessTokenService.allocateToken(user);
		principals.add(token, "AccessToken");
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return OAuth2Token.class;
	}

}