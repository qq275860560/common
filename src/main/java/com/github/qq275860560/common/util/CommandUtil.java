package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class CommandUtil {

	public static String runComand(String command) throws Exception {
		StringBuilder result = new StringBuilder();
		Process progress = Runtime.getRuntime().exec(command);
		log.info(command);
		BufferedReader in = new BufferedReader(new InputStreamReader(progress.getInputStream()));

		String line = null;
		while ((line = in.readLine()) != null) {
			result.append(line).append("\r\n");
		}
		in.close();
		progress.waitFor();
		return result.toString();

	}
}
