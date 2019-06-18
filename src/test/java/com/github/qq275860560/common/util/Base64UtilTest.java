package com.github.qq275860560.common.util;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class Base64UtilTest {
	 
	
	@Ignore
	@Test
	public void decodebase64File() throws Exception {
		URL source = new URL("https://www.baidu.com/img/bd_logo1.png");
		File destination = new File(System.getProperty("java.io.tmpdir") + File.separator + RandomGeneratorUtil.generateUUID()+ ".png");
		org.apache.commons.io.FileUtils.copyURLToFile(source, destination);
		String result = Base64Util.encodeBase64File(destination);
		log.info(result);
		File destination2 = new File(System.getProperty("java.io.tmpdir") + File.separator+ RandomGeneratorUtil.generateUUID() + ".png");
		Base64Util.decodeBase64File(destination2, result);
		// 验证加密前大小和加密后大小和md5是否一致
		Assert.assertEquals(destination.length(), destination2.length());
		Assert.assertEquals(MD5Util.md5Hex(destination), MD5Util.md5Hex(destination2));
	}

	@Test
	public void decodeBase64String() throws Exception {
		final String str = "123456你好";
		String result = Base64Util.encodeBase64String(str);
		log.info(result);
		String result2 = Base64Util.decodeBase64String(result);
		log.info(result2);
		Assert.assertEquals(str, result2);
	}

	
	
	@Test
	public void sign() throws Exception {
		final String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
		final String payload = "{\"from_user\":\"B\",\"target_user\":\"A\"}";
	    //final String payload = "{\"iss\":\"John Wu JWT\",\"iat\":1441593502,\"exp\":1441594722,\"aud\":\"www.example.com\",\"sub\":\"jrocket@example.com\",\"from_user\":\"B\",\"target_user\":\"A\"}";
 
		 
		String sign = Base64Util.encodeBase64String(header)+"."+Base64Util.encodeBase64String(payload);
		log.info(sign);
		Assert.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcm9tX3VzZXIiOiJCIiwidGFyZ2V0X3VzZXIiOiJBIn0=", sign);
		String sha256Hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(sign+".mystar");     
		
		log.info(sha256Hex);
		
		String jwt = sign + "."+sha256Hex;
	}
	
}
