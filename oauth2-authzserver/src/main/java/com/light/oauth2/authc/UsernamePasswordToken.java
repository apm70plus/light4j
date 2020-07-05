package com.light.oauth2.authc;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter@Setter
@RequiredArgsConstructor
public class UsernamePasswordToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;

	/**
     * The username
     */
    private final String username;

    /**
     * The password
     */
    private final String password;

	@Override
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		return getPassword();
	}
}
