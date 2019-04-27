package com.github.qq275860560.common.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.qq275860560.common.util.SVNUtil;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class SVNUtilTest {
	private static Log log = LogFactory.getLog(SVNUtilTest.class);

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
