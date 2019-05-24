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

/**
 * @author jiangyuanlin@163.com
Frame过滤器
 */
public class FrameFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		/**********Frame设置开始**********/
		response.setHeader("x-frame-options","SAMEORIGIN");
		/**********Frame设置结束**********/
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		return;
	}
}
