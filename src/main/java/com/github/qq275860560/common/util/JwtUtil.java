package com.github.qq275860560.common.util;

import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author jiangyuanlin@163.com
 */
public class JwtUtil {
	private static Log log = LogFactory.getLog(JwtUtil.class);

	private JwtUtil() {
	}

	private static Key privateKey;// 公钥

	private static Key publicKey;// 私钥

	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "application.properties"));
			byte[] keyBytes = (new Base64()).decode(configuration.getString("privateKey"));
			PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
			privateKey = keyFactory_privateKey.generatePrivate(keySpec_privateKey);

			keyBytes = (new Base64()).decode(configuration.getString("publicKey"));
			X509EncodedKeySpec keySpec_publicKey = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory_publicKey = KeyFactory.getInstance("RSA");
			publicKey = keyFactory_publicKey.generatePublic(keySpec_publicKey);

		} catch (Exception e) {
			log.error("", e);
			System.exit(1);
		}
	}

	public static String encrypt(String issUser, String audience, Float minutes, Map<String, Object> payLoadMap)
			throws JoseException {
		if (StringUtils.isBlank(issUser)) {// 令牌创建者
			log.debug("令牌创建者为空");
			return null;
		}

		if (StringUtils.isBlank(audience)) {// 令牌使用者
			log.debug("令牌使用者为空");
			return null;
		}

		if (minutes == null) {
			log.debug("令牌有效时间为空");
			return null;
		}

		JwtClaims claims = new JwtClaims();
		claims.setIssuer(issUser); // 令牌创建者
		claims.setAudience(audience); // 令牌使用者
		claims.setExpirationTimeMinutesInTheFuture(minutes);// 有效时间
		claims.setGeneratedJwtId(); // 令牌ID，默认16
		claims.setIssuedAtToNow(); // 令牌的发出时间（设置为）
		claims.setNotBeforeMinutesInThePast(1); // time before which the token
												// is not yet valid (2 minutes
												// ago)
		claims.setSubject("subject"); // the subject/principal is whom the token
										// is about

		for (Map.Entry<String, Object> entry : payLoadMap.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue() == null ? "" : entry.getValue().toString();
			claims.setClaim(key, value);
		}
		claims.setClaim("audience", audience);// 此处将用户编号作为payload传入

		JsonWebSignature jws = new JsonWebSignature(); // A JWT is a JWS and/or
														// a JWE with JSON
														// claims as the
														// payload.
		jws.setPayload(claims.toJson());// 设置json web token 的 payLoad

		jws.setKey(privateKey);// 设置私钥
		jws.setKeyIdHeaderValue("k1");// key Id
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
		return jws.getCompactSerialization();

	}

	public static Map<String, Object> decrypt(String token, String isUser, String audience) throws InvalidJwtException {

		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime() // the
																						// JWT
																						// must
																						// have
																						// an
																						// expiration
																						// time
				.setAllowedClockSkewInSeconds(30) // allow some leeway in
													// validating time based
													// claims to account for
													// clock skew
				.setRequireSubject() // the JWT must have a subject claim
				.setExpectedIssuer(isUser) // whom the JWT needs to have been
											// issued by
				.setExpectedAudience(audience) // to whom the JWT is intended
												// for
				.setVerificationKey(publicKey) // verify the signature with the
												// public key
				.build(); // create the JwtConsumer instance

		// Validate the JWT and process it to the Claims
		JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
		Map<String, Object> payLoadMap = jwtClaims.getClaimsMap();
		log.debug("token 校验成功" + payLoadMap);
		return payLoadMap;

	}

}