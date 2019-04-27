package com.github.qq275860560.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.client.ClientFactory;
import com.netflix.config.AggregatedConfiguration;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;

/**
 * @author jiangyuanlin@163.com
 */
public class RibbonUtil {

	private static final Log log = LogFactory.getLog(RibbonUtil.class);
 
 
 

	public static Server getServer(String clientName) {
		// ClientFactory.getNamedLoadBalancer会缓存结果, 所以不用担心它每次都会向eureka发起查询
		@SuppressWarnings("rawtypes")
		DynamicServerListLoadBalancer lb = (DynamicServerListLoadBalancer) ClientFactory
				.getNamedLoadBalancer(clientName);
		
		RandomRule randomRule = new RandomRule();
		return randomRule.choose(lb, null);
	}

	public static List<Server> listServer(String clientName) {
		// ClientFactory.getNamedLoadBalancer会缓存结果, 所以不用担心它每次都会向eureka发起查询
		@SuppressWarnings("rawtypes")
		DynamicServerListLoadBalancer lb = (DynamicServerListLoadBalancer) ClientFactory
				.getNamedLoadBalancer(clientName);
		return lb.getServerList(true);
	}

	public static void initServers(String clientName, Set<String> serverList) {
		if (serverList.isEmpty())
			return;

		StringBuilder sb = new StringBuilder();
		for (String server : serverList) {
			sb.append(server + ",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		Properties props = new Properties();
		props.setProperty(clientName + ".ribbon.listOfServers", sb.toString());
		props.setProperty(clientName + ".ribbon.NFLoadBalancerRuleClassName", "com.netflix.loadbalancer.RandomRule");
		ConcurrentMapConfiguration config = new ConcurrentMapConfiguration();
		config.loadProperties(props);
		((AggregatedConfiguration) ConfigurationManager.getConfigInstance()).addConfiguration(config, clientName);

	}
	
	/*
	 *spring-boot3.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
	 *spring-boot3.ribbon.listOfServers=127.0.0.1:8080,127.0.0.1:8081
	 */
	public static void initServersFromPropertiesFile(String propertiesFileName ) {
		try{
		ConfigurationManager.loadPropertiesFromResources(propertiesFileName);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) throws Exception {
		String clientName = "spring-boot";
		Set<String> serverList = new HashSet<>(
				Arrays.asList(new String[] { "192.168.199.1:8080", "192.168.199.1:8081" }));
		RibbonUtil.initServers(clientName, serverList);
		// ConfigurationManager.loadPropertiesFromResources("ribbon.properties");
		// log.info("serverList=" + selectAvailableServers("spring-boot"));
		for (int i = 0; i < 10; i++) {
			log.info("random server=" + getServer(clientName));
		}

		Set<String> serverList2 = new HashSet<>(
				Arrays.asList(new String[] { "192.168.199.1:8082", "192.168.199.1:8083" }));

		log.info("====================华丽分割线====================");
		RibbonUtil.initServers(clientName + 2, serverList2);
		for (int i = 0; i < 10; i++) {
			log.info("random server=" + getServer(clientName + 2));
		}

		
		RibbonUtil.initServersFromPropertiesFile("ribbon.properties");
		for (int i = 0; i < 10; i++) {
			log.info("random server=" + getServer("GITHUB-QQ275860560-web"));
		}
	}
}
