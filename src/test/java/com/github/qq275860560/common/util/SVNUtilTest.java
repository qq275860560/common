package com.github.qq275860560.common.util;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class SVNUtilTest {
	@Test
	public void get() throws Exception{ 
		String url = "https://192.168.115.56:9000/svn/github-build/trunk/github-build/pom.xml";
		String username = "jiangyuanlin";
		String password = "123456";
		File destFile = new File(System.getProperty("java.io.tmpdir") + "/" + "pom.xml");
		String result = SVNUtil.getFileContent(url, username, password);
		log.info(result);
	}
}
