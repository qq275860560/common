package com.ctbiyi.dataxweb.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ctbiyi.dataxweb.constant.Constant;
import com.ctbiyi.dataxweb.dao.BuildDao;
import com.ctbiyi.dataxweb.dao.FtpReaderDao;
import com.ctbiyi.dataxweb.dao.FtpWriterDao;
import com.ctbiyi.dataxweb.dao.HttpReaderDao;
import com.ctbiyi.dataxweb.dao.HttpWriterDao;
import com.ctbiyi.dataxweb.dao.InputDao;
import com.ctbiyi.dataxweb.dao.JobDao;
import com.ctbiyi.dataxweb.dao.MysqlReaderDao;
import com.ctbiyi.dataxweb.dao.MysqlWriterDao;
import com.ctbiyi.dataxweb.dao.OutputDao;
import com.ctbiyi.dataxweb.dao.TransformerDao;
import com.ctbiyi.dataxweb.dao.TxtFileReaderDao;
import com.ctbiyi.dataxweb.dao.TxtFileWriterDao;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qq275860560.common.util.JenkinsUtil;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class JobController {

	@Value("${jenkinsUrl}")
	private String jenkinsUrl;
	@Autowired
	private RestTemplate restTemplate  ;
	@Autowired
	private JobDao jobDao;
	@Autowired
	private BuildDao buildDao;
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
	@Autowired
	private TransformerDao transformerDao;
	
	@Autowired
	private ObjectMapper objectMapper;

	 
	@RequestMapping(value = "/api/job/checkJob")
	public Map<String, Object> checkJob(@RequestParam Map<String, Object> requestMap) throws Exception {
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
		boolean data = jobDao.checkJob(id, name);
		String msg = data == true ? "名称有效" : "名称已存在";
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", msg);
				put("data", data);
			}
		};

	}


	 
	@RequestMapping(value = "/api/job/pageJob")
	public Map<String, Object> pageJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String name = (String) requestMap.get("name");
		String inputName = (String) requestMap.get("inputName"); 
		String inputType = (String) requestMap.get("inputType"); 
		String outputName = (String) requestMap.get("outputName");
		String outputType = (String) requestMap.get("outputType");
		
		String transformerName = (String) requestMap.get("transformerName");
		String transformerType = (String) requestMap.get("transformerType");
 
		Integer status = requestMap.get("status") == null ? null
				: Integer.parseInt(requestMap.get("status").toString());
		Double lastBuildProgress = requestMap.get("lastBuildProgress") == null ? null
				: Double.parseDouble(requestMap.get("lastBuildProgress").toString());
		String createUserName = (String) requestMap.get("createUserName");
		String startCreateTime = (String) requestMap.get("startCreateTime");
		String endCreateTime = (String) requestMap.get("endCreateTime");
		log.info("" + requestMap.get("pageNum"));
		Integer pageNum = requestMap.get("pageNum") == null ? 1
				: Integer.parseInt(requestMap.get("pageNum").toString());
		Integer pageSize = requestMap.get("pageSize") == null ? 10
				: Integer.parseInt(requestMap.get("pageSize").toString());

		Map<String, Object> data = jobDao.pageJob(
				null, name, null, inputName, 
				inputType,null,outputName,outputType,
				null,transformerName,transformerType,
				null, status,
				null,null,null,	null,null, 
				null,null,null,lastBuildProgress,  
				null,createUserName, startCreateTime, endCreateTime, pageNum,pageSize);

		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());// 此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");// 此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);
			}
		};
	}

	 
	@RequestMapping(value = "/api/job/getJob")
	public Map<String, Object> getJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 

		//1TODO 把具体的输入流输出流，转换清洗规则也加载出来
		String id = (String) requestMap.get("id");
		Map<String, Object> data = jobDao.getJob(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}

	
	
	 
	@RequestMapping(value = "/api/job/saveJob")
	public Map<String, Object> saveJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
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

		//生成datax格式的reader
		Map<String, Object> readerMap = null;
		String inputId = (String) requestMap.get("inputId");
		if (StringUtils.isEmpty(inputId)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "输入流不能为空");
					put("data", null);
				}
			};
		}		
		Map<String, Object> inputMap = inputDao.getInput(inputId);
		requestMap.put("inputName", inputMap.get("name"));
		requestMap.put("inputType", inputMap.get("type"));
		String inputType = (String)inputMap.get("type");
		if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_MYSQLREADER)) {
			readerMap = generateReaderMap(mysqlReaderDao.getMysqlReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_TXTFILEREADER)) {			
			readerMap = generateReaderMap(txtFileReaderDao.getTxtFileReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_HTTPREADER)) {			
			readerMap = generateReaderMap(httpReaderDao.getHttpReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_FTPREADER)) {			
			readerMap = generateReaderMap(ftpReaderDao.getFtpReader(inputId));		
		}

		//生成datax格式的writer
		Map<String, Object> writerMap = null;
		String outputId = (String) requestMap.get("outputId");
		if (StringUtils.isEmpty(inputId)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "输出流不能为空");
					put("data", null);
				}
			};
		}		
		Map<String, Object> outputMap = outputDao.getOutput(outputId);
		requestMap.put("outputName", outputMap.get("name"));
		requestMap.put("outputType", outputMap.get("type"));
		String outputType = (String)outputMap.get("type");
		if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			writerMap = generateWriterMap(mysqlWriterDao.getMysqlWriter(outputId));	
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			writerMap = generateWriterMap(txtFileWriterDao.getTxtFileWriter(outputId));		
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			writerMap = generateWriterMap(httpWriterDao.getHttpWriter(outputId));		
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_FTPWRITER)) {
			writerMap = generateWriterMap(ftpWriterDao.getFtpWriter(outputId));		
		}
		
		//生成datax格式的transformer
		List<HashMap<String, Object>> transformerList = new ArrayList<>();
		String transformerId = (String) requestMap.get("transformerId");
		Map<String, Object> transformerMap = transformerDao.getTransformer(transformerId);
		requestMap.put("transformerName", transformerMap.get("name"));
		requestMap.put("transformerType", transformerMap.get("type"));
		if(transformerId!=null) {
			transformerList = generateTransformerList(transformerMap);		 
		}		
		String dataxJson = objectMapper.writeValueAsString(generateDataxMap(readerMap,writerMap,transformerList));
		requestMap.put("dataxJson", dataxJson);

		
		requestMap.put("status", Constant.JOB_STATUS_ENABLE);
		requestMap.put("nextBuildNumber", "1");
		Double progress = 0.0;
		requestMap.put("progress", progress);

 
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		requestMap.put("createTime", createTime);
		jobDao.saveJob(requestMap);
		
	    //1TODO 这部分使用异步处理	
		File file = new File(Constant.DATAX_HOME + File.separator + "job" + File.separator + name + ".json");
		FileUtils.writeStringToFile(file, dataxJson, "UTF-8");

		String command = "python " + Constant.DATAX_HOME + File.separator + "bin" + File.separator + "datax.py "
				+ file.getAbsolutePath() ;		
		ResponseEntity<String> response = restTemplate.exchange(String.format("%s/createItem?name=%s", jenkinsUrl, name),
				HttpMethod.POST, new HttpEntity<>(JenkinsUtil.generateJenkinsJobXml(command), new HttpHeaders() {
					{
						set("Content-Type", "text/xml; charset=UTF-8");

					}
				}), String.class);
		log.info(response.getBody());
		//		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}

	public static Map<String, Object> generateDataxMap(Map<String, Object> readerMap ,
			Map<String, Object> writerMap ,
			List<HashMap<String, Object>> transformerList 
			
			)
			throws IOException, JsonParseException, JsonMappingException {
		
		Map<String, Object> dataxMap = new HashMap<String, Object>() {
			{
				put("job", new HashMap<String, Object>() {
					{
						put("setting", new HashMap<String, Object>() {
							{
								put("speed", new HashMap<String, Object>() {
									{
										put("channel", 3);
									}
								});
								put("errorLimit", new HashMap<String, Object>() {
									{
										put("record", 0);
										put("percentage", 0.02);
									}
								});

							}
						});
						put("content", new ArrayList<Map<String, Object>>() {
							{
								add(new HashMap<String, Object>() {
									{
										put("reader", readerMap);
										put("writer", writerMap);
										if(!transformerList.isEmpty()) {
											put("transformer", transformerList);
										}

									}
								});
							}
						});

					}
				});
			}
		};
		return dataxMap;
	}

	public static Map<String, Object> generateReaderMap(Map<String, Object> requestMap) throws Exception{
		String type = (String) requestMap.get("type");
		if (Constant.INPUT_TYPE_MYSQLREADER.equals(type)) {
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							String username = (String) requestMap.get("parameterUsername");
							put("username", username);
							String password = (String) requestMap.get("parameterPassword");
							put("password", password);

							String[] column = ((String) requestMap.get("parameterColumn")).split(",");
							put("column", column);

							String where = (String) requestMap.get("parameterWhere");
							put("where", where);

							put("connection", new ArrayList<HashMap<String, Object>>() {
								{
									add(new HashMap<String, Object>() {
										{
											String[] jdbcUrl = ((String) requestMap
													.get("parameterConnectionJdbcUrl")).split(",");
											put("jdbcUrl", jdbcUrl);
											String[] table = ((String) requestMap.get("parameterConnectionTable"))
													.split(",");
											put("table", table);
										}
									});

								}
							});

						}
					});

				}
			};
		}else if (Constant.INPUT_TYPE_TXTFILEREADER.equals(type)) {				 			
				return new HashMap<String, Object>() {
					{
						put("name", type);
						put("parameter", new HashMap<String, Object>() {
							{
								String path = (String) requestMap.get("parameterPath");
								put("path", path);
								put("encoding", "UTF-8");
								String fieldDelimiter = (String) requestMap.get("parameterFieldDelimiter");
								put("fieldDelimiter", fieldDelimiter);
								String column = (String) requestMap.get("parameterColumn");
								put("column", new ObjectMapper().readValue(column,List.class));
							}
						});

					}
				};		 
		}else if (Constant.INPUT_TYPE_HTTPREADER.equals(type)) {				 			
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							String path = (String) requestMap.get("parameterPath");
							put("path", path);
							put("encoding", "UTF-8");
							String fieldDelimiter = (String) requestMap.get("parameterFieldDelimiter");
							put("fieldDelimiter", fieldDelimiter);
							String column = (String) requestMap.get("parameterColumn");
							put("column", new ObjectMapper().readValue(column,List.class));
						}
					});

				}
			};		 
	   }else if (Constant.INPUT_TYPE_FTPREADER.equals(type)) {				 			
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							
							String protocol = (String) requestMap.get("parameterProtocol");
							put("protocol", protocol);
							String host = (String) requestMap.get("parameterHost");
							put("host", host);
							String port = (String) requestMap.get("parameterPort");
							put("port", port);
							String username = (String) requestMap.get("parameterUsername");
							put("username", username);
							String password = (String) requestMap.get("parameterPassword");
							put("password", password);						 
							
							String path = (String) requestMap.get("parameterPath");
							put("path", path);
							put("encoding", "UTF-8");
							String fieldDelimiter = (String) requestMap.get("parameterFieldDelimiter");
							put("fieldDelimiter", fieldDelimiter);
							String column = (String) requestMap.get("parameterColumn");
							put("column", new ObjectMapper().readValue(column,List.class));
						}
					});

				}
			};		 
		}	
		return null;
	}

	public static Map<String, Object> generateWriterMap(Map<String, Object> requestMap)
			throws Exception {
		String type = (String) requestMap.get("type");

		if (Constant.OUTPUT_TYPE_MYSQLWRITER.equals(type)) {
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							String username = (String) requestMap.get("parameterUsername");
							put("username", username);
							String password = (String) requestMap.get("parameterPassword");
							put("password", password);

							String writeMode = (String) requestMap.get("parameterWriteMode");
							put("writeMode", writeMode);

							String[] column = ((String) requestMap.get("parameterColumn")).split(",");
							put("column", column);

							String[] preSql = ((String) requestMap.get("parameterPreSql")).split(",");
							put("preSql", preSql);

							put("connection", new ArrayList<HashMap<String, Object>>() {
								{
									add(new HashMap<String, Object>() {
										{
											String jdbcUrl = (String) requestMap
													.get("parameterConnectionJdbcUrl");
											put("jdbcUrl", jdbcUrl);

											String[] table = ((String) requestMap.get("parameterConnectionTable"))
													.split(",");
											put("table", table);
										}
									});

								}
							});

						}
					});

				}
			};
		}else if (Constant.OUTPUT_TYPE_TXTFILEWRITER.equals(type)) {
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							String path = (String) requestMap.get("parameterPath");
							put("path", path);
							String fileName = (String) requestMap.get("parameterFileName");
							put("fileName", fileName);
							String writeMode = (String) requestMap.get("parameterWriteMode");
							put("writeMode", writeMode);
						}
					});

				}
			};
		}else if (Constant.OUTPUT_TYPE_HTTPWRITER.equals(type)) {
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							String path = (String) requestMap.get("parameterPath");
							put("path", path);
							String fileName = (String) requestMap.get("parameterFileName");
							put("fileName", fileName);
							String writeMode = (String) requestMap.get("parameterWriteMode");
							put("writeMode", writeMode);
						}
					});

				}
			};
		}else if (Constant.OUTPUT_TYPE_FTPWRITER.equals(type)) {
			return new HashMap<String, Object>() {
				{
					put("name", type);
					put("parameter", new HashMap<String, Object>() {
						{
							
							String protocol = (String) requestMap.get("parameterProtocol");
							put("protocol", protocol);
							String host = (String) requestMap.get("parameterHost");
							put("host", host);
							String port = (String) requestMap.get("parameterPort");
							put("port", port);
							String username = (String) requestMap.get("parameterUsername");
							put("username", username);
							String password = (String) requestMap.get("parameterPassword");
							put("password", password);		
							
							String path = (String) requestMap.get("parameterPath");
							put("path", path);
							String fileName = (String) requestMap.get("parameterFileName");
							put("fileName", fileName);
							String writeMode = (String) requestMap.get("parameterWriteMode");
							put("writeMode", writeMode);
						}
					});

				}
			};
		}
		return null;

	}

	
	
	public static List<HashMap<String, Object>> generateTransformerList(Map<String, Object> requestMap)
			throws IOException, JsonParseException, JsonMappingException {
		String type = (String) requestMap.get("type");
		
		if (  Constant.TRANSFORMER_TYPE_GROOVY.equals(type) ) {			
			return new ArrayList<HashMap<String, Object>>() {
				{
					add(new HashMap<String, Object>() {
						{
							put("name", type);
							put("parameter", new HashMap<String, Object>() {
								{
									String parameterCode = (String) requestMap.get("parameterCode");
									put("code", parameterCode);
								 

									String[] column = ((String) requestMap.get("parameterExtraPackage")).split("\n");
									put("extraPackage", column);

									 

								}
							});
						}
					});

				}
			};
		}
		return Collections.EMPTY_LIST;
					
				 
	}

 
	@RequestMapping(value = "/api/job/updateJob")
	public Map<String, Object> updateJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		// name不允许修改
		String id = (String) requestMap.get("id");
		Map<String, Object> map = jobDao.getJob(id);
				
		//生成datax格式的reader
		Map<String, Object> readerMap = null;
		String inputId = (String) requestMap.get("inputId");
		if (StringUtils.isEmpty(inputId)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "输入流不能为空");
					put("data", null);
				}
			};
		}		
		Map<String, Object> inputMap = inputDao.getInput(inputId);
		requestMap.put("inputName", inputMap.get("name"));
		requestMap.put("inputType", inputMap.get("type"));
		String inputType = (String)inputMap.get("type");
		if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_MYSQLREADER)) {
			readerMap = generateReaderMap(mysqlReaderDao.getMysqlReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_TXTFILEREADER)) {
			readerMap = generateReaderMap(txtFileReaderDao.getTxtFileReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_HTTPREADER)) {
			readerMap = generateReaderMap(httpReaderDao.getHttpReader(inputId));		
		}else if(inputType.equalsIgnoreCase(Constant.INPUT_TYPE_FTPREADER)) {
			readerMap = generateReaderMap(ftpReaderDao.getFtpReader(inputId));		
		}

		//生成datax格式的writer
		Map<String, Object> writerMap = null;
		String outputId = (String) requestMap.get("outputId");
		if (StringUtils.isEmpty(inputId)) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "输出流不能为空");
					put("data", null);
				}
			};
		}		
		Map<String, Object> outputMap = outputDao.getOutput(outputId);
		requestMap.put("outputName", outputMap.get("name"));
		requestMap.put("outputType", outputMap.get("type"));
		String outputType = (String)outputMap.get("type");
		if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_MYSQLWRITER)) {
			writerMap = generateWriterMap(mysqlWriterDao.getMysqlWriter(outputId));	
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_TXTFILEWRITER)) {
			writerMap = generateWriterMap(txtFileWriterDao.getTxtFileWriter(outputId));		
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_HTTPWRITER)) {
			writerMap = generateWriterMap(httpWriterDao.getHttpWriter(outputId));		
		}else if(outputType.equalsIgnoreCase(Constant.OUTPUT_TYPE_FTPWRITER)) {
			writerMap = generateWriterMap(ftpWriterDao.getFtpWriter(outputId));		
		}
		
		//生成datax格式的transformer
		List<HashMap<String, Object>> transformerList = new ArrayList<>();
		String transformerId = (String) requestMap.get("transformerId");
		Map<String, Object> transformerMap = transformerDao.getTransformer(transformerId);
		requestMap.put("transformerName", transformerMap.get("name"));
		requestMap.put("transformerType", transformerMap.get("type"));
		if(transformerId!=null) {
			transformerList = generateTransformerList(transformerMap);		 
		}		
		String dataxJson = objectMapper.writeValueAsString(generateDataxMap(readerMap,writerMap,transformerList));
		requestMap.put("dataxJson", dataxJson);
		
		map.putAll(requestMap);
		jobDao.updateJob(map);
		
		//1TODO 这部分使用异步处理
		 //不能对任务名称继续更新		
		String name=(String)map.get("name");
		File file = new File(Constant.DATAX_HOME + File.separator + "job" + File.separator + name + ".json");
		FileUtils.writeStringToFile(file, dataxJson, "UTF-8");
		//
		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
 
	@RequestMapping(value = "/api/job/deleteJob")
	public Map<String, Object> deleteJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 

		String id = (String) requestMap.get("id");
		//
		try {//1TODO 这部分使用异步处理
		Map<String, Object> map = jobDao.getJob(id);
		String name=(String)map.get("name");
		ResponseEntity<String> response = restTemplate.exchange(String.format("%s/job/%s/doDelete", jenkinsUrl, name), HttpMethod.POST,
				new HttpEntity<>(new HttpHeaders() {
					{
						set("Content-Type", "text/xml; charset=UTF-8");

					}
				}), String.class);
		log.info(response.getBody());
		}catch (Exception e) {
			log.error("",e);
		}
		//
		jobDao.deleteJob(id);		
		buildDao.deleteBuildByJobId(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}

 
	@RequestMapping(value = "/api/job/runJob")
	public Map<String, Object> runJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = (String) requestMap.get("id");
		Map<String, Object> jobMap = jobDao.getJob(id);
		String name = (String) jobMap.get("name");

		Integer status = (Integer) jobMap.get("status");
		if (status == Constant.JOB_STATUS_DISABLE) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "构建失败,任务停用中");
					put("data", null);
				}
			};
		}
		if (status == Constant.JOB_STATUS_RUNNING) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "构建失败,任务正在构建中");
					put("data", null);
				}
			};
		}
 
	   //向jenkins发送构建请求
		ResponseEntity<String> response = restTemplate.exchange(String.format("%s/job/%s/build", jenkinsUrl, name),
				HttpMethod.POST, new HttpEntity<>(new HttpHeaders() {
					{
						set("Content-Type", "text/xml; charset=UTF-8");

					}
				}), String.class);
		
		//写入构建日志
		String currentBuildNumber = (String) jobMap.get("nextBuildNumber");
		Map<String, Object> buildMap = new HashMap<>();
		buildMap.put("id", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		buildMap.put("name", currentBuildNumber);
		buildMap.put("jobId", jobMap.get("id"));
		buildMap.put("jobName", jobMap.get("name"));
		buildMap.put("number", currentBuildNumber);		
		buildMap.put("status", Constant.BUILD_STATUS_RUNNING);	
		buildMap.put("progress", 0.0);
 
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		buildMap.put("createTime", createTime);		
		buildDao.saveBuild(buildMap);
		
		//更新计划任务
		jobMap.put("lastBuildCreateTime", createTime);
		jobMap.put("lastBuildProgress",0.0);
		jobMap.put("lastBuildId", buildMap.get("id"));
		jobMap.put("lastBuildNumber", currentBuildNumber);
		jobMap.put("nextBuildNumber", Integer.parseInt(currentBuildNumber)+1+"");
		jobMap.put("status", Constant.JOB_STATUS_RUNNING);
		jobDao.updateJob(jobMap);
		
		//异步更新计划任务和构建日志START
		String jobId=(String)jobMap.get("id");
		String jobName=(String)jobMap.get("name");	 
		 new Thread(){
			 public void run() {
				 updateJobAndBuild( jobId, jobName, currentBuildNumber);			 }
		 }.start();
		//异步更新计划任务和构建日志END
		 
		 
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "构建成功");
				put("data", null);
			}
		};
	}

	
	public void updateJobAndBuild(String jobId,String jobName,String currentBuildNumber) {
		// 不断的更新mysql
				while (true) {
					try {

						

						// getBuild-1请求
						ResponseEntity<Map> response3 = restTemplate.exchange(
								String.format("%s/job/%s/%s/api/json?pretty=true", jenkinsUrl, jobName, currentBuildNumber),
								HttpMethod.GET, new HttpEntity<>(new HttpHeaders() {
									{
										set("Content-Type", "text/xml; charset=UTF-8");

									}
								}), Map.class);
						Map<String, Object> responseMap = (Map<String, Object>) response3.getBody();
						Map<String, Object> buildMap = buildDao.getBuildByJobNameAndNumber(jobName, currentBuildNumber);
						buildMap.put("jobId", jobId);
						buildMap.put("jobName", jobName);
						buildMap.put("number", currentBuildNumber);
						// 是否构建中
						boolean building = (boolean) responseMap.get("building");
						buildMap.put("status", building == false ? Constant.BUILD_STATUS_EXIT : Constant.BUILD_STATUS_RUNNING);
						// 预期构建时长
						int estimatedDuration=(Integer) responseMap.get("estimatedDuration");
						buildMap.put("estimatedDuration", estimatedDuration);
						
												
						// 实际构建时长
						buildMap.put("duration", (Integer) responseMap.get("duration"));
						String result = (String) responseMap.get("result");
						if (result == null) {
							buildMap.put("result", null);
						} else if (result.equals("SUCCESS")) {
							buildMap.put("result", Constant.BUILD_RESULT_SUCCESS);
						} else if (result.equals("FAILURE")) {
							buildMap.put("result", Constant.BUILD_RESULT_FAILURE);
						} else if (result.equals("ABORTED")) {
							buildMap.put("result", Constant.BUILD_RESULT_ABORTED);
						}
						
						// 更新任务执行进度
						double progress = 0.0;
						if (building == false && result != null) {
							progress=1.0;// 已经构建完毕
						}else {
							DecimalFormat df=new DecimalFormat("0.00");
							long duration = System.currentTimeMillis()-new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)buildMap.get("createTime")).getTime();
							progress=
									Double.parseDouble(df.format(
											 (float)duration/estimatedDuration
											));	
							if(progress>1) progress=0.99;
						}						
						buildMap.put("progress", progress);
						
					
						// getBuild-2请求
						ResponseEntity<String> response4 = restTemplate.exchange(
								String.format("%s/job/%s/%s/consoleText", jenkinsUrl, jobName, currentBuildNumber), HttpMethod.GET,
								new HttpEntity<>(new HttpHeaders() {
									{
										set("Content-Type", "text/xml; charset=UTF-8");

									}
								}), String.class);
						//控制台日志
						String consoleText = response4.getBody();	
						buildMap.put("consoleText", consoleText);
						buildDao.updateBuild(buildMap);

						// 获取job的最后一次构建状态
						 response3 = restTemplate.exchange(
								String.format("%s/job/%s/api/json?pretty=true", jenkinsUrl, jobName), HttpMethod.GET,
								new HttpEntity<>(new HttpHeaders() {
									{
										set("Content-Type", "text/xml; charset=UTF-8");

									}
								}), Map.class);

						responseMap = (Map<String, Object>) response3.getBody();
						
						Integer lastBuildNumber = (Integer) ((Map<String, Object>) responseMap.get("lastBuild")).get("number");

						Map<String, Object> jobMap = jobDao.getJob(jobId);
						//最后一次构建编号
						jobMap.put("lastBuildNumber", lastBuildNumber);
						//最后一次成功构建编号
						jobMap.put("lastSuccessfulBuildNumber", responseMap.get("lastSuccessfulBuildNumber") == null ? null
								: ((Map<String, Object>) responseMap.get("lastSuccessfulBuildNumber")).get("number"));
						//最后一次失败构建编号
						jobMap.put("lastUnsuccessfulBuildNumber", responseMap.get("lastUnsuccessfulBuildNumber") == null ? null
								: ((Map<String, Object>) responseMap.get("lastUnsuccessfulBuildNumber")).get("number"));
						//下一次构建编号
						jobMap.put("nextBuildNumber", responseMap.get("nextBuildNumber"));
						jobMap.put("status", building == false ? Constant.JOB_STATUS_ENABLE : Constant.JOB_STATUS_RUNNING);
						jobMap.put("lastBuildEstimatedDuration", estimatedDuration);
						//更新任务执行进度
						jobMap.put("lastBuildProgress", progress);
						
						boolean end = false;
						if (building == false && result != null) {
							if (result.equals("SUCCESS")) {
								jobMap.put("lastSuccessfulBuildNumber", lastBuildNumber);
							} else if (result.equals("FAILURE")) {
								jobMap.put("lastUnsuccessfulBuildNumber", lastBuildNumber);
							}
							end = true;
						}
						jobDao.updateJob(jobMap);//1TODO 增加字段，最后一次任务的执行结果lastBuildResult
						
						if (building == false && result != null) {
							break;// 已经构建完毕
						}
						
						try {
							Thread.sleep(5000);
						} catch (Exception e) {
							 
						}
					} catch (Exception e) {
						log.error("", e);
						try {
							Thread.sleep(5000);
						} catch (Exception e2) {
							 
						}
						continue;		
						
					}
					
				}
	}

  

	
 
	@RequestMapping(value = "/api/job/stopJob")
	public Map<String, Object> stopJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = (String) requestMap.get("id");
		
		Map<String, Object> map = jobDao.getJob(id);
		String name = (String) map.get("name");
		String lastBuildNumber = (String)map.get("lastBuildNumber");

		Integer status = (Integer) map.get("status");
		if (status == Constant.JOB_STATUS_DISABLE) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "停止失败,任务停用中");
					put("data", null);
				}
			};
		}
		 
		if(status==Constant.JOB_STATUS_ENABLE) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "任务已经构建完毕，无法停止");
					put("data", null);
				}
			};
		}
		//1TODO取消队列	
		//停止
		ResponseEntity<String> response  =  restTemplate.exchange(String.format("%s/job/%s/%s/stop", jenkinsUrl, name,lastBuildNumber),
				HttpMethod.POST, new HttpEntity<>(new HttpHeaders() {
					{
						set("Content-Type", "text/xml; charset=UTF-8");

					}
				}), String.class);	
		log.info(""+response.getStatusCode());
		
		//更新job
	    map.put("status", Constant.JOB_STATUS_ENABLE);
	    jobDao.updateJob(map);

		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "停止成功");
				put("data", null);
			}
		};
	}

	 
	@RequestMapping(value = "/api/job/enableJob")
	public Map<String, Object> enableJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = (String) requestMap.get("id");
		Map<String, Object> map = jobDao.getJob(id);
		
		Integer status = (Integer) map.get("status");
		if (status == Constant.JOB_STATUS_ENABLE) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "启用失败,任务启用中");
					put("data", null);
				}
			};
		}
		
		map.put("status", Constant.JOB_STATUS_ENABLE);
		jobDao.updateJob(map);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "启用成功");
				put("data", null);
			}
		};
	}

	 
	@RequestMapping(value = "/api/job/disableJob")
	public Map<String, Object> disableJob(@RequestParam Map<String, Object> requestMap) throws Exception {
		 
		String id = (String) requestMap.get("id");
		Map<String, Object> map = jobDao.getJob(id);
		
		Integer status = (Integer) map.get("status");
		if (status == Constant.JOB_STATUS_DISABLE) {
			return new HashMap<String, Object>() {
				{
					put("code", HttpStatus.BAD_REQUEST.value());
					put("msg", "禁用失败,任务停用中");
					put("data", null);
				}
			};
		}
		
		map.put("status", Constant.JOB_STATUS_DISABLE);
		jobDao.updateJob(map);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "停用成功");
				put("data", null);
			}
		};
	}

}
