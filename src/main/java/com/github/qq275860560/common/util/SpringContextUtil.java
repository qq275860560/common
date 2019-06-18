package com.github.qq275860560.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware {
	 	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	public static <T> T getBean(String beanName) {
		return applicationContext == null ? null : (T) applicationContext.getBean(beanName);
	}

	public static Object getBean(Class<?> clazz) {
		return applicationContext == null ? null : applicationContext.getBean(clazz);
	}
}