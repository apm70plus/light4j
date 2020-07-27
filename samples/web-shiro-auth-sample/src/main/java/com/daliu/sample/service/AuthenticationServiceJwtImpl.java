package com.daliu.sample.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.light.auth.authc.JwtToken;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import com.light.auth.token.JwtTokenService;

/**
 * JWT token 认证服务实现
 * @author daliu
 *
 */
@Service
@ConditionalOnProperty(name = "light.auth.type", havingValue = "jwtToken", matchIfMissing = false)
public class AuthenticationServiceJwtImpl implements AuthenticationService {

	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		JwtToken token = (JwtToken)authcToken;
		LoginUser user = jwtTokenService.getUserByToken(token.getJwtToken());
		if (user == null) {
			throw new AuthenticationException("无效的token");
		}
		return user;
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return JwtToken.class;
	}

}
