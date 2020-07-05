package com.light.rbac.cache.impl;

import java.util.Set;

import javax.annotation.PostConstruct;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.light.rbac.cache.UserRoleCache;

/**
 * 用户角色缓存的Hazelcast实现，支持分布式集群
 * 
 * @author liuyg
 */
public class HazelcastUserRoleCacheImpl implements UserRoleCache {

	private IMap<Long, Set<String>> cache;
	
	public HazelcastUserRoleCacheImpl(HazelcastInstance hazelcastInstance) {
		cache = hazelcastInstance.getMap("hazelcast.user.roles");
	}
	
	@Override
	public void setUserRoles(Long userId, Set<String> roles) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getUserRoles(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
