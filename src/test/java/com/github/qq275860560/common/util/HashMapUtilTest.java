package com.github.qq275860560.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.qq275860560.common.util.HashMapUtil;
import com.github.qq275860560.common.util.JsonUtil;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class HashMapUtilTest {
	private static Log log = LogFactory.getLog(HashMapUtilTest.class);

	 
	@Test
	public void test() throws Exception {
		Map<String, Object> map = new HashMapUtil.Builder().put("a", "1").put("b", "2").build();
		log.info(JsonUtil.toJSONString(map));
		
		Map<String, Object> map2 = new HashMapUtil.Builder().put("c", "1").put("d", "2").build();
		log.info(JsonUtil.toJSONString(map2));
		log.info(JsonUtil.toJSONString(map));
		 
	}
	@Test
	public void test2() throws Exception {
	Map<String, Object> json_map =new HashMapUtil.Builder()
	.put("stapler-class-bag", "true")
	.put("com-coravy-hudson-plugins-github-GithubProjectPropert", new HashMap<>())
	.put("jenkins-branch-RateLimitBranchProperty$JobPropertyImpl", new HashMap<>())
	.put("jenkins-model-BuildDiscarderProperty",  
		new HashMapUtil.Builder()
		.put("specified", false)
		.put("specified", "0")
		.put("strategy",  		
			new HashMapUtil.Builder()
			.put("daysToKeepStr", "")
			.put("daysToKeepStr", "")
			.put("numToKeepStr", "")
			.put("artifactDaysToKeepStr", "")
			.put("artifactNumToKeepStr", "")
			.put("stapler-class", "hudson.tasks.LogRotator")
			.put("$class", "hudson.tasks.LogRotator")
			.build()
		)
		.build()		
	)
	.put("hudson-model-ParametersDefinitionProperty", 
			new HashMapUtil.Builder().put("specified", false)
			.build()
	)
	.build();
	
	log.info(JsonUtil.toJSONString(json_map));
	}
	
	
 
}
