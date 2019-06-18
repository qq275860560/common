package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class SocketUtil {

	 
	private SocketUtil() {
	}

	public static String send(String ip, int port, byte[] b) throws Exception {
		OutputStream os = null;
		BufferedReader br = null;
		Socket socket = null;
		try {
			InetSocketAddress address = new InetSocketAddress(ip, port);
			socket = new Socket();
			socket.connect(address, 30000);
			socket.setSoTimeout(30000);
			os = socket.getOutputStream();
			os.write(b);
			os.flush();
			socket.getOutputStream();
			socket.shutdownOutput();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			if (sb.length() > 0)
				sb.delete(sb.length() - 2, sb.length());
			return sb.toString();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (os != null) {
				os.close();
			}
			if (br != null) {
				br.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
		return null;
	}

}
