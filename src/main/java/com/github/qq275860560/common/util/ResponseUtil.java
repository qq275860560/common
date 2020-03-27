package com.github.qq275860560.common.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class ResponseUtil {
	 
	private ResponseUtil() {
	}

	// 发字符串发送到客户端
	public static void sendResult(HttpServletResponse response, String result) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json;charset=UTF-8");// 解决乱码
		// response.setHeader("Content-Type", "text/xml;charset=UTF-8");// 解决乱码
		PrintWriter out = response.getWriter();
		out.println(result);
		// BufferedOutputStream out = new
		// BufferedOutputStream(response.getOutputStream());
		// out.write(result.getBytes());
		out.flush();
		out.close();
	}

	// 文件发送到客户端
	public static void sendFile(HttpServletResponse response, File file, String responseContentType) throws Exception {
		byte[] byteArray = FileUtils.readFileToByteArray(file);
		String fileName = URLEncoder.encode(file.getName(), "utf-8"); // 解决中文文件名下载后乱码的问题
		sendFileByteArray(response, byteArray, fileName, responseContentType);
	}

	

	//内存中的文件字节数组发送到客户端
	public static void sendFileByteArray(HttpServletResponse response, byte[] byteArray, String fileName,
			String responseContentType) throws Exception {
		response.setCharacterEncoding("utf-8");
		// "application/vnd.ms-excel;charset=utf-8"
		// "application/octet-stream;charset=UTF-8"
		if(responseContentType!=null) { 
			response.setContentType(responseContentType);		
		}else {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		response.addHeader("Content-Length", "" + byteArray.length);
		response.addHeader("pargam", "no-cache");
		response.getOutputStream().write(byteArray);
		response.getOutputStream().flush();
	}

}
