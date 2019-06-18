package com.github.qq275860560.common.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class AspectLogUtil {
	 
	public static Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String methodName = method.getName();
		if (!methodName.matches(
				"(get|list|page|count|check|save|update|delete|upload|download|import|export|send|receive|batchUpdate|batchDelete|batchUpload|batchImport).*")) {
			log.info(
					"为了前后端联调方便，接口名请使用关键字前缀get|list|page|count|check|save|update|delete|upload|download|import|export|send|receive");
		}
		Object[] args = joinPoint.getArgs();
		Set<Object> set = new HashSet<>();
		for (Object obj : args) {

			if (obj == null || obj instanceof Serializable) {
				set.add(obj);
			} else {
				log.info(obj.getClass() + "没有实现序列化，无法打印");
			}
		}

		if (!methodName.matches("(upload|batchUpload|import|batchImport).*")) {
			log.info(
					method.getDeclaringClass().getName() + "." + method.getName() + "输入=" + JsonUtil.toJSONString(set));
		} else {
			log.info("上传导入不打印请求参数");
		}
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();

		if (!methodName.matches("(download|export).*")) {
			log.info(method.getDeclaringClass().getName() + "." + method.getName() + "输出="
					+ JsonUtil.toJSONString(result));
		} else {
			log.info("下载导出不打印返回结果");
		}
		log.info(method.getDeclaringClass().getName() + "." + method.getName() + "耗时="
				+ (System.currentTimeMillis() - start));
		return result;
	}

}