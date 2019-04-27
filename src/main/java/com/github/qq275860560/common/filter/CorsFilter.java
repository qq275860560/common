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

import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangyuanlin@163.com
 */
public class CorsFilter implements Filter {


	@Override
	public void init(FilterConfig config) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		/**********跨域开始**********/
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
		response.setHeader("Access-Control-Allow-Headers", "*,Content-Type,token,Authorization");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,downloadFileName,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(200);
			return;
		}
		/**********跨域开始**********/
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		return;
	}
}
