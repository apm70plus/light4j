package com.light.oauth2.service.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.light.oauth2.authc.OAuth2Token;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.cache.Oauth2UserCache;
import com.light.oauth2.repository.Oauth2TokenRepository;
import com.light.oauth2.service.AuthenticationService;

/**
 * AccessToken的认证服务实现
 * @author liuyg
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private Oauth2UserCache oauth2UserCache;
	@Autowired
	private Oauth2TokenRepository oauth2TokenRepository;

	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		TokenBean token = oauth2TokenRepository.getAccessToken(((OAuth2Token)authcToken).getAccessToken());
		if (token == null) {
			throw new AuthenticationException("invalid token.");
		}
		LoginUser user = oauth2UserCache.get(token.getUserId());
		if (user == null) {
			throw new AuthenticationException("userId is not found: " + token.getUserId());
		}
		user.getExtension().put("scope", token.getScope());
		
		return user;
	}
	
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return OAuth2Token.class;
	}
}
