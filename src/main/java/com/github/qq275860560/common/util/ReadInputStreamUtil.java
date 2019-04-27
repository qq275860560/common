package com.github.qq275860560.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author jiangyuanlin@163.com
 */
public class ReadInputStreamUtil {

	public static String read(InputStream inputStream, String suffix) {
		try {
			char lastChar = suffix.charAt(suffix.length() - 1);
			StringBuffer sb = new StringBuffer();
			char c = (char) inputStream.read();
			while (true) {				
				sb.append(c);
				if (c == lastChar && sb.toString().endsWith(suffix)) {
					return sb.toString();					 
				}
				c = (char) inputStream.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		/*
		TelnetClient telnetClient = new TelnetClient();
		telnetClient.connect("132.122.237.68", 6379);		
		InputStream inputStream = telnetClient.getInputStream();
		OutputStream outputStream = telnetClient.getOutputStream();
		*/
		
		Socket socket = new Socket("132.122.237.68", 6379);		
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();

		 
		outputStream.write("auth 123456\r\n".getBytes());
		outputStream.flush();
		System.out.println(read(inputStream, "\r\n"));
 
		for (int i = 0; i < 10; i++) {
			outputStream.write(("set foo" + i + " bar\r\n").getBytes());
			outputStream.flush();
		    System.out.println("set返回结果="+read(inputStream, "\r\n"));
		  
		    outputStream.write(("get foo" + i + "\n").getBytes());
			outputStream.flush();
			System.out.println("get返回字节数="+read(inputStream, "\r\n"));
			System.out.println("get返回结果="+read(inputStream, "\r\n"));		    
		}
		outputStream.close();
		inputStream.close();

	}

}