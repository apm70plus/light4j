package com.light.auth.token;

public interface TokenRepository {

	String getValue(String token);
	
	void put(String token, String value);
	
	void clearToken(String token);
}