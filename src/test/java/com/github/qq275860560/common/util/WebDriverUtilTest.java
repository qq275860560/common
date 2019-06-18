package com.github.qq275860560.common.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class WebDriverUtilTest {
	 
	/**
	 * 启动chrome浏览器测试，如果系统不需要兼容此浏览器，把这段代码注释掉即可
	 */
	@Ignore
	@Test
	public void createChromeDriver() throws Exception {
		WebDriver driver = WebDriverUtil.createChromeDriver();		
		boolean b = false;//定义测试结果,默认为false
		try{
			b  = test(driver);//执行测试用例
		}catch (Exception e) {
			log.error("",e);
		}finally{
			WebDriverUtil.closeDriver(driver);
			Assert.assertTrue(b);//用java判断结果成功或者失败，生成报告
		}		
	}

	/**
	 * 启动firefox浏览器测试，如果系统不需要兼容此浏览器，把这段代码注释掉即可
	 */
	@Ignore
	@Test
	public void createFirefoxDriver() throws Exception {
		WebDriver driver = WebDriverUtil.createFirefoxDriver();
		boolean b = false;//定义测试结果,默认为false
		try{
			b  = test(driver);//执行测试用例
		}catch (Exception e) {
			log.error("",e);
		}finally{
			WebDriverUtil.closeDriver(driver);
			Assert.assertTrue(b);//用java判断结果成功或者失败，生成报告
		}		
	}
	
	/**
	 * 启动ie浏览器测试，如果系统不需要兼容此浏览器，把这段代码注释掉即可
	 */
	@Ignore
	@Test
	public void createIEDriver() throws Exception {		
		WebDriver driver = WebDriverUtil.createIEDriver();
		boolean b = false;//定义测试结果,默认为false
		try{
			b  = test(driver);//执行测试用例
		}catch (Exception e) {
			log.error("",e);
		}finally{
			WebDriverUtil.closeDriver(driver);
			Assert.assertTrue(b);//用java判断结果成功或者失败，生成报告
		}			
	}

	/**
	 * 测试场景描述:百度首页输入广州天气，点击百度一下按钮，判断页面跳转后的标题是否为广州天气_百度搜索，是则认为成功，否则失败
	 */
	private boolean test(WebDriver driver) throws Exception {
		driver.get("http://www.baidu.com");//打开网页
		Thread.sleep(2000);//模拟人的操作，每次页面跳转，停留2秒钟	
		JavascriptExecutor js = (JavascriptExecutor) driver;//在当前网页直接用JavaScript引擎来操作浏览器
		js.executeScript("$('#kw').val('广州天气');");//这一段是从浏览器控制台复制出来的
		js.executeScript("$('#su').click();");	//这一段是从浏览器控制台复制出来的
		Thread.sleep(2000);//模拟人的操作，每次页面跳转，停留2秒钟	 
		log.info("真实数据="+js.executeScript("return $('title').text()"));//打印日志
		return (boolean)js.executeScript("return $('title').text()=='广州天气_百度搜索';");//这一段是从浏览器控制台复制出来的，返回true或者false，代表用例成功或者失败
	}
	 
}
