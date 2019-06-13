package com.github.qq275860560.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
//复制到项目中要启用本行@ControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {
	//复制到项目中要启用本行@ResponseBody
	//复制到项目中要启用本行@ExceptionHandler(value = Throwable.class)
	public Map handle(Throwable t, HttpServletResponse response) {
		log.debug("", t);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.BAD_REQUEST.value());
				put("msg", "请求失败");
				put("data",t.getMessage());
			}
		};

	}

}
