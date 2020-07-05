package com.light.auth.bean;

import java.util.HashMap;
import java.util.Map;

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
    
	/**
	 * 扩展信息
	 */
	private Map<String, Object> extension = new HashMap<>();

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return password;
	}
}
