package com.github.qq275860560.common.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 适用场景，比如jwt对token进行非对称加密，需要公钥和私钥，把生成的公钥和私钥字符串（base64形式）放到配置文件
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class RsaUtil {
	public static void main(String[] args) throws Exception {
		generateRsaKeyBase64EncodeString();

	}

	public static Map<String, String> generateRsaKeyBase64EncodeString() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		String privateKeyBase64EncodeString = Base64Utils.encodeToString(keyPair.getPrivate().getEncoded());
		log.info("privateKeyBase64EncodeString=" + privateKeyBase64EncodeString);
		String publicKeyBase64EncodeString = Base64Utils.encodeToString(keyPair.getPublic().getEncoded());
		log.info("publicKeyBase64EncodeString=" + publicKeyBase64EncodeString);

		return new HashMap<String, String>() {
			{
				put("privateKeyBase64EncodeString", privateKeyBase64EncodeString);
				put("publicKeyBase64EncodeString", publicKeyBase64EncodeString);

			}
		};

	}
	
 
	public static PrivateKey getPrivateKey(String privateKeyBase64EncodeString) throws Exception {		 
		byte[] keyBytes = Base64Utils.decode(privateKeyBase64EncodeString.getBytes());
		PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
		return keyFactory_privateKey.generatePrivate(keySpec_privateKey);
	}
	
	public static PublicKey getPublicKey(String publicKeyBase64EncodeString ) throws Exception {	 
		byte[] keyBytes = Base64Utils.decode(publicKeyBase64EncodeString.getBytes());
		X509EncodedKeySpec keySpec_publicKey = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_publicKey = KeyFactory.getInstance("RSA");
		return keyFactory_publicKey.generatePublic(keySpec_publicKey);

	}

	

}
