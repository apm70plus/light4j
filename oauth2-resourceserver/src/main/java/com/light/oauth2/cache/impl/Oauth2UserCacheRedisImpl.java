package com.light.oauth2.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.cache.Oauth2UserCache;
import com.light.util.JsonUtils;

public class Oauth2UserCacheRedisImpl implements Oauth2UserCache {

	private static final String REDIS_USER_PRE = "oauth_user:";
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Override
	public LoginUser get(String userId) {
		String json = redisTemplate.boundValueOps(REDIS_USER_PRE + userId).get();
		if (json == null) {
			return null;
		}
		return (LoginUser)JsonUtils.jsonToPojo(json, LoginUser.class);
	}

	@Override
	public void set(String userId, LoginUser user) {
		String json = JsonUtils.pojoToJson(user);
		redisTemplate.boundValueOps(REDIS_USER_PRE + userId).set(json);
	}

	@Override
	public void remove(String userId) {
		redisTemplate.delete(REDIS_USER_PRE + userId);
	}

}
