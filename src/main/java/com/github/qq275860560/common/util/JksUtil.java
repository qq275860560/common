package com.github.qq275860560.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */

@Slf4j
public class JksUtil {
	public static void main(String[] args) throws Exception {
		PrivateKey privateKey = getPrivateKey("jwt.jks", "123456", "jwt");
		log.info("" + privateKey);
		PublicKey publicKey = getPublicKey("jwt.jks", "123456", "jwt");
		log.info("" + publicKey);

	}

	private static PrivateKey getPrivateKey(String jwtJksFileName, String jwtJksPassword, String jwtJksAlias) throws KeyStoreException,
			IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jwtJksFileName);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, jwtJksPassword.toCharArray());

		return (PrivateKey) keyStore.getKey(jwtJksAlias, jwtJksPassword.toCharArray());

	}

	private static PublicKey getPublicKey(String jwtJksFileName, String jwtJksPassword, String jwtJksAlias) throws KeyStoreException,
			IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jwtJksFileName);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, jwtJksPassword.toCharArray());

		return keyStore.getCertificate(jwtJksAlias).getPublicKey();

	}

}
