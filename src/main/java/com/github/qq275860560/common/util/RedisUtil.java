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
	//环境变量,可选值DEV0,TEST,QA00,PROD
	private static String environment;
	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "redis.properties"));
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxTotal(configuration.getInt("maxtotal"));
			jedisPoolConfig.setMaxIdle(configuration.getInt("maxidle"));
			environment=configuration.getString("environment");
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

	/**
	 * 根据key获取记录
	 *
	 * @param key
	 * @return 值
	 */
	public static String get(String key) {
		return jedisCluster.get(key+":"+environment);

	}

	/**
	 * 添加没有过期时间的记录
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @return String 操作状态
	 */
	public static String set(String key, String value, String nxxx, String expx, long time) {
		return jedisCluster.set(key+":"+environment, value, nxxx, expx, time);
	}

	/**
	 * 添加没有过期时间的记录
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @return String 操作状态
	 */
	public static String set(String key, String value) {

		return jedisCluster.set(key+":"+environment, value);
	}

	/**
	 * 添加有过期时间的记录
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @return String 操作状态
	 */
	public static String setex(String key, int seconds, String value) {
		return jedisCluster.setex(key+":"+environment, seconds, value);

	}
	
	/**
	 * 添加存在不新增的记录,并且有超时功能
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @return String 操作状态
	 */
	public static String setnx(String key, int seconds, String value) {
		return jedisCluster.set(key+":"+environment,value,"NX","EX", seconds);
	}
	

	/**
	 * 删除keys对应的记录,可以是多个key
	 *
	 * @param keys
	 * @return 删除的记录数
	 */
	public static long del(String... keys) {
		String[] newkeys=new String[keys.length];
		for(int i=0;i<keys.length;i++){
			newkeys[i]=keys[i]+":"+environment;
		}
		return jedisCluster.del(newkeys);
	}

	/**
	 * 判断key是否存在，存在返回true
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(String key) {
		return jedisCluster.exists(key+":"+environment);
	}

	/**
	 * 设置过期时间
	 *
	 * @param key
	 * @param seconds
	 * @return 返回影响的记录数
	 */
	public static void expire(String key, int seconds) {
		jedisCluster.expire(key+":"+environment, seconds);

	}

	/**
	 * 查询key的过期时间
	 *
	 * @param key
	 * @return 以秒为单位的时间表示
	 */
	public static long ttl(String key) {
		return jedisCluster.ttl(key+":"+environment);
	}


	/**
	 * 左边入队
	 *
	 * @param key
	 * @return  
	 */
	public static long lpush(String key,String value) {
		return jedisCluster.lpush(key+":"+environment,value);
	}

	/**
	 * 右边出对
	 *
	 * @param key
	 * @return  
	 */
	public static String rpop(String key) {
		return jedisCluster.rpop(key+":"+environment);
	}
	
	 
	
 
}