package com.github.qq275860560.common.util;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class Base64Util {
 
	private Base64Util() {
	}

	// base64解密

	public static void decodeBase64File(File file, final String base64String) throws Exception {
		log.debug("encodeBase64File start");
		org.apache.commons.io.FileUtils.writeByteArrayToFile(file, Base64.decodeBase64(base64String));
	}

	// base64加密

	public static String encodeBase64File(final File file) throws Exception {
		log.debug("encodeBase64File start");
		return org.apache.commons.codec.binary.Base64
				.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(file));
	}

	public static String encodeBase64String(final String str) throws Exception {
		log.debug("encodeBase64String start");
		return Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8));
	}

	public static String decodeBase64String(final String base64String) throws Exception {
		log.debug("decodeBase64String start");
		return new String(Base64.decodeBase64(base64String), StandardCharsets.UTF_8);
	}

}
