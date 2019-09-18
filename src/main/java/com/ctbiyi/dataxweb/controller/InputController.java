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
import com.ctbiyi.dataxweb.dao.FtpReaderDao;
import com.ctbiyi.dataxweb.dao.HttpReaderDao;
import com.ctbiyi.dataxweb.dao.InputDao;
import com.ctbiyi.dataxweb.dao.MysqlReaderDao;
import com.ctbiyi.dataxweb.dao.TxtFileReaderDao;

import lombok.extern.slf4j.Slf4j;

 
@RestController
@Slf4j
public class InputController {

	@Autowired
	private InputDao inputDao;
	@Autowired
	private MysqlReaderDao mysqlReaderDao;
	@Autowired
	private TxtFileReaderDao txtFileReaderDao;
	@Autowired
	private HttpReaderDao httpReaderDao;
	@Autowired
	private FtpReaderDao ftpReaderDao;
	
	 
	@RequestMapping(value = "/api/input/checkInput")
	public Map<String, Object> checkInput(@RequestParam Map<String, Object> requestMap) throws Exception {
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
		boolean data = inputDao.checkInput(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}
	
	 
	@RequestMapping(value = "/api/input/pageInput")
	public Map<String, Object> pageInput(@RequestParam Map<String, Object> requestMap) throws Exception {
		 

		String name = (String) requestMap.get("name");
		String type = (String) requestMap.get("type");

		String createUserName = (String) requestMap.get("createUserName");
		String startCreateTime = (String) requestMap.get("startCreateTime");
		String endCreateTime = (String) requestMap.get("endCreateTime");
		Integer pageNum = requestMap.get("pageNum") == null ? 1
				: Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize = requestMap.get("pageSize") == null ? 10
				: Integer.parseInt(requestMap.get("pageSize").toString());

		Map<String, Object> data = inputDao.pageInput(null, name,  type,  null,
				createUserName, startCreateTime, endCreateTime, pageNum, pageSize);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());// 此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");// 此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);
			}
		};
	}

	 
	@RequestMapping(value = "/api/input/getInput")
	public Map<String, Object> getInput(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = (String) requestMap.get("id");
		Map<String, Object> inputMap = inputDao.getInput(id);		

		String type = (String)inputMap.get("type");
		Map<String,Object> data = new HashMap<>();
		if(type.equalsIgnoreCase(Constant.INPUT_TYPE_MYSQLREADER)) {
			data.putAll(mysqlReaderDao.getMysqlReader(id));
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_TXTFILEREADER)) {
			data.putAll(txtFileReaderDao.getTxtFileReader(id));
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_HTTPREADER)) {
			data.putAll(httpReaderDao.getHttpReader(id));
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_FTPREADER)) {
			data.putAll(ftpReaderDao.getFtpReader(id));
		}	
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}

	 
	 
	@RequestMapping(value = "/api/input/saveInput")
	public Map<String, Object> saveInput(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = UUID.randomUUID().toString().replace("-", "");
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
					put("msg", "输入流类型不能为空");
					put("data", null);
				}
			};
		}
		

	 
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);	 
	 
		if(type.equalsIgnoreCase(Constant.INPUT_TYPE_MYSQLREADER)) {
			mysqlReaderDao.saveMysqlReader(requestMap);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_TXTFILEREADER)) {
			txtFileReaderDao.saveTxtFileReader(requestMap);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_HTTPREADER)) {
			httpReaderDao.saveHttpReader(requestMap);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_FTPREADER)) {
			ftpReaderDao.saveFtpReader(requestMap);
		} 	 
		inputDao.saveInput(requestMap);
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}

	 
	 
	@RequestMapping(value = "/api/input/updateInput")
	public Map<String, Object> updateInput(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		// name,type不允许修改
		String id = (String) requestMap.get("id");		
		Map<String, Object> inputMap = inputDao.getInput(id);	 
		String type = (String)inputMap.get("type");
		Map<String, Object> readerMap = null;
		if(type.equals(Constant.INPUT_TYPE_MYSQLREADER)) {
			readerMap=mysqlReaderDao.getMysqlReader(id);
			readerMap.putAll(requestMap);		
			mysqlReaderDao.updateMysqlReader(readerMap);
		}else if(type.equals(Constant.INPUT_TYPE_TXTFILEREADER)) {
			readerMap=txtFileReaderDao.getTxtFileReader(id);
			readerMap.putAll(requestMap);		
			txtFileReaderDao.updateTxtFileReader(readerMap);
		}else if(type.equals(Constant.INPUT_TYPE_HTTPREADER)) {
			readerMap=httpReaderDao.getHttpReader(id);
			readerMap.putAll(requestMap);		
			httpReaderDao.updateHttpReader(readerMap);
		}else if(type.equals(Constant.INPUT_TYPE_FTPREADER)) {
			readerMap=ftpReaderDao.getFtpReader(id);
			readerMap.putAll(requestMap);		
			ftpReaderDao.updateFtpReader(readerMap);
		}
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}

	 
 
	@RequestMapping(value = "/api/input/deleteInput")
	public Map<String, Object> deleteInput(@RequestParam Map<String, Object> requestMap) throws Exception {
 

		String id = (String) requestMap.get("id");
		Map<String, Object> inputMap = inputDao.getInput(id);	
		
	 	String type = (String)inputMap.get("type");
		if(type.equalsIgnoreCase(Constant.INPUT_TYPE_MYSQLREADER)) {
			mysqlReaderDao.deleteMysqlReader(id);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_TXTFILEREADER)) {
			txtFileReaderDao.deleteTxtFileReader(id);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_HTTPREADER)) {
			httpReaderDao.deleteHttpReader(id);
		}else if(type.equalsIgnoreCase(Constant.INPUT_TYPE_FTPREADER)) {
			ftpReaderDao.deleteFtpReader(id);
		}
		
		inputDao.deleteInput(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}

}
