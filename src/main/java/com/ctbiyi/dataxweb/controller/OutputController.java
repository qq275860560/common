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

import com.ctbiyi.dataxweb.constant.Constant;
import com.ctbiyi.dataxweb.dao.FtpWriterDao;
import com.ctbiyi.dataxweb.dao.HttpWriterDao;
import com.ctbiyi.dataxweb.dao.MysqlWriterDao;
import com.ctbiyi.dataxweb.dao.OutputDao;
import com.ctbiyi.dataxweb.dao.TxtFileWriterDao;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class OutputController {


  
	@Autowired
	private OutputDao outputDao;
	@Autowired
	private MysqlWriterDao mysqlWriterDao;
	@Autowired
	private TxtFileWriterDao txtFileWriterDao;
	@Autowired
	private HttpWriterDao httpWriterDao;
	@Autowired
	private FtpWriterDao ftpWriterDao;
 
 
	 
	@RequestMapping(value = "/api/output/checkOutput")
	public Map<String, Object> checkOutput(@RequestParam Map<String, Object> requestMap) throws Exception {
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
		boolean data = outputDao.checkOutput(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}
	

	 
	@RequestMapping(value = "/api/output/pageOutput")
	public Map<String, Object> pageOutput(
			@RequestParam Map<String, Object> requestMap
			)  throws Exception{
		 	
		String name=(String)requestMap.get("name");
		String type=(String)requestMap.get("type");
	
		String createUserName=(String)requestMap.get("createUserName");
		String startCreateTime=(String)requestMap.get("startCreateTime");
		String endCreateTime=(String)requestMap.get("endCreateTime");
		Integer pageNum =requestMap.get("pageNum")==null?1:Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize =requestMap.get("pageSize")==null?10:Integer.parseInt(requestMap.get("pageSize").toString());
		 
		Map<String, Object> data = outputDao.pageOutput(null,name,  type,  null, createUserName, startCreateTime, endCreateTime, pageNum, pageSize) ;
		return new HashMap<String, Object>() {
			{				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	 
	

	 
 	@RequestMapping(value = "/api/output/getOutput")
	public Map<String, Object> getOutput(@RequestParam Map<String, Object> requestMap)  throws Exception{
		 	
		String id=(String)requestMap.get("id");
		Map<String, Object> outputMap=outputDao.getOutput(id);		
		
		String type = (String)outputMap.get("type");
		Map<String,Object> data = new HashMap<>();
		if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			data.putAll(mysqlWriterDao.getMysqlWriter(id));
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			data.putAll(txtFileWriterDao.getTxtFileWriter(id));
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			data.putAll(httpWriterDao.getHttpWriter(id));
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_FTPWRITER)) {
			data.putAll(ftpWriterDao.getFtpWriter(id));
		}
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
 
  
	@RequestMapping(value = "/api/output/saveOutput")
	public Map<String, Object> saveOutput(@RequestParam Map<String, Object> requestMap)  throws Exception{
		 
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
		
		String type = (String) requestMap.get("type");
		if (StringUtils.isEmpty(type)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "输出流类型不能为空");
					put("data", null);
				}
			};
		}
				
	 
		String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		
		if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			mysqlWriterDao.saveMysqlWriter(requestMap);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			txtFileWriterDao.saveTxtFileWriter(requestMap);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			httpWriterDao.saveHttpWriter(requestMap);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_FTPWRITER)) {
			ftpWriterDao.saveFtpWriter(requestMap);
		}	
		outputDao.saveOutput(requestMap);
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	

 
	@RequestMapping(value = "/api/output/updateOutput")
	public Map<String, Object> updateOutput(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
		 	
		// name,type不允许修改
		String id = (String) requestMap.get("id");	
		Map<String, Object> outputMap = outputDao.getOutput(id);
		String type = (String)outputMap.get("type");
		Map<String, Object> writerMap = null;
		if(type.equals(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			writerMap=mysqlWriterDao.getMysqlWriter(id);
			writerMap.putAll(requestMap);		
			mysqlWriterDao.updateMysqlWriter(writerMap);
		}else if(type.equals(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			writerMap=txtFileWriterDao.getTxtFileWriter(id);
			writerMap.putAll(requestMap);		
			txtFileWriterDao.updateTxtFileWriter(writerMap);
		}else if(type.equals(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			writerMap=httpWriterDao.getHttpWriter(id);
			writerMap.putAll(requestMap);		
			httpWriterDao.updateHttpWriter(writerMap);
		}else if(type.equals(Constant.OUTPUT_TYPE_FTPWRITER)) {
			writerMap=ftpWriterDao.getFtpWriter(id);
			writerMap.putAll(requestMap);		
			ftpWriterDao.updateFtpWriter(writerMap);
		}
		 
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
 
 	@RequestMapping(value = "/api/output/deleteOutput")
	public Map<String, Object> deleteOutput(
			@RequestParam Map<String, Object> requestMap)  throws Exception{
		 	
		String id = (String) requestMap.get("id");
		Map<String, Object> outputMap = outputDao.getOutput(id);	
		
		String type = (String)outputMap.get("type");
		if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			mysqlWriterDao.deleteMysqlWriter(id);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			txtFileWriterDao.deleteTxtFileWriter(id);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			httpWriterDao.deleteHttpWriter(id);
		}else if(type.equalsIgnoreCase(Constant.OUTPUT_TYPE_FTPWRITER)) {
			ftpWriterDao.deleteFtpWriter(id);
		}
		 
		outputDao.deleteOutput(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	 
 	
}
