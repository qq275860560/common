package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class HttpConnectionUtil {
	private static Log log = LogFactory.getLog(HttpConnectionUtil.class);

	private HttpConnectionUtil() {
	}

	 
 
	/*
	public static String get(String urlString ) {
		return HttpConnectionUtil.send(urlString, "GET", null, null);
	}
	public static String delete(String urlString ) {
		return HttpConnectionUtil.send(urlString, "DELETE", null, null);
	}
	public static String put(String urlString,  String content) {
		return HttpConnectionUtil.send(urlString, "PUT", null, content);
	}
	public static String post(String urlString,  String content) {
		return HttpConnectionUtil.send(urlString, "POST", null, content);
	} 
	
	public static String send(String urlString, String httpMethod, String content) {
		return HttpConnectionUtil.send(urlString, httpMethod, null, content);
	}
	*/
	public static String send(String urlString, String httpMethod, Map<String, String> headerMap, String content) {
		log.trace("requestBody=" + content);
		long start = System.currentTimeMillis();// 开始执行时间
		DataOutputStream dataOutputStream = null;
		BufferedReader responseReader = null;
		HttpURLConnection httpURLConnection = null;
		String result = null;
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			httpURLConnection = (HttpURLConnection) urlConnection;
			httpURLConnection.setConnectTimeout(20000);
			httpURLConnection.setReadTimeout(20000);
			// 指定流的大小，当内容达到这个值的时候就把流输出
			// httpURLConnection.setChunkedStreamingMode(20*1024);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			httpURLConnection.setRequestMethod(httpMethod);
			httpURLConnection.connect();

			if (StringUtils.isNotBlank(content)) {
				OutputStream outputStream = httpURLConnection.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				dataOutputStream.write(content.getBytes(Charset.forName("UTF-8")));
				dataOutputStream.flush();
				dataOutputStream.close();
			}
			// 获得响应状态
			int reponseCode = httpURLConnection.getResponseCode();
			log.info("reponseCode=" + reponseCode);

			Map<String, List<String>> map = urlConnection.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				log.info(entry.getKey() + "=" + entry.getValue());
			}
			StringBuilder sb = new StringBuilder();
			String readLine = null;
			responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine).append("\n");
			}
			responseReader.close();
			result = sb.toString();

			httpURLConnection.disconnect();

			log.info("请求总共用时=" + (System.currentTimeMillis() - start) + "ms,url=" + urlString + ",httpmethod="
					+ httpMethod + ",content=" + content);
			log.info(result);
			if (reponseCode >= 400) {
				log.error("请求总共用时=" + (System.currentTimeMillis() - start) + "ms,url=" + urlString + ",httpmethod="
						+ httpMethod + ",content=" + content);
				log.error("result=" + result);
			}

		} catch (Exception e) {
			log.error("请求总共用时=" + (System.currentTimeMillis() - start) + "ms,url=" + urlString + ",httpmethod="
					+ httpMethod + ",content=" + content);
			//log.error("", e);
			log.info("网络不通:"+urlString);
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
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return result;
	}
	
	
	
	public static int getResponseCode(URL url, String httpMethod, Map<String, String> headerMap, byte[] content) {
		int reponseCode = 0;
		log.trace("requestBody=" + content);
		long start = System.currentTimeMillis();// 开始执行时间
		DataOutputStream dataOutputStream = null;
		BufferedReader responseReader = null;
		HttpURLConnection httpURLConnection = null;
		String result = null;
		try {
	 
			
			URLConnection urlConnection = url.openConnection();
			httpURLConnection = (HttpURLConnection) urlConnection;
			
	 
			httpURLConnection.setFixedLengthStreamingMode((int) content.length);
			httpURLConnection.setConnectTimeout(200000);
			httpURLConnection.setReadTimeout(200000);
			// 指定流的大小，当内容达到这个值的时候就把流输出
			// httpURLConnection.setChunkedStreamingMode(20*1024);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			httpURLConnection.setRequestMethod(httpMethod);
			httpURLConnection.connect();

			if (content!=null && content.length>0) {
				OutputStream outputStream = httpURLConnection.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				dataOutputStream.write(content);
				dataOutputStream.flush();
				dataOutputStream.close();
			}
			// 获得响应状态
			 reponseCode = httpURLConnection.getResponseCode();
			log.info("reponseCode=" + reponseCode);

	 

			 

		} catch (Exception e) {			
			log.error("", e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}			 
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return reponseCode;
	}
	
	
	
	public static int getResponseCode(String urlString, String httpMethod, Map<String, String> headerMap, String content) {
		 
		DataOutputStream dataOutputStream = null;	
		HttpURLConnection httpURLConnection = null;
		int reponseCode = 0;
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			httpURLConnection = (HttpURLConnection) urlConnection;
			httpURLConnection.setConnectTimeout(200000);
			httpURLConnection.setReadTimeout(200000);
			// 指定流的大小，当内容达到这个值的时候就把流输出
			// httpURLConnection.setChunkedStreamingMode(20*1024);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			httpURLConnection.setRequestMethod(httpMethod);
			httpURLConnection.connect();

			if (StringUtils.isNotBlank(content)) {
				OutputStream outputStream = httpURLConnection.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				dataOutputStream.write(content.getBytes(Charset.forName("UTF-8")));
				dataOutputStream.flush();
				dataOutputStream.close();
			}
			// 获得响应状态
			reponseCode = httpURLConnection.getResponseCode();
			log.info("reponseCode=" + reponseCode);

			 

		} catch (Exception e) {			
			log.error("", e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}			 
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return reponseCode;
	}
	
	public static void main(String[] args) throws Exception{
		String result=send("https://video.ctccc.cn/fsapi/sipUser/getSipInfo?user="+"1011", "GET", null, null);
		System.out.println(result);
		org.json.JSONObject json = new org.json.JSONObject(result);
		org.json.JSONObject data = json.getJSONObject("data");
		String ip = data.getString("domain");
		int port = Integer.parseInt(data.getString("port"));
		String username = data.getString("user");
		String password =  data.getString("password");
		System.out.println(ip);
		System.out.println(port);
		System.out.println(username);
		System.out.println(password);
	}

}

 