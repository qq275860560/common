package com.github.qq275860560.common.util;

import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class SHA1HexUtil {
	 
	private SHA1HexUtil() {
	}

	public static String sha1Hex(final String data) {
		log.debug("sha1Hex start");
		return org.apache.commons.codec.digest.DigestUtils.sha1Hex(data);
	}

}
