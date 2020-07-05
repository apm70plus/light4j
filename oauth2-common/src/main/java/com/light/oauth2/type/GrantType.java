package com.light.oauth2.type;

public interface GrantType {

	static final String AUTHORIZATION_CODE = "authorization_code";
	static final String CLIENT_CREDENTIALS = "client_credentials";
	static final String PASSWORD = "password";
	static final String IMPLICIT = "implicit";
	static final String JWT_BEARER = "jwt-bearer";
	static final String REFRESH_TOKEN = "refresh_token";
}
