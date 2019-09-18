package com.ctbiyi.dataxweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctbiyi.dataxweb.dao.BuildDao;

import lombok.extern.slf4j.Slf4j;

 
@RestController
@Slf4j
public class BuildController {


  
	@Autowired
	private BuildDao buildDao;
 
 
 

	 
	@RequestMapping(value = "/api/build/pageBuild")
	public Map<String, Object> pageBuild(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{
		
		
		String name=(String)requestMap.get("name");
		String jobName=(String)requestMap.get("jobName");
		String number=(String)requestMap.get("number");
	
		Integer status = requestMap.get("status") == null ? null
				: Integer.parseInt(requestMap.get("status").toString());
		Double progress = requestMap.get("progress") == null ? null
				: Double.parseDouble(requestMap.get("progress").toString());		
		Integer result = requestMap.get("result") == null ? null
				: Integer.parseInt(requestMap.get("result").toString());
		
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = buildDao.pageBuild(
				null, name, null,jobName, number, 
				
				status, null, progress, null, result, 
				
				null, null,createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	

	 
 	@RequestMapping(value = "/api/build/getBuild")
	public Map<String, Object> getBuild(@RequestParam Map<String, Object> requestMap)  throws Exception{
		
		String id=(String)requestMap.get("id");
		Map<String, Object> data=buildDao.getBuild(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
  
	@RequestMapping(value = "/api/build/saveBuild")
	public Map<String, Object> saveBuild(@RequestParam Map<String, Object> requestMap)  throws Exception{
	
		String id=UUID.randomUUID().toString().replace("-", "");
		requestMap.put("id", id);	
	
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		buildDao.saveBuild(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	


	
 
	@RequestMapping(value = "/api/build/updateBuild")
	public Map<String, Object> updateBuild(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	
		String id=(String)requestMap.get("id");
		Map<String, Object> map=buildDao.getBuild(id);
		map.putAll(requestMap);
		buildDao.updateBuild(map);		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
	 
 	@RequestMapping(value = "/api/build/deleteBuild")
	public Map<String, Object> deleteBuild(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	
		
		String id=(String)requestMap.get("id");
		buildDao.deleteBuild(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	
}
