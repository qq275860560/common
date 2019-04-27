package com.github.qq275860560.common.util;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Computer;


/**
 * @author jiangyuanlin@163.com
 */
public class JenkinsUtil {
	private static Log log = LogFactory.getLog(JenkinsUtil.class);

	private JenkinsUtil() {
	}

	 
 

	public static void main22 (String[] args)  throws Exception{
		//String result = get(ip,port,username,password,"test222");
		//log.info("result="+result);
		//build(ip,port,username,password,"test");
		//String xml = FileUtils.readFileToString(new File("/","config.xml"));
		
		//String xml = FileUtils.readFileToString(new File(JenkinsUtil.class.getClassLoader().getResource("config.xml").getFile()));
		
		 
		//createItem(url, username, password, "test3", xml);
		
		//update(url, username, password, "test5", xml);
		
		
		//boolean b = exsit(url, username, password, "test277");
		//log.info(b);
		// deleteJob(url, username, password, "test3");
		//saveCredentials(  url, username,  password,"5555","jyl5","jyl56");
		//deleteCredentials(  url, username,  password,"1");
		//updateCredentials(  url, username,  password,"1","4444","4444");
		//getCredentials(  url, username,  password,"11111");
		// listCredentials(url, username, password );	 
	}

	
	public   static String getUrl(String url,String job_name) {
		String fmt = "%s/job/%s/";	
		return String.format(fmt, url,job_name );
	}
	
	
	public   static void saveCredentials( String url,String username,String password,String id,String credentials_username,String credentials_password) {
		String fmt = "%s/credentials/store/system/domain/_/createCredentials?json=%s";		
		Map<String, Object> credentials_map = new HashMap<>();
		credentials_map.put("scope", "GLOBAL");
		credentials_map.put("id",id);
		credentials_map.put("username", credentials_username);
		credentials_map.put("password", credentials_password);
		credentials_map.put("$class", "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl");
		credentials_map.put("description","");
		Map<String, Object> json_map = new HashMap<>();
		json_map.put("","0");
		json_map.put("credentials",credentials_map);
		String content =   JsonUtil.toJSONString(json_map) ;		
		String urlString = String.format(fmt, url,content );
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);
		log.info(result);
	}
	
	
	public   static void deleteCredentials( String url,String username,String password,String id ) {
		String fmt = "%s/credentials/store/system/domain/_/credential/%s/doDelete?json={}&Submit=Yes";		
		String urlString = String.format(fmt, url,id );
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);
	}
	
	
	public   static void updateCredentials( String url,String username,String password,String id,String credentials_username,String credentials_password) {
		String fmt = "%s/credentials/store/system/domain/_/credential/%s/updateSubmit?json=%s";		
		Map<String, Object> json_map = new HashMap<>();
		json_map.put("scope", "GLOBAL");
		json_map.put("id",id);
		json_map.put("username", credentials_username);
		json_map.put("password", credentials_password);
		json_map.put("stapler-class", "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl");
		json_map.put("description","");		 
		String content =   JsonUtil.toJSONString(json_map) ;			
		String urlString = String.format(fmt, url,id,content );
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);
	}
	
	
	public   static String getCredentials( String url,String username,String password,String id ) {
		String fmt = "%s/credentials/store/system/domain/_/credential/%s/";		
		String urlString = String.format(fmt, url,id );
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		if(HttpConnectionUtil.getResponseCode(urlString, "GET", headerMap, null)>=400)  return null;
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	public   static boolean exisitCredentials( String url,String username,String password,String id ) {
		return getCredentials(    url,  username,  password, id ) ==null ? false: true;
	}
	
	public   static void listCredentials(String url,String username,String password) {
		String fmt = "%s/credentials/store/system/domain/_/api/json";
		String urlString = String.format(fmt, url);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "text/xml; charset=UTF-8");		
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	
	public   static void saveJob( String url,String username,String password,String job_name,String xml) {
		String fmt = "%s/createItem?name=%s";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "text/xml; charset=UTF-8");		
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, xml);
	}
	
	public   static void saveJob( String url,String username,String password,String job_name,String svn_url,String svn_user_name,String svn_password  ) {
		if(exisitCredentials(url,username,password, job_name+"_credentials")){		
			updateCredentials(url,username,password,   job_name+"_credentials",svn_user_name,svn_password);
		}else{
			saveCredentials( url,username,password,  job_name+"_credentials" ,svn_user_name,svn_password);
		}
		
		
		//String xml = "<?xml version='1.1' encoding='UTF-8'?><project>  <keepDependencies>false</keepDependencies>  <properties/>  <scm class=\"hudson.scm.NullSCM\"/>  <canRoam>false</canRoam>  <disabled>false</disabled>  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>  <triggers/>  <concurrentBuild>false</concurrentBuild>  <builders/>  <publishers/>  <buildWrappers/></project>";
		String xml ="<?xml version='1.1' encoding='UTF-8'?><project>  <actions/>  <description></description>  <keepDependencies>false</keepDependencies>  <properties/>  <scm class=\"hudson.scm.SubversionSCM\" plugin=\"subversion@2.10.2\">    <locations>      <hudson.scm.SubversionSCM_-ModuleLocation>        <remote>"+svn_url+"</remote>        <credentialsId>"+ job_name+"_credentials"+"</credentialsId>        <local>.</local>        <depthOption>infinity</depthOption>        <ignoreExternalsOption>true</ignoreExternalsOption>      </hudson.scm.SubversionSCM_-ModuleLocation>    </locations>    <excludedRegions></excludedRegions>    <includedRegions></includedRegions>    <excludedUsers></excludedUsers>    <excludedRevprop></excludedRevprop>    <excludedCommitMessages></excludedCommitMessages>    <workspaceUpdater class=\"hudson.scm.subversion.UpdateUpdater\"/>    <ignoreDirPropChanges>false</ignoreDirPropChanges>    <filterChangelog>false</filterChangelog>    <quietOperation>true</quietOperation>  </scm>  <canRoam>true</canRoam>  <disabled>false</disabled>  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>  <triggers>    <hudson.triggers.SCMTrigger>      <spec>*/1 * * * *</spec>      <ignorePostCommitHooks>false</ignorePostCommitHooks>    </hudson.triggers.SCMTrigger>  </triggers>  <concurrentBuild>false</concurrentBuild>  <builders>    <hudson.tasks.Shell>      <command> <![CDATA[       source /etc/profile && BUILD_ID=DONTKILLME && echo '防止杀死子进程'&& chmod 777 ./run.sh  && sh ./run.sh && sleep 5s && echo '启动新进程成功'	]]>  </command>    </hudson.tasks.Shell>  </builders>  <publishers/>  <buildWrappers/></project>";

		if(!exisitJob( url,username,password,  job_name) ){
			saveJob( url,username,password, job_name, xml);
		}	
		updateJob(url,username,password, job_name,  xml);
	}
	
	public static void deleteJob ( String url,String username,String password,String job_name) {
		String fmt = "%s/job/%s/doDelete";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);

	}
	
	
	public static void updateJob(  String url,String username,String password,String job_name,String xml) {
		String fmt = "%s/job/%s/config.xml";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "text/xml; charset=UTF-8");		
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, xml);

	}
	
	

	
	

	
	public static String getJob( String url,String username,String password,String job_name) {
		String fmt="%s/job/%s/api/json?pretty=true&tree=builds[*]";
		//String fmt="%s/job/%s/api/json";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);
		return result;
	}
	public static boolean exsit( String url,String username,String password, String job_name) {
		return getJob(   url,  username,  password, job_name)!=null?true:false;
	}
	
	/**系统存在返回真
	 * @param url
	 * @param username
	 * @param password
	 * @param job_name
	 * @return
	 */
	public   static boolean exisitJob(  String url,String username,String password,String job_name) {
		String fmt = "%s/view/all/checkJobName?value=%s";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);		
		if(result !=null && result.contains("already exists")) return true;
		else return false;
		
	}
	
	
	public static void buildJob(  String url,String username,String password,String job_name) {
		String fmt = "%s/job/%s/build";
		String urlString = String.format(fmt, url, job_name);

		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);

	}
	
	
	public static void enableJob( String url,String username,String password, String job_name) {
		String fmt = "%s/job/%s/enable";
		String urlString = String.format(fmt, url, job_name);

		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);

	}
	
	public static void disableJob(  String url,String username,String password,String job_name) {
		String fmt = "%s/job/%s/disable";
		String urlString = String.format(fmt, url, job_name);

		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);
		String result = HttpConnectionUtil.send(urlString, "POST", headerMap, null);

	}
	

	
	public   static int getJobLastBuild(  String url,String username,String password,String job_name) {
		String fmt = "%s/job/%s/lastBuild/buildNumber";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);
		if(result==null)return 0;
		return  Integer.parseInt(result.trim());
	}
	
	public   static Integer getJobLastSuccessBuild( String url,String username,String password, String job_name) {
		String fmt = "%s/job/%s/lastStableBuild/buildNumber";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return  Integer.parseInt(HttpConnectionUtil.send(urlString, "GET", headerMap, null).trim());
	}
	
	public   static Integer getJobLastFailBuild( String url,String username,String password, String job_name) {
		String fmt = "%s/job/%s/lastUnsuccessfulBuild/buildNumber";
		String urlString = String.format(fmt, url, job_name);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return  Integer.parseInt(HttpConnectionUtil.send(urlString, "GET", headerMap, null).trim());
	}
	

	
	
	public   static Map<String, Object> getBuild( String url,String username,String password,String job_name,Integer build_number) {
		String fmt = "%s/job/%s/%s/api/json";
		String urlString = String.format(fmt, url,job_name,build_number);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);
		if(result==null)return Collections.EMPTY_MAP;
		return JsonUtil.parse(result,Map.class);
	}
	
	
	public   static String getJobBuild( String url,String username,String password,String job_name) {
		String fmt = "%s/queue/api/json";
		String urlString = String.format(fmt, url);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	
	public   static String getJobBuildConsoleText( String url,String username,String password,String job_name,Integer build_number) {
		String fmt = "%s/job/%s/%s/consoleText";
		String urlString = String.format(fmt, url,job_name, build_number);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	
	public static void main(String[] args) throws Exception{
		  JenkinsServer jenkinsServer = new JenkinsServer(new URI("http://132.122.237.68:8080/"), "admin", "123456");
		  
		  Map<String, Computer> map=jenkinsServer.getComputers();
		  log.info(JsonUtil.toJSONString(map));
		  jenkinsServer.getComputerSet();

	}
	
	public  static void doCreateItem(String name) {
	//curl -X POST 'http://132.122.237.68:8080/computer/doCreateItem'  -d 'name=HHHHHHHHHHHHHHHHHHHH&type=hudson.slaves.DumbSlave&json=%7B%22name%22%3A+%22HHHHHHHHHHHHHHHHHHHH%22%2C+%22nodeDescription%22%3A+%22%22%2C+%22numExecutors%22%3A+%221%22%2C+%22remoteFS%22%3A+%22%2Froot%2F.jenkins%22%2C+%22labelString%22%3A+%22HHHHHHHHHHHHHHHHHHHH%22%2C+%22mode%22%3A+%22NORMAL%22%2C+%22%22%3A+%5B%22hudson.slaves.JNLPLauncher%22%2C+%22hudson.slaves.RetentionStrategy%24Always%22%5D%2C+%22launcher%22%3A+%7B%22stapler-class%22%3A+%22hudson.slaves.JNLPLauncher%22%2C+%22%24class%22%3A+%22hudson.slaves.JNLPLauncher%22%2C+%22workDirSettings%22%3A+%7B%22disabled%22%3A+false%2C+%22workDirPath%22%3A+%22%22%2C+%22internalDir%22%3A+%22remoting%22%2C+%22failIfWorkDirIsMissing%22%3A+false%7D%2C+%22tunnel%22%3A+%22%22%2C+%22vmargs%22%3A+%22%22%7D%2C+%22retentionStrategy%22%3A+%7B%22stapler-class%22%3A+%22hudson.slaves.RetentionStrategy%24Always%22%2C+%22%24class%22%3A+%22hudson.slaves.RetentionStrategy%24Always%22%7D%2C+%22nodeProperties%22%3A+%7B%22stapler-class-bag%22%3A+%22true%22%7D%2C+%22type%22%3A+%22hudson.slaves.DumbSlave%22%7D&Submit=Save'  
	
	}
	
}

 