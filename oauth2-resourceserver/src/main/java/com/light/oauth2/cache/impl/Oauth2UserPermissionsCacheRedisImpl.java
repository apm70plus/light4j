package com.light.oauth2.cache.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.light.oauth2.cache.Oauth2UserPermissionsCache;

public class Oauth2UserPermissionsCacheRedisImpl implements Oauth2UserPermissionsCache {

	private static final String REDIS_PERMISSION_PRE = "oauth_upermissions:";
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Override
	public Collection<String> get(String userId) {
		String values = redisTemplate.boundValueOps(REDIS_PERMISSION_PRE + userId).get();
		if (values == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(values.split(","));
	}

	@Override
	public void set(String userId, Collection<String> permissions) {
		StringBuilder builder = new StringBuilder();
		permissions.stream().forEach(str -> {
			if (builder.length() != 0) {
				builder.append(",");
			}
			builder.append(str);
		});
		redisTemplate.boundValueOps(REDIS_PERMISSION_PRE + userId).set(builder.toString());
	}

	@Override
	public void remove(String userId) {
		redisTemplate.delete(REDIS_PERMISSION_PRE + userId);
	}

}
