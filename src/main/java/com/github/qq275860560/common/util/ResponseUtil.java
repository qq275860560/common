package com.github.qq275860560.common.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
	public static void sendFile(HttpServletResponse response, File file, String responseContentType,String responseContentDisposition) throws Exception {
		byte[] byteArray = FileUtils.readFileToByteArray(file);
		String fileName = file.getName(); // 解决中文文件名下载后乱码的问题
		sendFileByteArray(response, byteArray, fileName, responseContentType,responseContentDisposition);
	}

	

	//内存中的文件字节数组发送到客户端
	public static void sendFileByteArray(HttpServletResponse response, byte[] byteArray, String fileName,
			String responseContentType,String responseContentDisposition) throws Exception {
		// response.setCharacterEncoding("utf-8");
		// "application/vnd.ms-excel;charset=utf-8"
		// "application/octet-stream;charset=UTF-8"
		if(responseContentType!=null) { 
			response.setContentType(responseContentType);		
		}else {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
		if(responseContentDisposition!=null) {
		response.addHeader("Content-Disposition", responseContentDisposition);
		}else {
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		}
		response.addHeader("Content-Length", "" + byteArray.length);
		response.addHeader("pargam", "no-cache");
		response.getOutputStream().write(byteArray);
		response.getOutputStream().flush();
	}
	
	public static String  getContentDispositionInline(String filename) throws Exception {
		 return "inline;filename=" + URLEncoder.encode(filename, "UTF-8");
	}
	public static String  getContentDispositionAttachment(String filename) throws Exception {
		 return "attachment;filename=" + URLEncoder.encode(filename, "UTF-8");
	}
	public static String  getContentType(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		String mimetype="application/octet-stream";
		if(extension.equals("png")) mimetype="image/png" ;
	    else if(extension.equals("gif")) mimetype="image/gif" ;
	    else if(extension.equals("jpg")) mimetype="image/jpeg" ;
	    else if(extension.equals("jpeg")) mimetype="image/jpeg" ;
	    else if(extension.equals("bmp")) mimetype="image/bmp" ;
	    else if(extension.equals("mp2")) mimetype="audio/x-mpeg" ;
	    else if(extension.equals("mp3")) mimetype="audio/mp3" ;
	    else if(extension.equals("wav")) mimetype="audio/wav" ;
	    else if(extension.equals("ogg")) mimetype="audio/x-ogg" ;
	    else if(extension.equals("mid")) mimetype="audio/mid" ;
	    else if(extension.equals("midi")) mimetype="audio/midi" ;
	    else if(extension.equals("m3u")) mimetype="audio/x-mpegurl" ;
	    else if(extension.equals("m4a")) mimetype="audio/mp4a-latm" ;
	    else if(extension.equals("m4b")) mimetype="audio/mp4a-latm" ;
	    else if(extension.equals("m4p")) mimetype="audio/mp4a-latm" ;
	    else if(extension.equals("mpga")) mimetype="audio/mpeg" ;
	    else if(extension.equals("wma")) mimetype="audio/x-ms-wma" ;
	    else if(extension.equals("mpe")) mimetype="video/mpeg" ;
	    else if(extension.equals("mpg")) mimetype="video/mpeg" ;
	    else if(extension.equals("mpeg")) mimetype="video/mpeg" ;
	    else if(extension.equals("3gp")) mimetype="video/3gpp" ;
	    else if(extension.equals("asf")) mimetype="video/x-ms-asf" ;
	    else if(extension.equals("avi")) mimetype="video/x-msvideo" ;
	    else if(extension.equals("m4u")) mimetype="video/vnd.mpegurl" ;
	    else if(extension.equals("m4v")) mimetype="video/x-m4v" ;
	    else if(extension.equals("mov")) mimetype="video/quicktime" ;
	    else if(extension.equals("mp4")) mimetype="video/mp4" ;
	    else if(extension.equals("rmvb")) mimetype="video/*" ;
	    else if(extension.equals("wmv")) mimetype="video/*" ;
	    else if(extension.equals("vob")) mimetype="video/*" ;
	    else if(extension.equals("mkv")) mimetype="video/*" ;
	    else if(extension.equals("jar")) mimetype="application/java-archive" ;
	    else if(extension.equals("zip")) mimetype="application/zip" ;
	    else if(extension.equals("rar")) mimetype="application/x-rar-compressed" ;
	    else if(extension.equals("gz")) mimetype="application/gzip" ;
	    else if(extension.equals("gtar")) mimetype="application/x-gtar" ;
	    else if(extension.equals("tar")) mimetype="application/x-tar" ;
	    else if(extension.equals("tgz")) mimetype="application/x-compressed" ;
	    else if(extension.equals("z")) mimetype="application/x-compressed" ;
	    else if(extension.equals("htm")) mimetype="text/html" ;
	    else if(extension.equals("html")) mimetype="text/html" ;
	    else if(extension.equals("php")) mimetype="text/php " ;
	    else if(extension.equals("txt")) mimetype="text/plain" ;
	    else if(extension.equals("c")) mimetype="text/plain" ;
	    else if(extension.equals("conf")) mimetype="text/plain" ;
	    else if(extension.equals("cpp")) mimetype="text/plain" ;
	    else if(extension.equals("h")) mimetype="text/plain" ;
	    else if(extension.equals("java")) mimetype="text/plain" ;
	    else if(extension.equals("log")) mimetype="text/plain" ;
	    else if(extension.equals("prop")) mimetype="text/plain" ;
	    else if(extension.equals("rc")) mimetype="text/plain" ;
	    else if(extension.equals("sh")) mimetype="text/plain" ;
	    else if(extension.equals("csv")) mimetype="text/csv" ;
	    else if(extension.equals("xml")) mimetype="text/xml" ;
	    else if(extension.equals("apk")) mimetype="application/vnd.android.package-archive" ;
	    else if(extension.equals("bin")) mimetype="application/octet-stream" ;
	    else if(extension.equals("class")) mimetype="application/octet-stream" ;
	    else if(extension.equals("exe")) mimetype="application/octet-stream" ;
	    else if(extension.equals("mpc")) mimetype="application/vnd.mpohun.certificate" ;
	    else if(extension.equals("msg")) mimetype="application/vnd.ms-outlook" ;
	    else if(extension.equals("doc")) mimetype="application/msword" ;
	    else if(extension.equals("docx")) mimetype="application/msword" ;
	    else if(extension.equals("js")) mimetype="application/x-javascript" ;
	    else if(extension.equals("pdf")) mimetype="application/pdf" ;
	    else if(extension.equals("pps")) mimetype="application/vnd.ms-powerpoint" ;
	    else if(extension.equals("ppt")) mimetype="application/vnd.ms-powerpoint" ;
	    else if(extension.equals("pptx")) mimetype="application/vnd.ms-powerpoint" ;
	    else if(extension.equals("wps")) mimetype="application/vnd.ms-works" ;
	    else if(extension.equals("rtf")) mimetype="application/rtf" ;
	    else if(extension.equals("xls")) mimetype="application/vnd.ms-excel" ;
	    else if(extension.equals("xlsx")) mimetype="application/vnd.ms-excel" ; 
	    else mimetype="application/octet-stream";		 
		return mimetype;
	}

}
