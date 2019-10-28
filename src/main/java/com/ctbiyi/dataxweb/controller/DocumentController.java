package com.ctbiyi.dataxweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ctbiyi.dataxweb.dao.DocumentDao;
import com.github.qq275860560.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class DocumentController {



	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private DocumentDao documentDao;
 
 
  
	@RequestMapping(value = "/api/document/checkDocument")
	public Map<String, Object> checkDocument(@RequestParam Map<String, Object> requestMap) throws Exception {
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
		boolean data = documentDao.checkDocument(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}

	 
	@RequestMapping(value = "/api/document/pageDocument")
	public Map<String, Object> pageDocument(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{		
		String name=(String)requestMap.get("name");		 
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = documentDao.pageDocument(null, name,null, createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	
 
 
 
	@RequestMapping(value = "/api/document/downloadDocument")
	public void downloadDocument(@RequestParam Map<String, Object> requestMap,HttpServletResponse response) throws Exception {
 

		String id = (String) requestMap.get("id");
		Map<String, Object> map = documentDao.getDocument(id);
		byte[] file = (byte[]) map.get("file");
		String name = (String)map.get("name");
		ResponseUtil.sendFileByteArray((HttpServletResponse) response, file, name, "application/octet-stream;charset=UTF-8");
		 
	}
 
 	 
	@RequestMapping(value = "/api/document/saveDocument")
	public Map<String, Object> saveDocument(@RequestParam Map<String, Object> requestMap,@RequestParam("file") MultipartFile file )  throws Exception{
		 	
	 
		String id=UUID.randomUUID().toString().replace("-", "");
		requestMap.put("id", id);	
		requestMap.put("file",file.getBytes());
		if(StringUtils.isEmpty(requestMap.get("name"))) {
			requestMap.put("name", file.getOriginalFilename());
		}
 
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		documentDao.saveDocument(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", id);
			}
		};
	}
	
	 
	

 
 	@RequestMapping(value = "/api/document/deleteDocument")
	public Map<String, Object> deleteDocument(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
	 
		
		String id=(String)requestMap.get("id");
		documentDao.deleteDocument(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	
 	 
 	 
 	
 
}
