package com.github.qq275860560.common.util;

import java.io.ByteArrayInputStream;
import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class HttpServerUtilTest {
	 	

	@Test
	public void get() throws Exception {
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1"))
				.add(new RequestExpectContinue(true)).build();
		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
		HttpCoreContext coreContext = HttpCoreContext.create();
		if(IpUtil.getPortStatus("localhost", 8080)==false)return;
		HttpHost host = new HttpHost("localhost", 8080);
		coreContext.setTargetHost(host);
		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
		try {
			
			String[] targets = { "/", "com/github/common/util/HttpServerUtil.class",
					URLEncodeUtil.encode("com/github/common/util/HttpServerUtil.class") };

			for (int i = 0; i < targets.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				}
				BasicHttpRequest request = new BasicHttpRequest("GET", targets[i]);
				log.info(">> Request URI: " + request.getRequestLine().getUri());
				httpexecutor.preProcess(request, httpproc, coreContext);
				HttpResponse response = httpexecutor.execute(request, conn, coreContext);
				httpexecutor.postProcess(response, httpproc, coreContext);
				log.info("<< Response: " + response.getStatusLine());
				log.info(EntityUtils.toString(response.getEntity()));
				log.info("==============");
				if (!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					log.info("Connection kept alive...");
				}
			}
		} finally {
			conn.close();
		}

	}

	@Test
	public void post() throws Exception {
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1"))
				.add(new RequestExpectContinue(true)).build();

		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpCoreContext coreContext = HttpCoreContext.create();
		if(IpUtil.getPortStatus("localhost", 8080)==false)return;
		HttpHost host = new HttpHost("localhost", 8080);
		coreContext.setTargetHost(host);

		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

		try {

			HttpEntity[] requestBodies = {
					new StringEntity("This is the first test request", ContentType.create("text/plain", Consts.UTF_8)),
					new ByteArrayEntity("This is the second test request".getBytes("UTF-8"),
							ContentType.APPLICATION_OCTET_STREAM),
					new InputStreamEntity(
							new ByteArrayInputStream(
									"This is the third test request (will be chunked)".getBytes("UTF-8")),
							ContentType.APPLICATION_OCTET_STREAM) };

			for (int i = 0; i < requestBodies.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				}
				BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
						"com/github/common/util/HttpServerUtil.class");
				request.setEntity(requestBodies[i]);
				log.info(">> Request URI: " + request.getRequestLine().getUri());

				httpexecutor.preProcess(request, httpproc, coreContext);
				HttpResponse response = httpexecutor.execute(request, conn, coreContext);
				httpexecutor.postProcess(response, httpproc, coreContext);

				log.info("<< Response: " + response.getStatusLine());
				log.info(EntityUtils.toString(response.getEntity()));
				log.info("==============");
				if (!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					log.info("Connection kept alive...");
				}
			}
		} finally {
			conn.close();
		}

	}
}
