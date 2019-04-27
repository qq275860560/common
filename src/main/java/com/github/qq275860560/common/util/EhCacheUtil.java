package com.github.qq275860560.common.util;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class EhCacheUtil {

	public static void main(String[] args) {
		CacheManagerBuilder cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder();
		CacheConfigurationBuilder cacheConfigurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,
				String.class, ResourcePoolsBuilder.heap(10));
		
		CacheManager cacheManager = cacheManagerBuilder	.withCache("preConfigured",cacheConfigurationBuilder )
				.build();
		// 初始化缓存管理器

		cacheManager.init();
		Cache<String,String> preConfigured = cacheManager.getCache("preConfigured", String.class, String.class);
		preConfigured.put("key1", "zheng pens");
		preConfigured.put("key1", "pens");
		System.out.println(preConfigured.get("key1"));
		System.out.println(preConfigured.containsKey("key1"));

		Cache cache2 = cacheManager.createCache("cache2", cacheConfigurationBuilder.build());
		cache2.put("key2", "hello");
		System.out.println(cache2.get("key2"));
	}

	public static Object get(String cacheName, String key) {
		return null;
	}

	public static void put(String cacheName, String key, Object value) {

	}

	public static void delete(String cacheName, String key) {

	}

}
