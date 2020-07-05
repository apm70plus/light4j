package com.light.oauth2.cache;

import com.light.oauth2.bean.LoginUser;

public interface Oauth2UserCache {

	LoginUser get(String userId);
	
	void set(String userId, LoginUser user);
	
	void remove(String userId);
}
