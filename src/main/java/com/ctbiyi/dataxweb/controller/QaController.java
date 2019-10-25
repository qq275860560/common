package com.ctbiyi.dataxweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctbiyi.dataxweb.dao.QaDao;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class QaController {



	@Autowired
	private QaDao qaDao;
 
 
  
	@RequestMapping(value = "/api/qa/checkQa")
	public Map<String, Object> checkQa(@RequestParam Map<String, Object> requestMap) throws Exception {
		String id = (String) requestMap.get("id");
		String q = (String) requestMap.get("q");
		if (StringUtils.isEmpty(q)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "必填");
					put("data", null);
				}
			};
		}
		boolean data = qaDao.checkQa(id, q);
		String msg = data == true ? "有效" : "已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}

	 
	@RequestMapping(value = "/api/qa/pageQa")
	public Map<String, Object> pageQa(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{		
		String q=(String)requestMap.get("q");
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = qaDao.pageQa(null, q, null ,null, createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	
 
 	@RequestMapping(value = "/api/qa/getQa")
	public Map<String, Object> getQa(@RequestParam Map<String, Object> requestMap)  throws Exception{
	 
		String id=(String)requestMap.get("id");
		Map<String, Object> data=qaDao.getQa(id);	 
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
 	
	 
	
 	
 	 
  
	@RequestMapping(value = "/api/qa/saveQa")
	public Map<String, Object> saveQa(@RequestParam Map<String, Object> requestMap  )  throws Exception{
		 	
	 
		String id=UUID.randomUUID().toString().replace("-", "");
		requestMap.put("id", id);	
	
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		qaDao.saveQa(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", id);
			}
		};
	}
	
	 
	
 
	@RequestMapping(value = "/api/qa/updateQa")
	public Map<String, Object> updateQa(@RequestParam Map<String, Object> requestMap) throws Exception {
		String id = (String) requestMap.get("id");		
		Map<String, Object> qaMap = qaDao.getQa(id);	
		qaMap.putAll(requestMap);
		qaDao.updateQa(qaMap); 
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}

 
 	@RequestMapping(value = "/api/qa/deleteQa")
	public Map<String, Object> deleteQa(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	 
		
		String id=(String)requestMap.get("id");
		qaDao.deleteQa(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	
 	 
 	 
 	
 
}
