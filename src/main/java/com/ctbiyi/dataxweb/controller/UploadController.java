package com.ctbiyi.dataxweb.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

 
@RestController
@Slf4j
public class UploadController {



 
 
  
  	@RequestMapping(value = "/upload")
	public Map<String, Object> upload(@RequestParam Map<String, Object> requestMap,@RequestParam("target") MultipartFile target)  throws Exception{
	 	File targetFile = new File(FileUtils.getTempDirectoryPath(), new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".txt");
		FileUtils.writeByteArrayToFile(targetFile, target.getBytes());	
		log.info("上传后路径"+targetFile.getAbsolutePath());
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "上传成功");
				put("data", targetFile.getAbsolutePath());
			}
		};
	}
	
	 

 
}
