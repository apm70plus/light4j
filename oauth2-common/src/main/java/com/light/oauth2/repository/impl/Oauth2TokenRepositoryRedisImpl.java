package com.light.oauth2.repository.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.repository.Oauth2TokenRepository;
import com.light.util.JsonUtils;

@Component
public class Oauth2TokenRepositoryRedisImpl implements Oauth2TokenRepository {

	private static final String REDIS_ACCESSTOKEN_PRE = "access_token:";
	private static final String REDIS_REFRESH_TOKEN_PRE = "refresh_token:";

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Override
	public void saveAccessToken(TokenBean token, long timeout) {
		String json = JsonUtils.pojoToJson(token);
		redisTemplate.boundValueOps(REDIS_ACCESSTOKEN_PRE + token.getToken()).set(json, timeout, TimeUnit.SECONDS);
	}

	@Override
	public TokenBean getAccessToken(String token) {
		String json = redisTemplate.boundValueOps(REDIS_ACCESSTOKEN_PRE + token).get();
		if (json == null) {
			return null;
		}
		TokenBean tk = JsonUtils.jsonToPojo(json, TokenBean.class);
		tk.setToken(token);
		return tk;
	}

	@Override
	public void saveRefreshToken(TokenBean token, long timeout) {
		String json = JsonUtils.pojoToJson(token);
		redisTemplate.boundValueOps(REDIS_REFRESH_TOKEN_PRE + token.getToken()).set(json, timeout, TimeUnit.SECONDS);
	}

	@Override
	public TokenBean getRefreshToken(String token) {
		String json = redisTemplate.boundValueOps(REDIS_REFRESH_TOKEN_PRE + token).get();
		if (json == null) {
			return null;
		}
		TokenBean tk = JsonUtils.jsonToPojo(json, TokenBean.class);
		tk.setToken(token);
		return tk;
	}

}
