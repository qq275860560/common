package com.github.qq275860560.common.util;

import java.io.File;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class MD5Util {
	private static Log log = LogFactory.getLog(MD5Util.class);

	private MD5Util() {
	}

	// 建议使用MD5Util。md5Hex() md5加密

	public static String md5(final String data) {
		log.trace("md5 start");
		return new String(org.apache.commons.codec.digest.DigestUtils.md5(data));
	}

	// 建议使用MD5Util。md5Hex() md5加密

	public static String md5(final File file) throws Exception {
		log.trace("md5 start");
		return new String(org.apache.commons.codec.digest.DigestUtils
				.md5(org.apache.commons.io.FileUtils.readFileToByteArray(file)));
	}

	public static String md5Hex(final String data) {
		log.trace("md5Hex start");
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
	}

	public static String md5Hex(final byte[] bytes) {
		log.trace("md5Hex start");
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(bytes);
	}

	// md5加密

	public static String md5Hex(final File file) throws Exception {
		log.trace("md5Hex start");
		return org.apache.commons.codec.digest.DigestUtils
				.md5Hex(org.apache.commons.io.FileUtils.readFileToByteArray(file));
	}

	// 生成md5随机字符串，可以作为outh2中的authorizationCode,token,refreshToken等场景使用(如果是服务端保存状态的话)，如果是客户端保存状态，建议使用jwt

	public static String generateValue() {
		return md5Hex(UUID.randomUUID().toString());
	}
}
