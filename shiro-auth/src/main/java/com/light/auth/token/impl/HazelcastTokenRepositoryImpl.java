package com.light.auth.token.impl;

import com.hazelcast.core.IMap;
import com.light.auth.token.TokenRepository;

public class HazelcastTokenRepositoryImpl implements TokenRepository {

	private IMap<String, String> tokenMap;
	
	public HazelcastTokenRepositoryImpl(IMap<String, String> tokenMap) {
		this.tokenMap = tokenMap;
	}
	
	@Override
	public String getValue(String token) {
        String value = tokenMap.get(token);
		return value;
	}

	@Override
	public void put(String token, String value) {
		tokenMap.put(token, value);
	}

	@Override
	public void clearToken(String token) {
		tokenMap.delete(token);
	}

}
