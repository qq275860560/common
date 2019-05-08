package com.github.qq275860560.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializeUtil {
	private static final Log log = LogFactory.getLog(SerializeUtil.class);

	public static byte[] serialize(Object object) throws Exception {
		if (object == null)
			return null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public static Object unSerialize(byte[] bytes) throws Exception {
		if (bytes == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}
}
