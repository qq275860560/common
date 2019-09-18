package com.ctbiyi.dataxweb.controller;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.qq275860560.common.util.RequestUtil;
import com.github.qq275860560.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;
 
@RestController
@Slf4j
public class UserController {

	@Value("${loginUrl}")
	private String loginUrl;
	 
	@RequestMapping(value = "/getLoginUrl")
	public Map<String, Object> getLoginUrl()  throws Exception{		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取loginUrl成功");
				put("data", loginUrl);
			}
		}; 
	}
	@Value("${logoutUrl}")
	private String logoutUrl;
	 
	@RequestMapping(value = "/getLogoutUrl")
	public Map<String, Object> getLogoutUrl()  throws Exception{		
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取logoutUrl成功");
				put("data", logoutUrl);
			}
		}; 
	}
    
	@RequestMapping(value = "/login")
	public void login(HttpServletResponse response,@RequestParam(required=false) String ticket )  throws Exception{
		log.info(""+ticket);
		String result= null;
		if(ticket!=null) {
		result="<html>\r\n" + 
				"<head>\r\n" + 
				"<script>\r\n" + 
				"localStorage.setItem(\"access_token\",\""+ticket+"\" );\r\n" + 
				"localStorage.setItem(\"token_type\",\"Bearer\" );\r\n" + 
				"window.location.href=\"/\";\r\n" + 
				"</script>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"</body>\r\n" + 
				"</html>";
		}else {
			result="<html>\r\n" + 
					"<head>\r\n" + 
					"<script>\r\n" + 
					"localStorage.clear();\r\n" + 
					"window.location.href=\"/\";\r\n" + 
					"</script>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"</body>\r\n" + 
					"</html>";
		}
			
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
		
	}
	//http://localhost:8045/login?ticket=tttt
	//https://castest.ctbiyi.com/cas/login?service=http%3A%3A%2F%2Flocalhost%3A8045%3A8045%2Flogin
	//https://castest.ctbiyi.com/cas/login?service=http%3a%2f%2flocalhost%3a8045%2flogin
	//admin
	//ctsiadmin2018
	
	/*  curl -i -X POST "http://localhost:8045/api/user/pageUser?pageNum=1&pageSize=10" -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw" 
	 *  curl -i -X POST "http://username1:password1@localhost:8045/api/user/pageUser?pageNum=1&pageSize=10" 
	*/
	@RequestMapping(value = "/api/user/pageUser")
	public Map<String, Object> pageUser(@RequestParam(required=false) Integer pageNum,@RequestParam(required=false) Integer pageSize)  throws Exception{
		 		//模拟数据库执行
		Map<String, Object> data = new HashMap<String,Object>() {{			
			put("total", 2);
			put("pageList", Arrays.asList(new HashMap<String, Object>() {
				{
					put("userId", "1");
					put("username", "admin");
					put("roleNames", "ROLE_ADMIN");
				}
			}, new HashMap<String, Object>() {
				{
					put("userId", "2");
					put("username", "admin2");
					put("roleNames", "ROLE_ADMIN");
				}
			}));
		}};;
		return new HashMap<String, Object>() {
			{
				 
				put("code", HttpStatus.OK.value());//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	
	/* curl -i -X POST "http://localhost:8045/api/user/listUser" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw" 	 
	*/
	@RequestMapping(value = "/api/user/listUser")
	public Map<String, Object> listUser(@RequestParam(required=false) String username )  throws Exception{
	 		//模拟数据库执行
		List<Map<String, Object>> data = Arrays.asList(new HashMap<String, Object>() {
			{
				put("userId", "1");
				put("username", "admin");
				put("roleNames", "ROLE_ADMIN");
			}
		}, new HashMap<String, Object>() {
			{
				put("userId", "2");
				put("username", "admin2");
				put("roleNames", "ROLE_ADMIN");
			}
		});
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取列表成功");
				put("data", data);
			}
		};
	}
	
	/*  curl -i -X POST "http://localhost:8045/api/user/getUser?id=1" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw"
	 *  
	 */
 	@RequestMapping(value = "/api/user/getUser")
	public Map<String, Object> getUser(@RequestParam(required=true) String id)  throws Exception{
		 		//模拟数据库执行
		Map<String, Object> data=new HashMap<String, Object>() {
			{
				put("userId", id);
				put("username", "username" + id);
				put("roleNames", "ROLE_ADMIN");
			}
		};
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
	/* curl -i -X POST "http://localhost:8045/api/user/saveUser?username=admin2" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw"
	   
	*/
	@RequestMapping(value = "/api/user/saveUser")
	public Map<String, Object> saveUser(@RequestParam(required=true) String username)  throws Exception{
	 		//模拟数据库执行
		log.info("数据库执行");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	
	
	/* curl -i -X POST "http://localhost:8045/api/user/updateUser?username=admin2"  -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw" 
	   
	*/
	@RequestMapping(value = "/api/user/updateUser")
	public Map<String, Object> updateUser(
			@RequestParam(required=false) String username)  throws Exception{
		 		//模拟数据库执行
		log.info("数据库执行");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
	/* curl -i -X POST "http://localhost:8045/api/user/deleteUser?id=1" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw" 
	   curl -i -X POST "http://username1:password1@localhost:8045/api/user/deleteUser?id=1"
	*/
 	@RequestMapping(value = "/api/user/deleteUser")
	public Map<String, Object> deleteUser(
			@RequestParam(required=true) String id)  throws Exception{
		 		//模拟数据库执行
		log.info("数据库执行");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	
	
	
}
