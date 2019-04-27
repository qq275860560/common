package com.github.qq275860560.common.inteceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author jiangyuanlin@163.com
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
	private static Logger log = LoggerFactory.getLogger(RestTemplateInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);

		Enumeration<String> enumeration = httpServletRequest.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = httpServletRequest.getHeader(key);
			requestWrapper.getHeaders().set(key, value);
		}

		return execution.execute(requestWrapper, body);
	}

}