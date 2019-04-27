package com.github.qq275860560.common.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiangyuanlin@163.com
 * 仿照redis的语法建立的本地jvm缓存 *
 */
public class CacheUtil {

	public static ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

	public static boolean exists(String key) {
		return map.containsKey(key);
	}

	public static Object get(String key) {
		return map.get(key);
	}

	public static void set(String key, Object object) {
		map.put(key, object);
	}
	public static void del(String key){
		map.remove(key);
	}

}
