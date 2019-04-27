package com.github.qq275860560.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class SHA1HexUtil {
	private static Log log = LogFactory.getLog(SHA1HexUtil.class);

	private SHA1HexUtil(){}
	/**
	 * sha1加密
	 * 
	 * @param data
	 *            要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String sha1Hex(final String data) {
		log.debug("sha1Hex start");
		return org.apache.commons.codec.digest.DigestUtils.sha1Hex(data);
	}

}
