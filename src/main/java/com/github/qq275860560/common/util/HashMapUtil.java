package com.github.qq275860560.common.util;

import java.util.HashMap;

/**
 * @author jiangyuanlin@163.com
 */
public class HashMapUtil {

	private HashMapUtil() {
	}

	private HashMap<String, Object> map = new HashMap<>();

	static class Builder {
		private HashMapUtil hashMapUtil;

		public Builder() {
			hashMapUtil = new HashMapUtil();
		}

		public Builder put(String key, Object value) {
			hashMapUtil.put(key, value);
			return this;
		}

		public HashMap<String, Object> build() {
			return hashMapUtil.map;
		}
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

}
