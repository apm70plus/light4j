package com.light.oauth2.repository.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.light.oauth2.model.AuthorizationCode;
import com.light.oauth2.repository.AuthorizationCodeRepository;
import com.light.util.JsonUtils;

@Component
public class AuthorizationCodeRepositoryRedisImpl implements AuthorizationCodeRepository {

	private static final String REDIS_PRE = "auth_code:";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void save(AuthorizationCode code, long timeout) {
		String codeJson = JsonUtils.pojoToJson(code);
		redisTemplate.boundValueOps(REDIS_PRE + code.getCode()).set(codeJson, timeout, TimeUnit.SECONDS);
	}

	@Override
	public AuthorizationCode findByCode(String code) {
		String codeJson = redisTemplate.boundValueOps(REDIS_PRE + code).get();
		if (codeJson == null) {
			return null;
		}
		AuthorizationCode c = JsonUtils.jsonToPojo(codeJson, AuthorizationCode.class);
		c.setCode(code);
		return c;
	}

	@Override
	public void deleteByCode(String code) {
		redisTemplate.delete(REDIS_PRE + code);
	}
}
