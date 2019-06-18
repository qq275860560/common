package com.github.qq275860560.common.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 * 生成每个用户的token
 */
@Slf4j
public class TokenUtil {
	private static String issUser;// 牌的创建者 网站或应用clientId
	private static String audience;// 令牌使用者 用户ID
	private static Float minutes;// 有效期时间，分钟

	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "application.properties"));
			issUser = configuration.getString("issUser");
			audience = configuration.getString("audience");
			minutes = Float.parseFloat(configuration.getString("minutes"));
		} catch (Exception e) {
			log.error("", e);
			System.exit(1);
		}
	}

	public static String encrypt(String user_id) throws Exception {
		Map<String, Object> payLoadMap = new HashMap<String, Object>();
		payLoadMap.put("user_id", user_id);
		return JwtUtil.encrypt(issUser, audience, minutes, payLoadMap);
	}

	public static String decrypt(String token) throws Exception {
		if (token == null) {
			throw new Exception("token不能为空");
		}
		Map<String, Object> payLoadMap0 = null;
		payLoadMap0 = JwtUtil.decrypt(token, issUser, audience);
		if (payLoadMap0 == null) {
			throw new Exception("无效token");
		} else {
			if (System.currentTimeMillis() > (Long) payLoadMap0.get("exp") * 1000) {
				throw new Exception("token已经过期,请使用接口getToken重新获取");
			}
		}
		return payLoadMap0.get("user_id").toString();
	}

}
