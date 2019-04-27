package com.github.qq275860560.common.decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.github.qq275860560.common.util.JsonUtil;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;

/**
 * @author jiangyuanlin@163.com
 */
public class MapDecode implements Decoder {
	@Override
	public Object decode(Response response, Type type) throws IOException {
		Response.Body body = response.body();
		String responsBody = Util.toString(body.asReader());
		return JsonUtil.parse(responsBody, Map.class);
	}
}