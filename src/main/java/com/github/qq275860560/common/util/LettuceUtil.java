package com.github.qq275860560.common.util;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.protocol.RedisCommand;

import redis.clients.jedis.Jedis;

/**
 * @author jiangyuanlin@163.com
 */
public class LettuceUtil {

	public static void set(String ip, int port, String password, String key, String value) {
		Jedis jedis = new Jedis(ip, port);
		if (password != null)
			jedis.auth(password);
		jedis.set(key, value);
	}

	public static String get(String ip, int port, String password, String key) {
		Jedis jedis = new Jedis(ip, port);
		if (password != null)
			jedis.auth(password);
		return jedis.get(key);
	}

	public static void main(String[] args) {
		RedisURI redisURI = new RedisURI();
		redisURI.setHost("132.122.237.68");
		redisURI.setPort(6379);
		redisURI.setPassword("123456");
		RedisClient client = RedisClient.create(redisURI);
		StatefulRedisConnection<String, String> connection = client.connect();
		com.lambdaworks.redis.api.sync.RedisCommands<String, String> commands = connection.sync();
		String str = commands.get("foo");
	}

}
