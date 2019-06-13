package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;
/**
  * @author jiangyuanlin@163.com
RSA字符串生成器
 */
@Slf4j
public class RsaUtil {

	public static void main(String[] args) throws Exception {
		Map<String, String> map=generateRsaKeyBase64EncodeStringWithApache();

		PrivateKey privateKey = getPrivateKeyFromPrivateKeyBase64EncodeStringWithSunMiscBASE64Encoder(map.get("privateKeyBase64EncodeString"));
		log.info(""+privateKey.getAlgorithm());
		PublicKey publicKey = getPublicKeyFromPublicKeyBase64EncodeStringWithSunMiscBASE64Decoder(map.get("publicKeyBase64EncodeString"));
		log.info(""+publicKey.getAlgorithm());
		
		KeyPair keyPair = new KeyPair(publicKey, privateKey);
		 
	}

	private static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	/*
	byte[] cipherData = Base64.encodeBase64(plainText.getBytes()); //默认不换行    
	byte[] cipherData = Base64.encodeBase64(plainText.getBytes(), false); //取消换行  
	byte[] cipherData = Base64.encodeBase64Chunked(plainText.getBytes()); //进行换行  
	*/
	public static Map<String, String>  generateRsaKeyBase64EncodeStringWithApache() throws NoSuchAlgorithmException {
		KeyPair keyPair = generateRsaKeyPair();
		String privateKeyBase64EncodeString = new String(Base64.encodeBase64Chunked(keyPair.getPrivate().getEncoded()));
		log.info("privateKeyBase64EncodeString=\n" +"-----BEGIN RSA PRIVATE KEY-----\r\n"+ privateKeyBase64EncodeString+"-----END RSA PRIVATE KEY-----");
		String publicKeyBase64EncodeString = new String(Base64.encodeBase64Chunked(keyPair.getPublic().getEncoded()));
		log.info("publicKeyBase64EncodeString=\n" + "-----BEGIN PUBLIC KEY-----\r\n"+ publicKeyBase64EncodeString+"-----END PUBLIC KEY-----");
		return new HashMap<String, String>() {
			{
				put("privateKeyBase64EncodeString", privateKeyBase64EncodeString);
				put("publicKeyBase64EncodeString", publicKeyBase64EncodeString);

			}
		};

	}
	
	public static Map<String, String>  generateRsaKeyBase64EncodeStringWithSunMiscBASE64Encoder() throws NoSuchAlgorithmException {
		KeyPair keyPair = generateRsaKeyPair();
		String privateKeyBase64EncodeString = new sun.misc.BASE64Encoder().encode(keyPair.getPrivate().getEncoded());
		log.info("privateKeyBase64EncodeString=\n" +"-----BEGIN RSA PRIVATE KEY-----\n"+ privateKeyBase64EncodeString+"\n-----END PRIVATE KEY-----");
		String publicKeyBase64EncodeString = new sun.misc.BASE64Encoder().encode(keyPair.getPublic().getEncoded());
		log.info("publicKeyBase64EncodeString=\n" + "-----BEGIN RSA PUBLIC KEY-----\n"+ publicKeyBase64EncodeString+"\n-----END PUBLIC KEY-----");
		return new HashMap<String, String>() {
			{
				put("privateKeyBase64EncodeString", privateKeyBase64EncodeString);
				put("publicKeyBase64EncodeString", publicKeyBase64EncodeString);

			}
		};

	}
	
	
	public static PrivateKey getPrivateKeyFromPrivateKeyBase64EncodeStringWithSunMiscBASE64Encoder(String privateKeyBase64EncodeString)
			throws Exception {
		byte[] keyBytes =new sun.misc.BASE64Decoder().decodeBuffer (privateKeyBase64EncodeString);
		PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
		return keyFactory_privateKey.generatePrivate(keySpec_privateKey);
	}
	
	public static PublicKey getPublicKeyFromPublicKeyBase64EncodeStringWithSunMiscBASE64Decoder(String publicKeyBase64EncodeString)
			throws Exception {
		byte[] keyBytes =new sun.misc.BASE64Decoder().decodeBuffer(publicKeyBase64EncodeString);
		X509EncodedKeySpec keySpec_publicKey = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_publicKey = KeyFactory.getInstance("RSA");
		return keyFactory_publicKey.generatePublic(keySpec_publicKey);

	}
	

	public static Map<String, String> generateRsaKeyBase64EncodeString() throws Exception {
		KeyPair keyPair = generateRsaKeyPair();
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

	public static PrivateKey getPrivateKeyFromPrivateKeyBase64EncodeString(String privateKeyBase64EncodeString)
			throws Exception {
		byte[] keyBytes = Base64Utils.decode(privateKeyBase64EncodeString.getBytes());
		PKCS8EncodedKeySpec keySpec_privateKey = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_privateKey = KeyFactory.getInstance("RSA");
		return keyFactory_privateKey.generatePrivate(keySpec_privateKey);
	}

	public static PublicKey getPublicKeyFromPublicKeyBase64EncodeString(String publicKeyBase64EncodeString)
			throws Exception {
		byte[] keyBytes = Base64Utils.decode(publicKeyBase64EncodeString.getBytes());
		X509EncodedKeySpec keySpec_publicKey = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory_publicKey = KeyFactory.getInstance("RSA");
		return keyFactory_publicKey.generatePublic(keySpec_publicKey);

	}
	
	

	

	public static void generateRsaKeyFile(File privateKeyFile, File publicKeyFile) throws Exception {
		KeyPair keyPair = generateRsaKeyPair();
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(privateKeyFile))) {
			objectOutputStream.writeObject(keyPair.getPrivate());
			log.debug("privateKey生成正常" + privateKeyFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("", e);
		}

		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(publicKeyFile))) {
			objectOutputStream.writeObject(keyPair.getPublic());
			log.debug("publicKey生成正常" + publicKeyFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public static PrivateKey getPrivateKeyFromPrivateKeyFile(File privateKeyFile) throws Exception {
		PrivateKey privateKey = null;
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(privateKeyFile))) {
			privateKey = (PrivateKey) objectInputStream.readObject();
			log.debug("privateKey读取正常" + privateKeyFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("", e);
		}
		return privateKey;

	}

	public static PublicKey getPublicKeyFromPublicKeyFile(File publicKeyFile) throws Exception {
		PublicKey publicKey = null;
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(publicKeyFile))) {
			publicKey = (PublicKey) objectInputStream.readObject();
			log.debug("publicKey读取正常" + publicKeyFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("", e);
		}
		return publicKey;

	}

}
