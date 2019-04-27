package com.github.qq275860560.common.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import net.bull.javamelody.Main;
import redis.clients.jedis.Jedis;

/**
 * @author jiangyuanlin@163.com
 */
public class JedisUtil {
	
	public static void set(String ip,int port,String password,String key,String value) {
		Jedis jedis = new Jedis(ip, port);
		if(password!=null)jedis.auth(password);
		jedis.set(key,value);
	}
	
	public static String get(String ip,int port,String password,String key) {
		Jedis jedis = new Jedis(ip, port);
		if(password!=null)jedis.auth(password);
		return jedis.get(key);
	}
	
	public static void lpush(String ip,int port,String password,String key,String value) {
		Jedis jedis = new Jedis(ip, port);
		if(password!=null)jedis.auth(password);
		jedis.lpush(key, value);
	}
	
	public static String rpop(String ip,int port,String password,String key) {
		Jedis jedis = new Jedis(ip, port);
		if(password!=null)jedis.auth(password);
		return jedis.rpop(key);
	}
	
	public static void main(String[] args) {
		set("132.122.237.68", 6379, "123456", "foo", "bar");
		System.out.println(get("132.122.237.68", 6379, "123456", "foo"));
				
		lpush("132.122.237.68", 6379, "123456", "queue", "value");
		System.out.println(rpop("132.122.237.68", 6379, "123456", "queue"));		
	}
	
 
	
		

}
