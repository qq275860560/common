package com.github.qq275860560.common.util;

import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class IpUtilTest {
	@Ignore
	@Test
	public void getPingStatus() throws Exception {
		Assert.assertEquals(true, IpUtil.getIpStatus("127.0.0.1"));
		Assert.assertEquals(true, IpUtil.getIpStatus("8.8.8.8"));
		Assert.assertEquals(true, IpUtil.getIpStatus("www.taobao.com"));
		Assert.assertEquals(true, IpUtil.getIpStatus("www.baidu.com"));
		Assert.assertEquals(false, IpUtil.getIpStatus("www.无效网站.com"));

	}

	
	@Test
	public void getPortStatus() throws Exception {
		Assert.assertEquals(true, IpUtil.getPortStatus("www.taobao.com", 80));
		Assert.assertEquals(true, IpUtil.getPortStatus("www.baidu.com", 80));

	}

	@Ignore
	@Test
	public void getIpInfo()  {
		Assert.assertEquals( true ,  JsonUtil.parse(IpUtil.getIpInfo("114.114.114.114"),Map.class).get("status"));
		//Assert.assertEquals( true ,   JsonUtil.getJsonObject(IpUtil.getIpInfo("8.8.8.8"),Map.class).get("status"));
 

	}

	@Test
	public void getUrlStatus() throws Exception{
		String url = "http://www.baidu.com";
		boolean b = IpUtil.getUrlStatus(url);
		log.info(""+b);
	}
}
