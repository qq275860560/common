package com.github.qq275860560.common.util;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.niws.client.http.RestClient;

import feign.Feign;
import feign.Headers;
import feign.Request.Options;
import feign.RequestLine;
import feign.Retryer;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.RibbonClient;

/**
 * @author jiangyuanlin@163.com
 */
public class FeignUtil {

	private static final Log log = LogFactory.getLog(FeignUtil.class);

    
	/**
     * 适用于Content-Type和Accept都为json的接口
     * @param clazz 使用feign访问的接口类,如MedBodyClient.class
      * @param fallback 回退类
     * @param url 添加网址
     * @return
     */
    public static <T> T buildHystrixFeign(String clientName, Set<String> serverList , final Class<T> clazz, String url , T fallback) {
    	RibbonUtil.initServers(clientName, serverList);
        return HystrixFeign.builder().client(RibbonClient.create()).encoder(new JacksonEncoder()).decoder(new JacksonDecoder()) 
                //options添加Feign请求响应超时时间
                .options(new Options(60 * 1000, 60 * 1000)).retryer(Retryer.NEVER_RETRY)
                .setterFactory(new SetterFactory() {
                    @Override
                    public Setter create(Target<?> target, Method method) {
                        //添加Hstrix请求响应超时时间
                        return HystrixCommand.Setter
                                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(clazz.getClass().getSimpleName()))
                                .andCommandPropertiesDefaults(
                                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(60 * 1000) // 超时配置
                        );
                    }
                }).target(clazz, url, fallback);
    }
    
	/**
	 * 适用于Content-Type和Accept都为json的接口
	 * @param clazz
	 * @param url
	 * @return
	 */
	public static <T> T buildFeignClient(String clientName, Set<String> serverList , Class<T> clazz,String url){
		RibbonUtil.initServers(clientName, serverList);
		return Feign.builder()
		.client(RibbonClient.create()).encoder(new JacksonEncoder())
		.decoder(new JacksonDecoder())
		.target(clazz, url);
	}
	
	/**
	 * 适用于Content-Type和Accept都为json的接口
	 * @param clazz
	 * @param url
	 * @return
	 */
	public static <T> T buildFeignClient(String propertiesFile, Class<T> clazz,String url){
		RibbonUtil.initServersFromPropertiesFile(propertiesFile);
		return Feign.builder()
		.client(RibbonClient.create()).encoder(new JacksonEncoder())
		.decoder(new JacksonDecoder())
		.target(clazz, url);
	}
	
	public static void main2(String[] args) throws Exception{

		long start = System.currentTimeMillis();
	 
		
		Class<PortClientByFeign> clazz = PortClientByFeign.class;
		String url = "http://GITHUB-QQ275860560-COMMON";
		PortClientByFeign portClientByFeign =  buildFeignClient("ribbon.properties",clazz, url);

		String clientName = "GITHUB-QQ275860560-COMMON";
		Set<String> serverList = new HashSet<>(Arrays.asList(new String[] { "127.0.0.1:8080","127.0.0.1:8081" }));
		//PortClientByFeign portClientByFeign =  buildFeignClient(clientName,serverList , clazz, url);

		
		for (int i = 1; i <= 10; i++) {
			Map<String, Object> requestBody = new HashMap<>();
			Map<String, Object> responseMap = portClientByFeign.getPortByFeign(requestBody);
			System.out.println(JsonUtil.toJSONString(responseMap));
			
		}
		
	 
		
		log.info("总共用时="+(System.currentTimeMillis()-start)+"ms");
		
		
	
        

	}
	
	
	public static void main(String[] args) throws Exception{
		RibbonUtil.initServersFromPropertiesFile("ribbon.properties");
		String clientName = "GITHUB-QQ275860560-COMMON";
		RestClient client = (RestClient)ClientFactory.getNamedClient(clientName);  
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("/api/common/port/getPort")).header("Content-Type", "application/json;charset=UTF-8").build();  
       //如何post请求？
        for(int i = 0; i < 4; i ++) {  
            HttpResponse response = client.executeWithLoadBalancer(request);  
            
            String result = response.getEntity(String.class);
            System.out.println("请求结果："+result);
        }
	}
}

interface PortClientByFeign {
	@RequestLine("POST /api/common/port/getPort")
	@Headers(value = { "Content-Type: application/json;charset=UTF-8" })
	Map<String, Object> getPortByFeign(Map<String, Object> requestBody);
}
