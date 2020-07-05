package com.light.oauth2.repository;

import com.light.oauth2.bean.TokenBean;

public interface Oauth2TokenRepository {

	void saveAccessToken(TokenBean token, long timeout);
	
	TokenBean getAccessToken(String token);
	
	void saveRefreshToken(TokenBean token, long timeout);
	
	TokenBean getRefreshToken(String token);
}
