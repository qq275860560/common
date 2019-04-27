package com.github.qq275860560.common.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author jiangyuanlin@163.com
 */
public class RestTemplateUtil {
	private static Log log = LogFactory.getLog(RestTemplateUtil.class);

	private RestTemplateUtil() {
	}

	private static Map<String, RestTemplate> restTemplates = new HashMap<>();

	public static synchronized RestTemplate build(ClientHttpRequestInterceptor... interceptors) {
		return build("default", interceptors);
	}

	public static synchronized RestTemplate build(String name, ClientHttpRequestInterceptor... interceptors) {

		if (restTemplates.containsKey(name))
			return restTemplates.get(name);

		SimpleClientHttpRequestFactory factory = new MyHttpRequestFactory();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(5000);

		RestTemplate restTemplate = new RestTemplate(factory);
		List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
		// interceptorList.add( );
		if (interceptors != null && interceptors.length > 0) {
			for (ClientHttpRequestInterceptor interceptor : interceptors) {
				interceptorList.add(interceptor);
			}
		}
		restTemplate.setInterceptors(interceptorList);
		//
		// restTemplate.setErrorHandler(new CustomResponseErrorHandler());
		//
		restTemplates.put(name, restTemplate);

		return restTemplate;
	}

	private static class MyHttpRequestFactory extends SimpleClientHttpRequestFactory {

		@Override
		public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
			uri = convertToRealUri(uri);
			return super.createRequest(uri, httpMethod);
		}

		@Override
		public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
			uri = convertToRealUri(uri);
			return super.createAsyncRequest(uri, httpMethod);
		}

		private URI convertToRealUri(URI uri) {
			try {
				String vipAddress = uri.getHost();
				String ipAndPort = EurekaUtil.getInstanceIpAndPort(vipAddress);
				uri = new URI(uri.toString().replace(vipAddress, ipAndPort));

			} catch (Exception e) {
				log.error("", e);
			}
			return uri;

		}

		public static void main(String[] args) {

			RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() {
				@Override
				public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
					uri = convertToRealUri(uri);
					return super.createRequest(uri, httpMethod);
				}

				@Override
				public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
					uri = convertToRealUri(uri);
					return super.createAsyncRequest(uri, httpMethod);
				}

				private URI convertToRealUri(URI uri) {
					try {
						String vipAddress = uri.getHost();
						String ipAndPort = EurekaUtil.getInstanceIpAndPort(vipAddress);
						uri = new URI(uri.toString().replace(vipAddress, ipAndPort));

					} catch (Exception e) {
						log.error("", e);
					}
					return uri;

				}

			});

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("name", "admin");
			requestBody.put("password", "123456");
			Map<String, Object> result = restTemplate.postForObject(
					"http://" + EurekaUtil.properties.getProperty("eureka.vipAddress") + "/api/msmng/user/login",
					requestBody, Map.class);
			log.info(result);
		}

	}

}
