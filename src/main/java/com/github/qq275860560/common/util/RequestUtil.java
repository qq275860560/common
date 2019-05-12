package com.github.qq275860560.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;

/**
 * @author jiangyuanlin@163.com
 请求对象解释器
*/
public class RequestUtil {

	private static Log log = LogFactory.getLog(RequestUtil.class);

	// http请求的参数变成Map或者List的工具类;对于ContentType为application/x-www-form-urlencoded的格式一般为a=1&b=2&b=3，解析后的Map有两个key，其中一个key为a，value为"1"，另一个key为b，value为new
	// String[]{"2","3"};
	// 对于Content-Type=application/json;charset=UTF-8的消息体格式为标准json字符串，调用jackson反序列化

	private RequestUtil() {
	}

	// 将ContentType=application/x-www-form-urlencoded的请求参数解析到Map中

	public static Map<String, Object> parameterToMap(HttpServletRequest request) {
		try {
			if (request == null)
				return Collections.EMPTY_MAP;
			Map<String, Object> requestMap = new HashMap<>();
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				String[] values = request.getParameterValues(paraName);
				if (values.length == 1)
					requestMap.put(paraName, values[0]);
				else
					requestMap.put(paraName, values);
			}
			return requestMap;
		} catch (Exception e) {
			log.error("", e);
			return Collections.EMPTY_MAP;
		}
	}

	// 将Content-Type=application/json;charset=UTF-8的请求体解析到Map中

	public static Map<String, Object> bodyToMap(HttpServletRequest request) {
		try {
			if (request == null)
				return Collections.EMPTY_MAP;
			String result = toString(request);
			if (StringUtils.isBlank(result))
				return Collections.EMPTY_MAP;
			// Map<String,Object> requestMap = mapper.readValue(result, Map.class);
			// for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
			// entry.setValue(URLDecoder.decode(entry.getValue().toString(),"UTF-8"));
			// }
			// return requestMap;
			return JsonUtil.parse(result, Map.class);
		} catch (Exception e) {
			log.error("", e);
			return Collections.EMPTY_MAP;
		}
	}

	public static Map<String, Object> bodyToMap(HttpEntityEnclosingRequest request) {
		try {
			if (request == null)
				return Collections.EMPTY_MAP;
			String result = toString(request);
			if (StringUtils.isBlank(result))
				return Collections.EMPTY_MAP;
			// Map<String,Object> requestMap = mapper.readValue(result, Map.class);
			// for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
			// entry.setValue(URLDecoder.decode(entry.getValue().toString(),"UTF-8"));
			// }
			// return requestMap;
			return JsonUtil.parse(result, Map.class);
		} catch (Exception e) {
			log.error("", e);
			return Collections.EMPTY_MAP;
		}
	}

	// 将Content-Type为application/json;charset=UTF-8的请求体解析到List中

	public static List<Object> bodyToList(HttpServletRequest request) {
		try {
			if (request == null)
				return Collections.EMPTY_LIST;
			String result = toString(request);
			if (StringUtils.isBlank(result))
				return Collections.EMPTY_LIST;
			return JsonUtil.parse(result, List.class);
		} catch (Exception e) {
			log.error("", e);
			return Collections.EMPTY_LIST;
		}
	}

	private static String toString(HttpServletRequest request) throws IOException, UnsupportedEncodingException {
		InputStream inputStream = request.getInputStream();
		return toString(inputStream);
	}

	private static String toString(HttpEntityEnclosingRequest request)
			throws IOException, UnsupportedEncodingException {
		InputStream inputStream = request.getEntity().getContent();
		return toString(inputStream);
	}

	private static String toString(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, length);
		}
		return bos.toString("UTF-8");
	}

	// 将ContentType=application/x-www-form-urlencoded的Map转为请求参数

	public static String mapToParameter(Map<String, Object> requestMap) {
		try {
			if (requestMap == null)
				return "";
			String result = "";
			for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
				if (entry.getValue() instanceof String[]) {
					String[] values = (String[]) entry.getValue();
					for (String value : values) {
						result += URLEncoder.encode(entry.getKey(), "UTF-8").replace("+", "%20") + "="
								+ URLEncoder.encode(value, "UTF-8").replace("+", "%20") + "&";
					}
				} else {
					result += URLEncoder.encode(entry.getKey(), "UTF-8").replace("+", "%20") + "="
							+ URLEncoder.encode(entry.getValue().toString(), "UTF-8").replace("+", "%20") + "&";
				}

			}
			if (result.length() > 0)
				result = result.substring(0, result.length() - 1);
			return result;
		} catch (Exception e) {
			log.error("", e);
			return "";
		}
	}

	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

}
