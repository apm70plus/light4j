package com.light.auth.authc;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private final String jwtToken;
	
	@Override
	public Object getPrincipal() {
		return jwtToken;
	}

	@Override
	public Object getCredentials() {
		return jwtToken;
	}

	
}
