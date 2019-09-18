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

import com.ctbiyi.dataxweb.dao.TransformerDao;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class TransformerController {


  
	@Autowired
	private TransformerDao transformerDao;
 
  
	@RequestMapping(value = "/api/transformer/checkTransformer")
	public Map<String, Object> checkTransformer(@RequestParam Map<String, Object> requestMap) throws Exception {
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
		boolean data = transformerDao.checkTransformer(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}
	
 
	@RequestMapping(value = "/api/transformer/pageTransformer")
	public Map<String, Object> pageTransformer(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{
		 
		String name=(String)requestMap.get("name");
		String type=(String)requestMap.get("type");
	
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = transformerDao.pageTransformer(null, name, type, null, null,null, createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	
 
 	@RequestMapping(value = "/api/transformer/getTransformer")
	public Map<String, Object> getTransformer(@RequestParam Map<String, Object> requestMap)  throws Exception{
		 
		String id=(String)requestMap.get("id");
		Map<String, Object> data=transformerDao.getTransformer(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
 
 	 
	@RequestMapping(value = "/api/transformer/saveTransformer")
	public Map<String, Object> saveTransformer(@RequestParam Map<String, Object> requestMap)  throws Exception{
	 
		String id=UUID.randomUUID().toString().replace("-", "");
		requestMap.put("id", id);	
		
		String name = (String) requestMap.get("name");
		if (StringUtils.isEmpty(name)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "名称不能为空");
					put("data", null);
				}
			};
		}
		
	 
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		transformerDao.saveTransformer(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	


	
	 
	@RequestMapping(value = "/api/transformer/updateTransformer")
	public Map<String, Object> updateTransformer(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	 	
		String id=(String)requestMap.get("id");
		Map<String, Object> map=transformerDao.getTransformer(id);
		map.putAll(requestMap);
		transformerDao.updateTransformer(map);		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
 
 	@RequestMapping(value = "/api/transformer/deleteTransformer")
	public Map<String, Object> deleteTransformer(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	 	
		String id=(String)requestMap.get("id");
		transformerDao.deleteTransformer(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	
}
