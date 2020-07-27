package com.light.auth.config.session;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class ShiroCacheManager implements CacheManager {

    private org.springframework.cache.CacheManager springCacheManager;

    public ShiroCacheManager(org.springframework.cache.CacheManager springCacheManager) {
        this.springCacheManager = springCacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCacheAdapter(springCacheManager.getCache(s));
    }
}
