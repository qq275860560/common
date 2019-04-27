package com.github.qq275860560.common.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.bull.javamelody.internal.common.LOG;

/**
 * @author jiangyuanlin@163.com
 * json输入输出参数拦截
 */
public class JsonParameterFilter implements Filter {
	private static final Log log = LogFactory.getLog(LoginFilter.class);

  

	
    
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// 获得用户请求的URI
		String path = request.getRequestURI();

		String array[] = path.split("/");
		if(array[array.length-1].matches("^(get|list|page|count|check|save|update|delete)")){;
			chain.doFilter(new MyHttpServletRequestWrapper((HttpServletRequest) request), response);
		}else{
			chain.doFilter(req, res);
		}
		
 
		

	}

	@Override
	public void destroy() {
		return;
	}
	
	
	
}




  class MyHttpServletRequestWrapper  extends HttpServletRequestWrapper {
	  private static final Log log = LogFactory.getLog(MyHttpServletRequestWrapper.class);
	  private String requestBody = null;
	  
		public MyHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);
			if (requestBody == null) {
				requestBody = readBody(request);
			}
			log.info("requestBody="+requestBody);
			;
		}
	 
		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}
	 
		@Override
		public ServletInputStream getInputStream() throws IOException {
			return new CustomServletInputStream(requestBody);
		}

    private static String readBody(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String inputLine;
		BufferedReader br = null;
		try {
			br = request.getReader();
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read body.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();

    }

     class CustomServletInputStream extends ServletInputStream {
    	private ByteArrayInputStream buffer;
    	 
		public CustomServletInputStream(String body) {
			body = body == null ? "" : body;
			this.buffer = new ByteArrayInputStream(body.getBytes());
		}

        @Override
        public int read() throws IOException {
        	return buffer.read();
        }

		@Override
		public boolean isFinished() {
			return buffer.available() == 0;
		}

		@Override
		public boolean isReady() {
			return true;

		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new RuntimeException("Not implemented");
			
		}
    }
}