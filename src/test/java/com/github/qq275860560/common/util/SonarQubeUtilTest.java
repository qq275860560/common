package com.github.qq275860560.common.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.qq275860560.common.util.JsonUtil;
import com.github.qq275860560.common.util.SonarQubeUtil;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class SonarQubeUtilTest {
	private static Log log = LogFactory.getLog(SonarQubeUtilTest.class);

 
	
	 
	
	
	@Test
	public void listTest() throws Exception{
		  String  result = SonarQubeUtil.listTest(   );
		  log.info(result);
	}
	
	@Test
	public void showTest() throws Exception{
		  String  result = SonarQubeUtil.showTest(   );
		  log.info(result);
	}
	@Test
	public void showComponents() throws Exception{
		String componentKey ="com.github.edocker:github-edocker-plan";
		  String  result = SonarQubeUtil.showComponents(  componentKey );
		  log.info(result);
		}
	
	@Test
	public void measuresComponent() throws Exception{
		String componentKey ="com.github.edocker:github-edocker-plan";
		Map<String, Object>  result = SonarQubeUtil.measuresComponent(  componentKey );
		  log.info(JsonUtil.toJSONString( result));
		}
}
