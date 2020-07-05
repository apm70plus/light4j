package com.light.rbac.cache.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.light.rbac.cache.RolePermissionCache;
import com.light.rbac.model.SysRolePermission;
import com.light.rbac.repository.SysRolePermissionRepository;

/**
 * 角色权限缓存的默认实现，基于内存实现，不支持集群
 * 
 * @author liuyg
 */
public class DefaultRolePermissionCacheImpl implements RolePermissionCache, ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private SysRolePermissionRepository sysRolePermissionRepository;
	private volatile boolean loaded = false;
	private ConcurrentHashMap<String, Set<String>> cache = new ConcurrentHashMap<>();

	@Override
	public void setRolePermissions(String roleCode, Set<String> permissions) {
		cache.put(roleCode, permissions);
	}

	@Override
	public Set<String> getRolePermissions(String roleCode) {
		Set<String> permissions = cache.get(roleCode);
	    if (permissions != null) {
	    	return Collections.unmodifiableSet(permissions);	
	    } else {
	    	return Collections.emptySet();
	    }
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (loaded) {
			return;
		} else {
			loaded = true;
		}
		Iterable<SysRolePermission> rolePermissions = sysRolePermissionRepository.findAll();
		rolePermissions.forEach(rolePermission -> {
			Set<String> permissions = cache.get(rolePermission.getRole().getCode());
			if (permissions == null) {
				permissions = new HashSet<>();
				cache.put(rolePermission.getRole().getCode(), permissions);
			}
			permissions.add(rolePermission.getPermission().getCode());
		});
	}
}
