package com.github.qq275860560.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author jiangyuanlin@163.com
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
	private static Log log = LogFactory.getLog(SpringContextUtil.class);
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