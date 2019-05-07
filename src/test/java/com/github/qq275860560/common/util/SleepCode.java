package com.github.qq275860560.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author jiangyuanlin@163.com
 */
public class SleepCode {

	// 升序排序
	public static List<Integer> sort(List<Integer> list) throws Exception {
		final List<Integer> resultList = new ArrayList<>();

		for (final int i : list) {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(i * 1000);
						resultList.add(i);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};

			}.start();
		}
		;

		while (true) {
			if (Thread.activeCount() == 1) {
				break;
			}
			Thread.sleep(100);
		}
		return resultList;
	}

	// 获取一天后的时间
	public static Date getNextDate() throws Exception {
		Thread.sleep(24 * 3600 * 1000);
		return new Date();
	}

}
