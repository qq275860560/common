package com.github.qq275860560.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author jiangyuanlin@163.com
 */
public class FunnyUtil {

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

	// 打印佛祖
	public static String getSakyamuni() {
		return "//                            _ooOoo_\r\n" + "//                           o8888888o\r\n"
				+ "//                           88\" . \"88\r\n" + "//                           (| -_- |)\r\n"
				+ "//                            O\\ = /O\r\n" + "//                        ____/`---'\\____\r\n"
				+ "//                      .   ' \\\\| |// `.\r\n" + "//                       / \\\\||| : |||// \\\r\n"
				+ "//                     / _||||| -:- |||||- \\\r\n"
				+ "//                       | | \\\\\\ - /// | |\r\n"
				+ "//                     | \\_| ''\\---/'' | |\r\n"
				+ "//                      \\ .-\\__ `-` ___/-. /\r\n"
				+ "//                   ___`. .' /--.--\\ `. . __\r\n"
				+ "//                .\"\" '< `.___\\_<|>_/___.' >'\"\".\r\n"
				+ "//               | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\r\n"
				+ "//                 \\ \\ `-. \\_ __\\ /__ _/ .-` / /\r\n"
				+ "//         ======`-.____`-.___\\_____/___.-`____.-'======\r\n"
				+ "//                            `=---='\r\n" + "//\r\n"
				+ "//         .............................................\r\n"
				+ "//                  佛祖镇楼                  BUG辟易\r\n" + "//          佛曰:\r\n"
				+ "//                  写字楼里写字间，写字间里程序员；\r\n" + "//                  程序人员写程序，又拿程序换酒钱。\r\n"
				+ "//                  酒醒只在网上坐，酒醉还来网下眠；\r\n" + "//                  酒醉酒醒日复日，网上网下年复年。\r\n"
				+ "//                  但愿老死电脑间，不愿鞠躬老板前；\r\n" + "//                  奔驰宝马贵者趣，公交自行程序员。\r\n"
				+ "//                  别人笑我忒疯癫，我笑自己命太贱；\r\n" + "//                  不见满街漂亮妹，哪个归得程序员？";

	}

	public static void main(String[] args) throws Exception {
		// 升序排序
		System.out.println(sort(Arrays.asList(6, 7, 8, 9, 10, 1, 2, 3, 4, 5)));
		// 获取一天后的时间
		System.out.println(getNextDate());
		// 显示佛祖
		System.out.println(getSakyamuni());
	}

}
