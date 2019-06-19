package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class CommandUtil {

	public static String runComand(String command,File file) throws Exception {
		command=command.trim();
		if(!command.startsWith("cmd") && !command.startsWith("/bin/sh") ) {
			if(System.getProperty("os.name").toLowerCase().indexOf("windows")>=0) {
				command="cmd /c \""+ command+"\"";
			}else {
				command="/bin/sh -c \""+command + "\"";
			}
		}
		StringBuilder result = new StringBuilder();
		Process progress = Runtime.getRuntime().exec(command,null,file);
		log.info(command);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(progress.getInputStream()));

		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line).append("\r\n");
		}
		bufferedReader.close();
		progress.waitFor();
		return result.toString();

	}
	
	public static String runComand(String command) throws Exception {		 
		return runComand(command,null);
	}
}
