package com.light.rbac.cache;

import java.util.Set;

public interface UserRoleCache {

	void setUserRoles(Long userId, Set<String> roles);
	
	Set<String> getUserRoles(Long userId);
}
