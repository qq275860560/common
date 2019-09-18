package com.ctbiyi.dataxweb.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ctbiyi.dataxweb.dao.PluginDao;
import com.github.qq275860560.common.util.CommandUtil;
import com.github.qq275860560.common.util.CompressUtil;
import com.github.qq275860560.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

 
@RestController
@Slf4j
public class PluginController {



	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PluginDao pluginDao;
 
 
 
	 
	@RequestMapping(value = "/api/plugin/checkPlugin")
	public Map<String, Object> checkPlugin(@RequestParam Map<String, Object> requestMap) throws Exception {
		String id = (String) requestMap.get("id");
		String name = (String) requestMap.get("name");
		if (StringUtils.isEmpty(name)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "名称必填");
					put("data", null);
				}
			};
		}
		boolean data = pluginDao.checkPlugin(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}

	 
	@RequestMapping(value = "/api/plugin/pagePlugin")
	public Map<String, Object> pagePlugin(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{
	 	
		String name=(String)requestMap.get("name");
		Integer type=requestMap.get("type")==null?null:Integer.parseInt(requestMap.get("type").toString());
	
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = pluginDao.pagePlugin(null, name, type ,null, createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	

 
 	@RequestMapping(value = "/api/plugin/getPlugin")
	public Map<String, Object> getPlugin(@RequestParam Map<String, Object> requestMap)  throws Exception{
		 	
		String id=(String)requestMap.get("id");
		Map<String, Object> data=pluginDao.getPlugin(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
 
  
	@RequestMapping(value = "/api/plugin/savePlugin")
	public Map<String, Object> savePlugin(@RequestParam Map<String, Object> requestMap,@RequestParam("readme") MultipartFile readme,@RequestParam("source") MultipartFile source,@RequestParam("distribute") MultipartFile distribute)  throws Exception{
		 
		String id=UUID.randomUUID().toString().replace("-", "");
		requestMap.put("id", id);	
		requestMap.put("readme",readme.getBytes());
		// 解压验证源码是否能够编译
		File zipFile = new File(FileUtils.getTempDirectoryPath(), File.separator + requestMap.get("name")+"-source.zip");
		FileUtils.writeByteArrayToFile(zipFile, source.getBytes());
		//工具类实现参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/CompressUtil.java 
		File destDir=CompressUtil.unZip(zipFile);
		//工具类实现参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/CommandUtil.java 
		String result=CommandUtil.runComand("mvn install", destDir);
		log.info(result);
		requestMap.put("source",source.getBytes());
		
		requestMap.put("distribute",distribute.getBytes());
	 
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		pluginDao.savePlugin(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	
	 
	


	
	 
	@RequestMapping(value = "/api/plugin/updatePlugin")
	public Map<String, Object> updatePlugin(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	 	
		String id=(String)requestMap.get("id");
		Map<String, Object> map=pluginDao.getPlugin(id);
		map.putAll(requestMap);
		pluginDao.updatePlugin(map);		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
	 
 	@RequestMapping(value = "/api/plugin/deletePlugin")
	public Map<String, Object> deletePlugin(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
		 	
		String id=(String)requestMap.get("id");
		pluginDao.deletePlugin(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	/*  curl -i -X POST "http://admin:123456@localhost:8045/api/plugin/getPluginReadme?id=1" 
	*/
	@RequestMapping(value = "/api/plugin/getPluginReadme")
	public Map<String, Object> getPluginReadme(@RequestParam Map<String, Object> requestMap) throws Exception {
	 
		String id = (String) requestMap.get("id");
		Map<String, Object> map = pluginDao.getPlugin(id);
		byte[] readme = (byte[]) map.get("readme");
		String data = new String(readme, "UTF-8");

		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取使用说明成功");
				put("data", data);
			}
		};
	}
 	
 	
 	 
	@RequestMapping(value = "/api/plugin/getPluginSource")
	public void getPluginSource(@RequestParam Map<String, Object> requestMap, HttpServletResponse response)
			throws Exception {
	 
		String id = (String) requestMap.get("id");
		Map<String, Object> map = pluginDao.getPlugin(id);
		byte[] source = (byte[]) map.get("source");
		String fileName = (String) map.get("name") + "-source.zip";
		// 工具类实现参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/ResponseUtil.java
		ResponseUtil.sendFileByteArray(response, source, fileName, "application/octet-stream;charset=UTF-8");
	}
 	 
 	 
 	 	@RequestMapping(value = "/api/plugin/getPluginDistribute")
 		public void getPluginDistribute(
 				@RequestParam Map<String, Object> requestMap,HttpServletResponse response)  throws Exception{
 	 		
 			String id=(String)requestMap.get("id");
 			Map<String, Object> map=pluginDao.getPlugin(id);
 			byte[] source = (byte[])map.get("distribute");
 			String fileName=(String)map.get("name")+"-distribute.zip";	
 			//工具类实现参考https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/ResponseUtil.java 
 			ResponseUtil.sendFileByteArray(response, source, fileName,"application/octet-stream;charset=UTF-8");
 	 	}


 
}
