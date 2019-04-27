package com.github.qq275860560.common.util;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;
/**
 * @author jiangyuanlin@163.com
 *
 */
public class EurekaUtil {

	private static final int VERIFY_WAIT_MILLIS = 65 * 1000;
	private static Logger log = LoggerFactory.getLogger(EurekaUtil.class);
	public static EurekaClient eurekaClient;
	public static Properties properties = new Properties();
 
	private static String eurekaServiceUrl;
	static{
	try {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("eureka-client.properties");
		properties.load(in);		
		eurekaServiceUrl = properties.getProperty("eureka.serviceUrl.default");	        
		properties.setProperty("eureka.metadataMap.nodeId", InetAddress.getLocalHost().getHostName() + "_nodeId");
		ConfigurationManager.loadProperties(properties);
		
		
		

		//ConfigurationManager.loadPropertiesFromResources("eureka-client.properties");  
	
		MyDataCenterInstanceConfig instanceConfig = new MyDataCenterInstanceConfig() {
			
			@Override
			public String getHostName(boolean refresh) {//显示到ureka
				return properties.getProperty("instanceId")!=null?properties.getProperty("instanceId"):super.getHostName(refresh);				
			}

			@Override
			public String getIpAddress() {//显示到ureka
				return properties.getProperty("ipAddr")!=null?properties.getProperty("ipAddr"):super.getIpAddress();	
			}

		};
		/*
		创建 EurekaInstanceConfig对象
		使用 EurekaInstanceConfig对象 创建 InstanceInfo对象
		使用 EurekaInstanceConfig对象 + InstanceInfo对象 创建 ApplicationInfoManager对象
		创建 EurekaClientConfig对象
		使用 ApplicationInfoManager对象 + EurekaClientConfig对象 创建 EurekaClient对象
		*/
		InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
		ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
		eurekaClient = new DiscoveryClient(applicationInfoManager, new DefaultEurekaClientConfig());
		applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
	} catch (Exception e) {
		log.error("",e);
		 System.exit(1);
	}
	}
	  /**
     * 注册一个应用实例
     * @param appId 应用id
     * @param payload json或者xml
     */
	public static void  registerInstance(String appId,String payload) throws Exception{
		start();
	}


    /**
     * 删除一个实例
     * @param appId 应用id
     * @param instanceId 实例id
     */
	public static void  deleteInstance(String appId,String instanceId){
		   String url = String.format(eurekaServiceUrl+"/apps/%s/%s",appId,instanceId);
		   HttpConnectionUtil.send(url, "DELETE", null, null);
	}

    /**
     * 发送一个应用实例心跳
     * @param appId 应用id
     * @param instanceId 实例id
     */
	public static void  heartbeat(String appId,String instanceId){
		String url = String.format(eurekaServiceUrl+"/apps/%s/%s",appId,instanceId);
		HttpConnectionUtil.send(url, "PUT", null, null);
	}

    /**
     * 列出所有实例
     * @return json/xml
     */
	private static String instances(){
	    String url = eurekaServiceUrl+"/apps";
		return HttpConnectionUtil.send(url, "GET", null, null);
	}

    /**
     * 列出应用下的所有实例
     * @param appId 应用id
     * @return json/xml
     */
	private static String instances(String appId){
		 String url = String.format(eurekaServiceUrl+"/apps/%s",appId);
		 return HttpConnectionUtil.send(url, "GET", null, null);
	}

    /**
     * 查询指定的实例
     * @param appId 应用id
     * @param instanceId 实例id
     * @return json/xml
     */
	private static String instance(String appId,String instanceId){
		String url = String.format(eurekaServiceUrl+"/apps/%s/%s",appId,instanceId);
		 return HttpConnectionUtil.send(url, "GET", null, null);
	}

    /**
     * 查询特定的实例
     * @param instanceId 实例id
     * @return json/xml
     */
	private static String instance(String instanceId){
		 String url = String.format(eurekaServiceUrl+"/instances/%s",instanceId);
		 return HttpConnectionUtil.send(url, "GET", null, null);
	}
	
	   /**
     * 查询指定服务的ip端口
     * 通过注册中心vipAddress互相调用。实际过程就是调用前去eureka拿一个真实地址替换vipAddress变量。
     */
	public static String getInstanceIpAndPort(String vipAddress){
		 InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(vipAddress, false);
		 return instanceInfo.getIPAddr()+":"+ instanceInfo.getPort();
	}

    /**
     * 中止/失效一个实例
     * @param appId 应用id
     * @param instanceId 实例id
     */
	private static void outOfService(String appId,String instanceId){
		 String url = String.format(eurekaServiceUrl+"/apps/%s/%s/status?value=OUT_OF_SERVICE",appId,instanceId);
		 HttpConnectionUtil.send(url, "PUT", null, null);
	}

    /**
     * 恢复一个实例到指定状态
     * @param appId 应用id
     * @param instanceId 实例id
     * @param status UP, DOWN,STARTING,OUT_OF_SERVICE,UNKNOWN;
     */
	public static void  backInService(String appId, String instanceId, String status){
		if(!status.equals("UP") 
				&& !status.equals("DOWN") 
				&& !status.equals("STARTING") 
				&& !status.equals("OUT_OF_SERVICE") 
				&& !status.equals("UNKNOWN") 	
				) {
			log.info("不存在此状态");
			return;
		}		
		 String url = String.format(eurekaServiceUrl+"/apps/%s/%s/status?value=%s",appId,instanceId,status);
		 HttpConnectionUtil.send(url, "DELETE", null, null);
	}

    /**
     * 更新实例的元数据
     * @param appId 应用id
     * @param instanceId 实例id
     * @param key 键
     * @param value 值
     */
	public static void  updateMetadata(String appId,String instanceId,String key,String value){
	     String url = String.format(eurekaServiceUrl+"/apps/%s/%s/metadata?%s=%s",appId,instanceId,key,value);
	     HttpConnectionUtil.send(url, "PUT", null, null);
	}

    /**
     * 在一个特定的vip地址查询所有实例
     * @param vipAddress vip地址
     * @return json/xml
     */
    private static String listInstancesByVipAddress(String vipAddress){
    	//查询vipAddress域名下的所有服务
		List<InstanceInfo> serverInfos = eurekaClient.getInstancesByVipAddress(vipAddress, false);
		log.info(vipAddress+"已注册服务="+JsonUtil.toJSONString(serverInfos));
    	if(true)return JsonUtil.toJSONString(serverInfos);
    	Map<String, String> map = new HashMap<>();
    	map.put("Accept", "application/json;charset=UTF-8");
    	String url = String.format(eurekaServiceUrl+"/vips/%s",vipAddress);
    	return HttpConnectionUtil.send(url, "GET", null, null);    	  
    }

    /**
     * 在一个特定的安全vip地址查询所有实例
     * @param svipAddress 安全的vip地址
     * @return json/xml
     */
    private static  String svips(String svipAddress){    	
    	 String url = String.format(eurekaServiceUrl+"/svips/%s",svipAddress);
    	 return HttpConnectionUtil.send(url, "GET", null, null);    
    }

	public static void main(String[] args) throws Exception{
		start();
		log.info(JsonUtil.toJSONString(EurekaUtil.getInstanceIpAndPort(EurekaUtil.properties.getProperty("eureka.vipAddress"))));
		if(true)return;
		
		start();
		 //String vipAddress = properties.getProperty("eureka.vipAddress");
		 //log.info(listInstancesByVipAddress(vipAddress));
		 //heartbeat("GITHUB-QQ275860560-COMMON","192.168.137.86");
		while(true){
		 heartbeat(EurekaUtil.properties.getProperty("eureka.name"),EurekaUtil.properties.getProperty("instanceId"));
		 Thread.sleep(20000);
		}
	}
	public static void shutdown(){
		eurekaClient.shutdown();
		//DiscoveryManager.getInstance().shutdownComponent();
	}
	public static void start() throws Exception{
		
	 

	 
		new Thread(new Runnable() {
			@Override
			public void run() {
				String vipAddress = properties.getProperty("eureka.vipAddress");
				long startTime = System.currentTimeMillis();
				InstanceInfo nextServerInfo = null;
				while (nextServerInfo == null) {
					if (System.currentTimeMillis() - startTime > VERIFY_WAIT_MILLIS) {
						log.warn("超时未注册到"+vipAddress);
						return;
					}
					try {						
						nextServerInfo = eurekaClient .getNextServerFromEureka(vipAddress, false);						
						Thread.sleep(5000);					 
					} catch (Throwable e) {
					}					 
				}
				log.info("成功注册="+JsonUtil.toJSONString(nextServerInfo));
				//查询vipAddress域名下的所有服务
				//List<InstanceInfo> serverInfos = eurekaClient.getInstancesByVipAddress(vipAddress, false);
				//log.info(vipAddress+"已注册服务="+JsonUtil.toJSONString(serverInfos));
			}
		}).start();
	 
		//Thread.sleep(500000);	 
	}

}
