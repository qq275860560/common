package com.ctbiyi.dataxweb.controller;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.qq275860560.common.util.RequestUtil;
import com.github.qq275860560.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class UserController {

	@Value("${loginUrl}")
	private String loginUrl;
	 
	@RequestMapping(value = "/getLoginUrl")
	public Map<String, Object> getLoginUrl()  throws Exception{		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取loginUrl成功");
				put("data", loginUrl);
			}
		}; 
	}
	@Value("${logoutUrl}")
	private String logoutUrl;
	 
	@RequestMapping(value = "/getLogoutUrl")
	public Map<String, Object> getLogoutUrl()  throws Exception{		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取logoutUrl成功");
				put("data", logoutUrl);
			}
		}; 
	}
    
	@RequestMapping(value = "/login")
	public void login(HttpServletResponse response,@RequestParam(required=false) String ticket )  throws Exception{
		log.info(""+ticket);
		String result= null;
		if(ticket!=null) {
		result="<html>\r\n" + 
				"<head>\r\n" + 
				"<script>\r\n" + 
				"localStorage.setItem(\"access_token\",\""+ticket+"\" );\r\n" + 
				"localStorage.setItem(\"token_type\",\"Bearer\" );\r\n" + 
				"window.location.href=\"/\";\r\n" + 
				"</script>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"</body>\r\n" + 
				"</html>";
		}else {
			result="<html>\r\n" + 
					"<head>\r\n" + 
					"<script>\r\n" + 
					"localStorage.clear();\r\n" + 
					"window.location.href=\"/\";\r\n" + 
					"</script>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"</body>\r\n" + 
					"</html>";
		}
			
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
		
	}
	 
	
	
	
}
