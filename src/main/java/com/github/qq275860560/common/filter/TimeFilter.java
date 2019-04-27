package com.github.qq275860560.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.qq275860560.common.util.RequestUtil;

/**
 * @author jiangyuanlin@163.com
 */
public class TimeFilter implements Filter {
	private static final Log log = LogFactory.getLog(TimeFilter.class);

	@Override
	public void init(FilterConfig config) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		long end = System.currentTimeMillis();
		log.info("请求总共用时=" + (end - start) + "ms,url=" + request.getRequestURL() + ",parameter="
				+ RequestUtil.parameterToMap(request));
	}

	@Override
	public void destroy() {
		return;
	}
}
