package com.light.oauth2.authc;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth2Token implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private final String accessToken;
	
	@Override
	public Object getPrincipal() {
		return accessToken;
	}

	@Override
	public Object getCredentials() {
		return accessToken;
	}

	
}
