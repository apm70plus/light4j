package com.light.rbac.cache;

import java.util.Set;

public interface RolePermissionCache {

	void setRolePermissions(String roleCode, Set<String> permissions);
	
	Set<String> getRolePermissions(String roleCode);
}
