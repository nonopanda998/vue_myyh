package com.myyh.system.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class CacheManagerUtil {

	@Resource
	private CacheManager cacheManager;

	public void put(String cacheName, String key, Object value) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(key,value);
	}

	public Object get(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		return cache.get(String.valueOf(key), Object.class);
	}


	public void del(String cacheName,String key) {
		cacheManager.getCache(cacheName).put(key,null);
	}

}
