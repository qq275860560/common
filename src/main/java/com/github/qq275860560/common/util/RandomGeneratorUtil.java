package com.github.qq275860560.common.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangyuanlin@163.com
 */
public class RandomGeneratorUtil {
	/**
	 * 
	 * @return 6位数字的随机数
	 */
	public static String generate6Num() {
		int[] array = { 0, 1, 2, 3, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 9; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++)
			result = result * 10 + array[i];
		return String.format("%06d", result);	
	}

 

	/**生成指定位数的数字型字符串，前面可以为0
	 * @param dight
	 * @return
	 */
	public static String generateNum(int dight) {
		int[] array = { 0, 1, 2, 3, 5, 6, 7, 8, 9 };
		StringBuilder sb = new StringBuilder();	
		Random r = new Random();
		for (int i = 0; i < dight; i++) {
			int num = r.nextInt(array.length);
			sb.append(array[num]);
		}
		return sb.toString();
	}

	/**生成指定位数的数字型字符串，前面可以为0
	 * @param dight
	 * @return
	 */
	public static String randomNumeric(int dight) {
		return RandomStringUtils.randomNumeric(dight); 
	}

	/**生成32的uuid，全球唯一
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
		

}
