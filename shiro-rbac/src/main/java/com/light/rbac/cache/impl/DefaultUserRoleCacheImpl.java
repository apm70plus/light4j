package com.light.rbac.cache.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.light.rbac.cache.UserRoleCache;
import com.light.rbac.model.SysUserRole;
import com.light.rbac.repository.SysUserRoleRepository;

/**
 * 用户角色缓存的默认实现，基于内存实现，不支持集群
 * 
 * @author liuyg
 */
public class DefaultUserRoleCacheImpl implements UserRoleCache, ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;
	private volatile boolean loaded = false;
	private ConcurrentHashMap<Long, Set<String>> cache = new ConcurrentHashMap<>();
	
	@Override
	public void setUserRoles(Long userId, Set<String> roles) {
		cache.put(userId, roles);
	}

	@Override
	public Set<String> getUserRoles(Long userId) {
	    Set<String> roles = cache.get(userId);
	    if (roles != null) {
	    	return Collections.unmodifiableSet(roles);	
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
		Iterable<SysUserRole> userRoles = sysUserRoleRepository.findAll();
		userRoles.forEach(userRole -> {
			Set<String> roles = cache.get(userRole.getUserId());
			if (roles == null) {
				roles = new HashSet<>();
				cache.put(userRole.getUserId(), roles);
			}
			roles.add(userRole.getRole().getCode());
		});
	}

}
