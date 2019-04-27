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

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

/**
 * @author jiangyuanlin@163.com
 */
public class CatFilter implements Filter {
	private static final Log log = LogFactory.getLog(CatFilter.class);
	private ThreadLocal<Transaction> tranLocal = new ThreadLocal<Transaction>();
	private ThreadLocal<Transaction> pageLocal = new ThreadLocal<Transaction>();

	@Override
	public void init(FilterConfig config) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;


		String uri = request.getRequestURI();
		Transaction t = Cat.newTransaction("URL", uri);
		Cat.logEvent("URL.Method", request.getMethod(), Message.SUCCESS, request.getRequestURL().toString());
		Cat.logEvent("URL.Host", request.getMethod(), Message.SUCCESS, request.getRemoteHost());
		tranLocal.set(t);
		
		
		String viewName =  "无";
		t = Cat.newTransaction("View", viewName);
		pageLocal.set(t);
		pageLocal.set(t);
		
		chain.doFilter(request, response);

		Transaction pt = pageLocal.get();
		pt.setStatus(Transaction.SUCCESS);
		pt.complete();


		// 总计
		t = tranLocal.get();
		t.setStatus(Transaction.SUCCESS);
		t.complete();
		
	}

	@Override
	public void destroy() {
		return;
	}
}
