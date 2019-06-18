package com.github.qq275860560.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class IntegerUtil {
	 	private IntegerUtil() {
	}

	// 低位低字节

	public static byte[] intToByte(int number) {
		return intToByte(number, 4);
	}

	public static byte[] intToByte(int number, int len) {
		byte[] result = new byte[len];
		for (int i = 0; i < 4 && i < len; i++) {

			// result[i] = new Integer(number & 0xff).byteValue();// 将最低位保存在最低位
			// number = number >> 8; // 向右移8位

			result[i] = (byte) (number >> 8 * i & 0xff);
		}
		return result;
	}

	// 低位低字节

	public static int byeToInt(byte[] bytes) {
		// int temp =bytes[3];
		// for (int i = 2; i >=0; i--) {
		// temp=(temp << 8 )| bytes[i];
		// }
		// return temp;

		int temp = bytes[bytes.length - 1];
		for (int i = bytes.length - 2; i >= 0; i--) {
			temp = (temp << 8) | bytes[i];
		}
		return temp;
	}

	public static void main(String[] args) {
		int number = 1404;
		byte[] result1 = intToByte(number, 3);
		System.out.println(result1[0] + "  " + result1[1] + " " + result1[2] + " " + result1[3]);
		byte[] bytes = new byte[] { 0x7c, 0x05, 0x00, 0x00 };
		int result = byeToInt(bytes);
		System.out.println(result);
	}

}
