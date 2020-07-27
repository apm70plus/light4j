package com.daliu.sample.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 登录重试计数器单机版
 * @author daliu
 *
 */
public class DefaultLoginRetryCounter extends LoginRetryCounter {

	/**
	 * 缓存（单机版，如果是集群部署，可以考虑hazelcast或redis实现）
	 */
	private Cache<String, Integer> cache;

	public DefaultLoginRetryCounter(int maxRetryTimes, int lockMinutes) {
		super(maxRetryTimes, lockMinutes);
		cache = CacheBuilder.newBuilder().expireAfterWrite(lockMinutes, TimeUnit.MINUTES).build();
	}

	@Override
	protected int getFailureTimes(String username) {
		Integer count = cache.getIfPresent(username);
		if (count == null) {
			return 0;
		} else {
			return count;
		}
	}

	@Override
	protected void resetFailureTimes(String username) {
		cache.invalidate(username);
	}

	@Override
	protected void incrementFailureTimes(String username) {
		Integer count = cache.getIfPresent(username);
		if (count == null) {
			count = 0;
		}
		count = count + 1;
		cache.put(username, count);
	}

}
