package com.github.qq275860560.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

import com.github.qq275860560.common.model.ApiResult;

/**
 * @author jiangyuanlin@163.com
 */
public class HttpServerUtil {
	private static Log log = LogFactory.getLog(HttpServerUtil.class);

	public static int port;
	public static String docRoot;

	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "httpserver.properties"));
			port = configuration.getInt("port");
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private HttpServerUtil() {
	}

	public static void main(String[] args) throws Exception {
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new ResponseDate())
				.add(new ResponseServer("Test/1.1")).add(new ResponseContent()).add(new ResponseConnControl()).build();

		UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
		reqistry.register("*", new HttpFileHandler());
		HttpService httpService = new HttpService(httpproc, reqistry);

		HttpConnectionFactory<DefaultBHttpServerConnection> httpConnectionFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
		ServerSocket serverSocket = new ServerSocket(port);

		log.info("Listening on port=" + serverSocket.getLocalPort());

		while (!Thread.interrupted()) {
			try {
				Socket socket = serverSocket.accept();
				HttpServerConnection httpServerConnection = httpConnectionFactory.createConnection(socket);
				ThreadPoolExecutorUtil.threadPoolExecutor.execute(new HandlerRunnable(httpService, httpServerConnection));
			} catch (InterruptedIOException ex) {
				break;
			} catch (IOException e) {
				log.error("",e);
				break;
			}
		}

	}

	static class HttpFileHandler implements HttpRequestHandler {

		public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
				throws HttpException, IOException {
			String result = null;

			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "*,Content-Type,token");

			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("POST")) {
				result = "{\"code\":\"-1\",\"msg\":\"请求失败，只支持post请求\",\"data\":\"" + "" + "\"}";
			} else {
				
				if (request.getLastHeader("Content-Type").getValue().equals("application/json;charset=UTF-8")) {

					// 分析uri
					String target = request.getRequestLine().getUri();
					if (request instanceof HttpEntityEnclosingRequest) {
						Map<String, Object> requestMap = RequestUtil.bodyToMap((HttpEntityEnclosingRequest) request);
						log.info(JsonUtil.toJSONString(requestMap));
						result = JsonUtil.toJSONString(new ApiResult(ApiResult.SUCCESS, ApiResult.SUCCESS_MSG, null));
					}
				}
			}

			response.setStatusCode(HttpStatus.SC_OK);
			StringEntity entity = new StringEntity(result, "UTF-8");
			response.setEntity(entity);

		}

	}

	 

	static class HandlerRunnable implements Runnable {

		private final HttpService httpservice;
		private final HttpServerConnection conn;

		public HandlerRunnable(final HttpService httpservice, final HttpServerConnection conn) {
			super();
			this.httpservice = httpservice;
			this.conn = conn;
		}

		@Override
		public void run() {
			HttpContext context = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.conn.isOpen()) {
					this.httpservice.handleRequest(this.conn, context);
					break;//
				}
			} catch (Exception e) {
				log.error("", e);
			} finally {
				try {
					this.conn.shutdown();
				} catch (IOException ignore) {
				}
			}
		}

	}
}
