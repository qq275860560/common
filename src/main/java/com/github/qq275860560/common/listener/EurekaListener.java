/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.qq275860560.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qq275860560.common.util.EurekaUtil;
/**
 * @author jiangyuanlin@163.com
 */
public class EurekaListener implements ServletContextListener {
	private static Logger log = LoggerFactory.getLogger(EurekaListener.class);
	 public static void main(String[] args) throws Exception{
		EurekaUtil.start();
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		EurekaUtil.shutdown();

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {		
			EurekaUtil.start();		
			while(true){
				EurekaUtil.heartbeat(EurekaUtil.properties.getProperty("eureka.name"),EurekaUtil.properties.getProperty("instanceId"));
				 Thread.sleep(20000);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
