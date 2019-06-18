package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class IpUtil {
 
	private IpUtil() {

	}

	private static final String unknown = "unknown";

	public static boolean getIpStatus(String ip) {
		log.debug("getPingStatus start");
		BufferedReader bufferedReader = null;
		try {
			List<String> commend = new java.util.ArrayList<String>();
			commend.add("ping");
			commend.add(ip);
			if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0)

				commend.add("-n");
			else
				commend.add("-c");
			commend.add("3");
			commend.add("-w");
			commend.add("5");
			String pingCommand = commend.toString().replace("[", " ").replace(",", " ").replace("]", " ");
			log.debug(pingCommand);

			bufferedReader = new BufferedReader(
					new InputStreamReader(Runtime.getRuntime().exec(pingCommand).getInputStream()));
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				if (line.matches(".*\\s+TTL=\\d+.*")) {
					bufferedReader.close();
					return true;
				}
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return false;
	}

	public static boolean getPortStatus(String ip, int port) {
		log.debug("getPortStatus start");
		Socket socket = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port));
			return true;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}

		return false;
	}

	public static boolean getUrlStatus(String url) {
		URLConnection connection = null;
		try {
			URL u = new URL(url);
			connection = u.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			return true;
		} catch (Exception e1) {
			log.error("", e1);
		}
		return false;
	}

	// 获取客户端ip地址(可以穿透代理)

	public static String getIpAddr(HttpServletRequest request) {
		log.debug("getIpAddr start");
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}

		if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();// 没有正反向代理
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {// 因为有些有些登录是通过代理，所以取第一个（第一个为真是ip）
			ip = ip.split(",")[0];
		}
		return ip;
	}

	// 通过IP获取地址(需要联网，调用淘宝的IP库)
	public static String getIpInfo(String ip) {
		log.debug("getIpInfo start");
		// 如果输入的是域名，先转换为ip
		HttpURLConnection httpURLConnection = null;
		BufferedReader responseReader = null;
		try {
			URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			StringBuilder temp = new StringBuilder();
			for (String readLine = null; (readLine = responseReader.readLine()) != null;) {
				temp.append(readLine).append("\n");
			}
			responseReader.close();
			String result = temp.toString();
			httpURLConnection.disconnect();
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> map = JsonUtil.parse(result, Map.class);
			if ((int) map.get("code") == 0) {
				Map<String, Object> data = (Map<String, Object>) map.get("data");
				responseMap.put("status", true);
				responseMap.put("country", data.get("country"));
				responseMap.put("region", data.get("region"));
				responseMap.put("city", data.get("city"));
				responseMap.put("isp", data.get("isp"));
			} else {
				responseMap.put("status", false);
			}
			return JsonUtil.toJSONString(responseMap);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			if (responseReader != null) {
				try {
					responseReader.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return null;
	}

	private static String localIP = null;

	public static String getLocalIp() throws Exception {
		;
		if (localIP != null)
			return localIP;
		else
			return getLocalHostLANAddress().getHostAddress();
	}

	public static InetAddress getLocalHostLANAddress() throws Exception {

		InetAddress candidateAddress = null;
		// 遍历所有的网络接口
		for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
			NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
			// 在所有的接口下再遍历IP
			for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
				InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
				if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
					if (inetAddr.isSiteLocalAddress()) {
						// 如果是site-local地址，就是它了
						return inetAddr;
					} else if (candidateAddress == null) {
						// site-local类型的地址未被发现，先记录候选地址
						candidateAddress = inetAddr;
					}
				}
			}
		}
		if (candidateAddress != null) {
			return candidateAddress;
		}
		// 如果没有发现 non-loopback地址.只能用最次选的方案
		return InetAddress.getLocalHost();

	}

}
