package com.github.qq275860560.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class SHA1HexUtil {
	private static Log log = LogFactory.getLog(SHA1HexUtil.class);

	private SHA1HexUtil() {
	}

	public static String sha1Hex(final String data) {
		log.debug("sha1Hex start");
		return org.apache.commons.codec.digest.DigestUtils.sha1Hex(data);
	}

}
