package com.light.auth.token.impl;

import com.light.auth.token.TokenRepository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

public class RedisTokenRepositoryImpl implements TokenRepository {

	private StringRedisTemplate stringRedisTemplate;

	private int expireSeconds;
	
	public RedisTokenRepositoryImpl(StringRedisTemplate stringRedisTemplate, int expireSeconds) {
		this.stringRedisTemplate = stringRedisTemplate;
		this.expireSeconds = expireSeconds;
	}
	
	@Override
	public String getValue(String token) {
		String value = this.stringRedisTemplate.opsForValue().get(token);
		return value;
	}

	@Override
	public void put(String token, String value) {
		this.stringRedisTemplate.opsForValue().set(token, value, Duration.ofSeconds(expireSeconds));
	}

	@Override
	public void clearToken(String token) {
		this.stringRedisTemplate.delete(token);
	}

}
