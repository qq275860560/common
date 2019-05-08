package com.github.qq275860560.common.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.qq275860560.common.model.ApiResult;

/**
 * @author jiangyuanlin@163.com
 */
public class JsonUtil {
	private static Log log = LogFactory.getLog(JsonUtil.class);

	private JsonUtil() {
	}

	private static ObjectMapper mapper = new ObjectMapper().configure(Feature.ESCAPE_NON_ASCII, true)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE));;

	// 将对象序列化成Json字符串

	/**
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		if (obj == null)
			return null;
		// return JSONUtils.toJSONString(obj);

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error(null, e);
		}
		return jsonString;

	}

	// 将Json字符串反序列化成对象

	public static <T> T parse(String jsonString, Class<T> clazz) {
		// return clazz.cast(JSONUtils.parse(jsonString));
		T t = null;
		try {
			t = mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			log.error(null, e);
		}
		return t;
	}

	// 将输入流反序列化成对象

	public static <T> T parse(InputStream inputStream, Class<T> clazz) {
		T t = null;
		try {
			t = mapper.readValue(inputStream, clazz);
		} catch (Exception e) {
			log.error(null, e);
		}
		return t;
	}

	public static String getValue(String jsonKey, String content) {
		String result = null;
		try {
			com.fasterxml.jackson.databind.JsonNode jsoNode = mapper.readTree(content);
			result = jsoNode.get(jsonKey).asText();
		} catch (Exception e) {
			log.error(null, e);
		}
		return result;
	}

	public static String format(String content) {
		if (null == content || "".equals(content))
			return "";
		StringBuilder sb = new StringBuilder();
		char current = '\0';
		int indent = 0;
		boolean isInQuotationMarks = false;
		for (int i = 0; i < content.length(); i++) {
			char last = current;
			current = content.charAt(i);
			switch (current) {
			case '"':
				if (last != '\\') {
					isInQuotationMarks = !isInQuotationMarks;
				}
				sb.append(current);
				break;
			case '{':
			case '[':
				sb.append(current);
				if (!isInQuotationMarks) {
					sb.append('\n');
					indent++;
					addIndentBlank(sb, indent);
				}
				break;
			case '}':
			case ']':
				if (!isInQuotationMarks) {
					sb.append('\n');
					indent--;
					addIndentBlank(sb, indent);
				}
				sb.append(current);
				break;
			case ',':
				sb.append(current);
				if (last != '\\' && !isInQuotationMarks) {
					sb.append('\n');
					addIndentBlank(sb, indent);
				}
				break;
			default:
				sb.append(current);
			}
		}

		return sb.toString();
	}

	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

	public static void main(String[] args) {

		String name = getValue("age", "{\"name\":\"zhangsan张三\",\"age\":100}");
		log.info(name);

		log.info(JsonUtil.toJSONString(new ApiResult(0, "请求成功", null)));
	}

}
