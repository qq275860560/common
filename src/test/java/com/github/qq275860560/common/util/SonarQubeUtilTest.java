package com.github.qq275860560.common.util;

import java.util.Map;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class SonarQubeUtilTest {
	 	
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
