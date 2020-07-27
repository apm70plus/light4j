package com.light.auth.config.session;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

public class ShiroCacheAdapter<K, V> implements Cache<K, V> {

    private org.springframework.cache.Cache springCache;

    public ShiroCacheAdapter(org.springframework.cache.Cache springCache) {
        this.springCache = springCache;
    }

    @Override
    public V get(K k) throws CacheException {
        return (V)springCache.get(k);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        springCache.put(k, v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = (V)springCache.get(k);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        springCache.clear();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
