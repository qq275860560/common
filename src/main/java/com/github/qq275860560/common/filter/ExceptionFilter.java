package com.github.qq275860560.common.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.qq275860560.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class ExceptionFilter implements Filter {
	
	private static String environment;

	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "application.properties"));
			environment = configuration.getString("spring.profiles.active");
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error("", e);
			if ("prod".equals(environment)) {
				ResponseUtil.sendResult(response, "{\"code\":-2,\"msg\":\"请求错误\",\"data\":\"" + e.getMessage() + "\"}");
			} else {
				ResponseUtil.sendResult(response,
						"{\"code\":-2,\"msg\":\"请求错误\",\"data\":\"" + ExceptionUtils.getStackTrace(e) + "\"}");
			}

		}
	}

	@Override
	public void destroy() {
		return;
	}
}
