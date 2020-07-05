package com.light.auth.token.impl;

import java.nio.charset.Charset;

import org.crazycake.shiro.RedisManager;

import com.light.auth.token.TokenRepository;

public class RedisTokenRepositoryImpl implements TokenRepository {

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private RedisManager redisManager;
	private int expirSeconds;
	
	public RedisTokenRepositoryImpl(RedisManager redisManager, int expirSeconds) {
		this.redisManager = redisManager;
		this.expirSeconds = expirSeconds;
	}
	
	@Override
	public String getValue(String token) {
		byte[] bs = this.redisManager.get(token.getBytes());
		if (bs != null && bs.length > 0) {
			String value = new String(bs, UTF8);
			return value;
		} else {
			return null;
		}
	}

	@Override
	public void put(String token, String value) {
		this.redisManager.set(token.getBytes(UTF8), value.getBytes(UTF8), expirSeconds);
	}

	@Override
	public void clearToken(String token) {
		this.redisManager.del(token.getBytes(UTF8));
	}

}
