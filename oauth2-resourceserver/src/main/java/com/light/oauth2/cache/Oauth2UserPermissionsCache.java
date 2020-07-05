package com.light.oauth2.cache;

import java.util.Collection;

public interface Oauth2UserPermissionsCache {

	Collection<String> get(String userId);
	
	void set(String userId, Collection<String> permissions);
	
	void remove(String userId);
}
