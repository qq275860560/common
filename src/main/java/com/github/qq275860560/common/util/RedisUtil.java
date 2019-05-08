package com.github.qq275860560.common.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author jiangyuanlin@163.com
 */
public class RedisUtil {

	private static Log log = LogFactory.getLog(RedisUtil.class);

	private RedisUtil() {
	}

	private static JedisCluster jedisCluster;
	// 环境变量,可选值DEV0,TEST,QA00,PROD
	private static String environment;
	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "redis.properties"));
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxTotal(configuration.getInt("maxtotal"));
			jedisPoolConfig.setMaxIdle(configuration.getInt("maxidle"));
			environment = configuration.getString("environment");
			Set<HostAndPort> set = new HashSet<HostAndPort>();
			String[] servers = configuration.getString("servers").split(",");
			for (int i = 0; i < servers.length; i++) {
				String[] info = servers[i].split(":");
				String ip = info[0];
				int port = 6379;
				if (info.length > 1) {
					port = Integer.parseInt(info[1]);
				}
				log.debug("redis ip=" + ip + ",port=" + port);
				set.add(new HostAndPort(ip, port));
			}
			jedisCluster = new JedisCluster(set, configuration.getInt("connectiontimeout"),
					configuration.getInt("sotimeout"), configuration.getInt("maxattempts"),
					configuration.getString("password"), jedisPoolConfig);
			log.debug("redis cluster name=" + jedisCluster.get("name"));
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static String get(String key) {
		return jedisCluster.get(key + ":" + environment);

	}

	public static String set(String key, String value, String nxxx, String expx, long time) {
		return jedisCluster.set(key + ":" + environment, value, nxxx, expx, time);
	}

	public static String set(String key, String value) {

		return jedisCluster.set(key + ":" + environment, value);
	}

	public static String setex(String key, int seconds, String value) {
		return jedisCluster.setex(key + ":" + environment, seconds, value);

	}

	public static String setnx(String key, int seconds, String value) {
		return jedisCluster.set(key + ":" + environment, value, "NX", "EX", seconds);
	}

	public static long del(String... keys) {
		String[] newkeys = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			newkeys[i] = keys[i] + ":" + environment;
		}
		return jedisCluster.del(newkeys);
	}

	public static boolean exists(String key) {
		return jedisCluster.exists(key + ":" + environment);
	}

	public static void expire(String key, int seconds) {
		jedisCluster.expire(key + ":" + environment, seconds);

	}

	public static long ttl(String key) {
		return jedisCluster.ttl(key + ":" + environment);
	}

	public static long lpush(String key, String value) {
		return jedisCluster.lpush(key + ":" + environment, value);
	}

	public static String rpop(String key) {
		return jedisCluster.rpop(key + ":" + environment);
	}

}