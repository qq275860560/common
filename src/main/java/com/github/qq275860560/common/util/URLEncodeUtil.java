package com.github.qq275860560.common.util;

import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class URLEncodeUtil {
	private static Log log = LogFactory.getLog(URLEncodeUtil.class);

	private URLEncodeUtil() {
	}

	// url转码，解决乱码问题

	public static String encode(String urlPath) {// 防止乱码
		try {
			StringBuilder pathBuilder = new StringBuilder();
			String[] pathSegmentsArr = urlPath.split("/");
			for (String pathSegment : pathSegmentsArr) {
				if (!pathSegment.isEmpty()) {
					pathBuilder.append("/").append(URLEncoder.encode(pathSegment, "UTF-8").replace("+", "%20"));
				}
			}
			if (urlPath.endsWith("/")) {
				pathBuilder.append("/");
			}
			return pathBuilder.toString();
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
}
