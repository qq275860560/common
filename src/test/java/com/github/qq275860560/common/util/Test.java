package com.github.qq275860560.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.net.telnet.TelnetClient;

import redis.clients.jedis.Jedis;

/**
 * @author jiangyuanlin@163.com
 */
public class Test {
 
 public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.199.192", 6379);
		// jedis.auth("123456");
		jedis.set("redis", "redis");
		System.out.println(jedis.get("redis"));
	 
		//jedis.close();

}

}