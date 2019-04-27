package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class ResponseUtil {
	private static Log log = LogFactory.getLog(ResponseUtil.class);

	private ResponseUtil() {
	}

	/** 把字符串发回给客户端
	 * @param response
	 * @param result
	 * @throws IOException
	 */
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

	/** 把文件流发回给客户端
	 * @param response
	 * @param result
	 * @throws IOException
	 */
	public static void sendResult(HttpServletResponse response, File file,String contentType) throws IOException {
		FileInputStream fis = null;
		ServletOutputStream out = null;
		try {
			fis = new FileInputStream(file);

			String filename = URLEncoder.encode(file.getName(), "utf-8"); // 解决中文文件名下载后乱码的问题
			byte[] b = new byte[fis.available()];
			int count = fis.read(b);
			log.info("字节总数:" + count);
			response.setCharacterEncoding("utf-8");
			if(contentType!=null){
				response.setContentType(contentType);
			}else{//字节流默认为excel文件
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				//"application/octet-stream;charset=UTF-8"
			}
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(filename.getBytes(), "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			response.addHeader("pargam", "no-cache");

			// 获取响应报文输出流对象
			out = response.getOutputStream();
			// 输出
			out.write(b);
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
		}

	}

}
