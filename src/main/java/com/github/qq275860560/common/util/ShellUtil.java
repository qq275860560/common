package com.github.qq275860560.common.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author jiangyuanlin@163.com
 */
public class ShellUtil {
	private static Log log = LogFactory.getLog(ShellUtil.class);

	private ShellUtil() {
	}

	 
	public static Map<String, Object> execute(String ip, Long port, String username, String password, String shell) {
		InputStream stdOut = null;
		InputStream stdErr = null;
		Connection connection = null;
		Session session = null;

		int ret = -1;
		String outStr = null;
		String outErr = null;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			connection = new Connection(ip, Integer.parseInt(port.toString()));
			connection.connect();
			connection.authenticateWithPassword(username, password);
			session = connection.openSession();

			session.execCommand(shell);

			stdOut = new StreamGobbler(session.getStdout());
			outStr = processStream(stdOut);

			stdErr = new StreamGobbler(session.getStderr());
			outErr = processStream(stdErr);

			int TIME_OUT = 1000 * 5 * 60;
			session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

			ret = session.getExitStatus();

			if (ret == 0) {
				resultMap.put("code", 0);
				resultMap.put("msg", resultMap.get("msg") + "\n" + outStr);
			} else {
				resultMap.put("code", -1);
				resultMap.put("msg", resultMap.get("msg") + "\n" + outErr);
			}

			session.close();
			connection.close();
		} catch (Exception e) {
			log.error("", e);

		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			if (stdOut != null) {
				try {
					stdOut.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (stdErr != null) {
				try {
					stdErr.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}

		return resultMap;
	}

	private static String processStream(InputStream in) throws Exception {
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();
		while (in != null && in.read(buf) != -1) {
			sb.append(new String(buf, "UTF-8"));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String ip = "192.168.137.63";
		long port = 22;
		String username = "root";
		String password = "123456";
		String command = " cd /tmp && ls";
		System.out.println(execute(ip, port, username, password, command));

	}

}
