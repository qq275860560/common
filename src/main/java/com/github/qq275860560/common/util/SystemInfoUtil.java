package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimerTask;

import javax.annotation.PreDestroy;

import com.sun.management.OperatingSystemMXBean;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class SystemInfoUtil {
	 	private static final int CPUTIME = 5000;
	// 网管进程信息采集周期(注意：PERIOD_TIME 一定要大于 SLEEP_TIME )

	private static final int PERIOD_TIME = 1000 * 60 * 15;
	// 此类中Thread.sleep()里的线程睡眠时间

	private static final int SLEEP_TIME = 1000 * 60 * 9;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;
	private static String isWindowsOrLinux = isWindowsOrLinux();
	private static String pid = "";

	// 判断是服务器的系统类型是Windows 还是 Linux

	public static String isWindowsOrLinux() {
		String osName = System.getProperty("os.name");
		String sysName = "";
		if (osName.toLowerCase().startsWith("windows")) {
			sysName = "windows";
		} else if (osName.toLowerCase().startsWith("linux")) {
			sysName = "linux";
		}
		return sysName;
	}

	// 获取JVM 的CPU占用率（%）

	public static String getCPURate() {
		String cpuRate = "";
		if (isWindowsOrLinux.equals("windows")) { // 判断操作系统类型是否为：windows
			cpuRate = "0";
		} else {
			cpuRate = getCPURateForLinux();
		}
		return cpuRate;
	}
	// windows环境下获取JVM的PID

	public void getJvmPIDOnWindows() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		pid = runtime.getName().split("@")[0];
		log.info("PID of JVM:" + pid);
	}

	// linux环境下获取JVM的PID

	public void getJvmPIDOnLinux() {
		String command = "pidof java";
		BufferedReader in = null;
		Process pro = null;
		try {
			pro = Runtime.getRuntime().exec(new String[] { "sh", "-c", command });
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			StringTokenizer ts = new StringTokenizer(in.readLine());
			pid = ts.nextToken();
			log.info("PID of JVM:" + pid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取JVM的内存占用率（%）

	public String getMemoryRate() {
		String memRate = "";
		if (isWindowsOrLinux.equals("windows")) { // 判断操作系统类型是否为：windows
			memRate = getMemoryRateForWindows();// 查询windows系统的cpu占用率
		} else {
			memRate = getMemoryRateForLinux();// 查询linux系统的cpu占用率
		}
		return memRate;
	}

	// 获取JVM 线程数

	public static int getThreadCount() {
		int threadCount = 0;
		if (isWindowsOrLinux.equals("windows")) { // 判断操作系统类型是否为：windows
			threadCount = getThreadCountForWindows();// 查询windows系统的线程数
		} else {
			threadCount = getThreadCountForLinux();// 查询linux系统的线程数
		}
		return threadCount;
	}

	// 获取磁盘读写速率（MB/s）

	// public String getDiskAccess() { // TODO:待完成
	// String diskAccess = "";
	// if (isWindowsOrLinux.equals("windows")) { // 判断操作系统类型是否为：windows
	// // diskAccess = getDiskAccessForWindows(); // 查询windows系统的磁盘读写速率
	// } else {
	// diskAccess = getDiskAccessForLinux(); // 查询linux系统的磁盘读写速率
	// }
	// return diskAccess;
	// }

	// 获取网口吞吐量（MB/s）

	public String getNetworkThroughput() {
		String throughput = "";
		if (isWindowsOrLinux.equals("windows")) { // 判断操作系统类型是否为：windows
			throughput = getNetworkThroughputForWindows(); // 查询windows系统的磁盘读写速率
		} else {
			throughput = getNetworkThroughputForLinux(); // 查询linux系统的磁盘读写速率
		}
		return throughput;
	}

	// 获取Windows环境下网口的上下行速率

	public String getNetworkThroughputForWindows() {
		Process pro1 = null;
		Process pro2 = null;
		Runtime r = Runtime.getRuntime();
		BufferedReader input = null;
		String rxPercent = "";
		String txPercent = "";
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		try {
			String command = "netstat -e";
			pro1 = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String result1[] = readInLine(input, "windows");
			Thread.sleep(SLEEP_TIME);
			pro2 = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			String result2[] = readInLine(input, "windows");
			rxPercent = formatNumber((Long.parseLong(result2[0]) - Long.parseLong(result1[0]))
					/ (float) (1024 * 1024 * (SLEEP_TIME / 1000))); // 上行速率(MB/s)
			txPercent = formatNumber((Long.parseLong(result2[1]) - Long.parseLong(result1[1]))
					/ (float) (1024 * 1024 * (SLEEP_TIME / 1000))); // 下行速率(MB/s)
			input.close();
			pro1.destroy();
			pro2.destroy();
		} catch (Exception e) {
			log.error("");
		}
		jsonObject.put("rxPercent", rxPercent); // 下行速率
		jsonObject.put("txPercent", txPercent); // 上行速率
		return JsonUtil.toJSONString(jsonObject);
	}

	// 获取Linux环境下网口的上下行速率

	public String getNetworkThroughputForLinux() {
		Process pro1 = null;
		Process pro2 = null;
		Runtime r = Runtime.getRuntime();
		BufferedReader input = null;
		String rxPercent = "";
		String txPercent = "";
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		try {
			String command = "watch ifconfig";
			pro1 = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));

			String result1[] = readInLine(input, "linux");
			Thread.sleep(SLEEP_TIME);
			pro2 = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			String result2[] = readInLine(input, "linux");
			rxPercent = formatNumber((Long.parseLong(result2[0]) - Long.parseLong(result1[0]))
					/ (float) (1024 * 1024 * (SLEEP_TIME / 1000))); // 下行速率(MB/s)
			txPercent = formatNumber((Long.parseLong(result2[1]) - Long.parseLong(result1[1]))
					/ (float) (1024 * 1024 * (SLEEP_TIME / 1000))); // 上行速率(MB/s)
			input.close();
			pro1.destroy();
			pro2.destroy();
		} catch (Exception e) {
			log.error("");
		}
		jsonObject.put("rxPercent", rxPercent); // 下行速率
		jsonObject.put("txPercent", txPercent); // 上行速率
		return JsonUtil.toJSONString(jsonObject);
	}

	// 获取linux环境下JVM的cpu占用率

	public static String getCPURateForLinux() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		String user = "";
		String linuxVersion = System.getProperty("os.version");
		try {
			System.out.println("Linux版本: " + linuxVersion);

			Process process = Runtime.getRuntime().exec(new String[] { "sh", "-c", "top -b -p " + pid });
			try {
				// top命令默认3秒动态更新结果信息，让线程睡眠5秒以便获取最新结果
				Thread.sleep(CPUTIME);
				is = process.getInputStream();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);

			if (linuxVersion.equals("2.4")) {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();

				System.out.println(user + " , " + system + " , " + nice);

				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));

				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();
				return String.valueOf((userUsage + systemUsage + niceUsage) / 100);
			} else {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				String userUsage = tokenStat.nextToken(); // 用户空间占用CPU百分比
				user = userUsage.substring(0, userUsage.indexOf("%"));
				process.destroy();
			}

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			freeResource(is, isr, brStat);
			return "100";
		} finally {
			freeResource(is, isr, brStat);
		}
		return user; // jvm cpu占用率
	}

	// 获取Linux环境下JVM的内存占用率

	public String getMemoryRateForLinux() {
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		String remCount = "";
		try {
			String command = "top -b -n 1 -H -p" + pid;
			pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			StringTokenizer ts = new StringTokenizer(in.readLine());
			int i = 1;
			while (ts.hasMoreTokens()) {
				i++;
				ts.nextToken();
				if (i == 10) {
					remCount = ts.nextToken();
				}
			}
			in.close();
			pro.destroy();
		} catch (Exception e) {
			log.error("");
		}
		return remCount;
	}

	// 获取windows环境下jvm的内存占用率

	public String getMemoryRateForWindows() {
		String command = "TASKLIST /NH /FO CSV /FI \"PID EQ " + pid + " \"";
		String remCount = ""; // jvm物理内存占用量
		BufferedReader in = null;
		String result = "";
		try {
			Process pro = Runtime.getRuntime().exec(command);
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			StringTokenizer ts = new StringTokenizer(in.readLine(), "\"");
			int i = 1;
			while (ts.hasMoreTokens()) {
				i++;
				ts.nextToken();
				if (i == 9) {
					remCount = ts.nextToken().replace(",", "").replace("K", "").trim();
				}
			}
			long physicalJvmMem = Long.parseLong(remCount) / 1024; // jvm物理内存占用量(MB)
			OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
			long physicalTotal = osmxb.getTotalPhysicalMemorySize() / (1024 * 1024); // 获取服务器总物理内存(MB)
			result = formatNumber(physicalJvmMem / (float) physicalTotal);
			if (Float.parseFloat(result) > 1) { // 占用率最多只能是100%
				result = "1";
			} else if (Float.parseFloat(result) < 0) {
				result = "0";
			}
			in.close();
			pro.destroy();
		} catch (Exception e) {
			log.error("getThreadCountForWindows()报异常：" + e);
		}
		return String.valueOf((Float.parseFloat(result) * 100));
	}

	// 获取linux磁盘读写速率

	// public String getDiskAccessForLinux() {
	// Process pro = null;
	// Runtime r = Runtime.getRuntime();
	// String command = "time dd if=/dev/zero of=/temp.log bs=1024k count=1000";
	// // 读取和写入1G数据
	// BufferedReader in = null;
	// float result = 0.0f;
	// try {
	// pro = r.exec(new String[] { "sh", "-c", command });
	// // pro.getInputStream()取不到结果内容，反而是getErrorStream()可以取到值
	// in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
	// in.readLine();
	// in.readLine();
	// if (getLocale().indexOf("zh") != -1) { // 中文语言环境
	// StringTokenizer ts = new StringTokenizer(in.readLine(), "，");
	// ts.nextToken();
	// ts.nextToken();
	// result = Float.parseFloat(ts.nextToken().split(" ")[0]);
	// System.out.println("中文环境");
	// } else { // 英文语言环境
	// StringTokenizer ts = new StringTokenizer(in.readLine());
	// ts.nextToken();
	// ts.nextToken();
	// ts.nextToken();
	// ts.nextToken();
	// ts.nextToken();
	// ts.nextToken();
	// ts.nextToken();
	// result = Float.parseFloat(ts.nextToken().split(" ")[0]);
	// System.out.println("英文环境");
	// }
	// r.exec("rm -f /temp.log"); // 将生成的文件删掉
	// in.close();
	// pro.destroy();
	// } catch (IOException e) {
	// System.out.println(e.getMessage());
	// }
	// return String.valueOf(result);
	// }

	// 获取Linux服务器的语言环境

	public String getLocale() {
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		String command = "locale";
		BufferedReader in = null;
		StringTokenizer ts = null;
		try {
			pro = r.exec(command);
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			ts = new StringTokenizer(in.readLine());
			in.close();
			pro.destroy();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ts.nextToken();
	}

	// 获取Linux环境下JVM的线程数

	public static int getThreadCountForLinux() {
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		String command = "top -b -n 1 -H -p " + pid;
		BufferedReader in = null;
		int result = 0;
		try {
			pro = r.exec(new String[] { "sh", "-c", command });
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			in.readLine();
			StringTokenizer ts = new StringTokenizer(in.readLine());
			ts.nextToken();
			result = Integer.parseInt(ts.nextToken());
			in.close();
			pro.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	// 获取Windows环境下JVM的线程数

	public static int getThreadCountForWindows() {
		String command = "wmic process " + pid + " list brief";
		int count = 0;
		BufferedReader in = null;
		try {
			Process pro = Runtime.getRuntime().exec(command);
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			// testGetInput(in);
			in.readLine();
			in.readLine();
			StringTokenizer ts = new StringTokenizer(in.readLine());
			int i = 1;

			while (ts.hasMoreTokens()) {
				i++;
				ts.nextToken();
				if (i == 5) {
					count = Integer.parseInt(ts.nextToken());
				}
			}
			in.close();
			pro.destroy();
		} catch (Exception e) {
			log.error("getThreadCountForWindows()报异常：" + e);
		}
		return count;
	}

	private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {
		try {
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	// 获取网口上下行速率

	public String[] readInLine(BufferedReader input, String osType) {
		String rxResult = "";
		String txResult = "";
		StringTokenizer tokenStat = null;
		try {
			if (osType.equals("linux")) { // 获取linux环境下的网口上下行速率
				String result[] = input.readLine().split(" ");
				int j = 0, k = 0;
				for (int i = 0; i < result.length; i++) {
					if (result[i].indexOf("RX") != -1) {
						j++;
						if (j == 2) {
							rxResult = result[i + 1].split(":")[1];
						}
					}
					if (result[i].indexOf("TX") != -1) {
						k++;
						if (k == 2) {
							txResult = result[i + 1].split(":")[1];
							break;
						}
					}
				}

			} else { // 获取windows环境下的网口上下行速率
				input.readLine();
				input.readLine();
				input.readLine();
				input.readLine();
				tokenStat = new StringTokenizer(input.readLine());
				tokenStat.nextToken();
				rxResult = tokenStat.nextToken();
				txResult = tokenStat.nextToken();
			}
		} catch (Exception e) {
			log.error("");
		}
		String arr[] = { rxResult, txResult };
		return arr;
	}

	// 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：

	private String substring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		String tgt = "";
		for (int i = start_idx; i <= end_idx; i++) {
			tgt += (char) b[i];
		}
		return tgt;
	}

	// 格式化浮点数(float 和 double)，保留两位小数

	private String formatNumber(Object obj) {
		String result = "";
		if (obj.getClass().getSimpleName().equals("Float")) {
			result = new Formatter().format("%.2f", (float) obj).toString();
		} else if (obj.getClass().getSimpleName().equals("Double")) {
			result = new Formatter().format("%.2f", (double) obj).toString();
		}
		return result;
	}

	// 测试方法 ：监测java执行相关命令后是否能获取到结果集(注：此方法执行后会中断程序的执行，测试完后请注释掉)

	public void testGetInput(BufferedReader in) {
		int y = 0;
		try {
			while ((y = in.read()) != -1) {
				System.out.print((char) y);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	class SysInfoAcquirerTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				System.out.println("任务开始：");
				long startTime = System.currentTimeMillis();
				int threadCount = getThreadCount();
				String cpuRate = getCPURate(); // CPU使用率
				String memoryRate = getMemoryRate(); // 内存占用率
				Map<String, Object> jsonObj = JsonUtil.parse(getNetworkThroughput(), Map.class);

				String upSpeed = (String) jsonObj.get("txPercent");// 上行速度
				String downSpeed = (String) jsonObj.get("rxPercent"); // 下行速度
				System.out.println("JVM PID：" + pid);
				System.out.println("JVM 线程数：" + threadCount);
				System.out.println("内存占用率：" + memoryRate + "%");
				System.out.println("CPU使用率：" + cpuRate + "%");
				System.out.println("上行速度：" + upSpeed + "MB/s 下行速度：" + downSpeed + "MB/s");

				long endTime = System.currentTimeMillis();
				System.out.println("任务总耗时：" + (endTime - startTime) / (1000 * 60) + "分钟");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}

	}

	@PreDestroy
	public void destroy() {
		log.info("do nothing in @PreDestroy method");
	}
}