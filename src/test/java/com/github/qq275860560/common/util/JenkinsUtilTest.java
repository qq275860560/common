package com.github.qq275860560.common.util;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.github.qq275860560.common.util.JenkinsUtil;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class JenkinsUtilTest {
	private static Log log = LogFactory.getLog(JenkinsUtilTest.class);

	@Value("${jenkins_url}")
	private String jenkins_url;
	@Value("${jenkins_username}")
	private String jenkins_username;
	@Value("${jenkins_password}")
	private String jenkins_password;

	 
	@Ignore
	@Test
	public void checkJobName() throws Exception{
		 boolean b = JenkinsUtil.exisitJob(jenkins_url, jenkins_username,jenkins_password , "com.github.edocker_github-edocker-plan"); 
		 log.info(b);	
	}

	@Ignore
	@Test
	public void saveJob() throws Exception {
		String jobName = "test";
		String svn_url = "https://192.168.115.56:9000/svn/e-docker/trunk/github-edocker-plan";
		String svn_user_name="jiangyuanlin";
		String svn_password="123456";;
		
		String shell=" ls -l \n cd /tmp \n ls -l";
		JenkinsUtil.saveJob( jenkins_url, jenkins_username,jenkins_password ,  jobName,  svn_url,  svn_user_name,  svn_password ) ;
	}
	
	@Ignore
	@Test
	public void getCredentials() throws Exception{
		  JenkinsUtil.getCredentials( jenkins_url, jenkins_username,jenkins_password ,"mmmmm"+"_credentials"); 
		 	
	}
	@Ignore
	@Test
	public void exisitCredentials() throws Exception{
		  boolean b = JenkinsUtil.exisitCredentials(jenkins_url, jenkins_username,jenkins_password , "mmmmm"+"_credentials"); 
		 	log.info(b);
	}
	@Ignore
	@Test
	public void getJobLastBuild() throws Exception{
		  int result = JenkinsUtil.getJobLastBuild( jenkins_url, jenkins_username,jenkins_password ,"github-edocker-plan"); 
		 	log.info(result);
	}
	@Ignore
	@Test
	public void getJobLastSuccessBuild() throws Exception{
		Integer result = JenkinsUtil.getJobLastSuccessBuild(jenkins_url, jenkins_username,jenkins_password , "github-edocker-plan"); 
		 	log.info(result);
	}
	@Ignore
	@Test
	public void getJobLastFailBuild() throws Exception{
		  Integer  result = JenkinsUtil.getJobLastFailBuild( jenkins_url, jenkins_username,jenkins_password ,"github-edocker-plan"); 
		 	log.info(result);
	}
	@Ignore
	@Test
	public void getJobBuild() throws Exception{
		  String  result = JenkinsUtil.getJobBuild( jenkins_url, jenkins_username,jenkins_password ,"github-edocker-plan"); 
		 	log.info(result);
	}
	@Ignore
	@Test
	public void getBuild() throws Exception{
		  Map<String, Object>  result = JenkinsUtil.getBuild(jenkins_url, jenkins_username,jenkins_password , "github-edocker-plan",140); 
		 	log.info(result);
	}

	@Ignore
	@Test
	public void getJob() throws Exception{
		  String  result = JenkinsUtil.getJob(jenkins_url, jenkins_username,jenkins_password , "github-edocker-plan"); 
		 	log.info(result);
	}
	
	@Ignore
	@Test
	public void getJobBuildConsoleText() throws Exception{
		  String  result = JenkinsUtil.getJobBuildConsoleText( jenkins_url, jenkins_username,jenkins_password ,"github-edocker-plan",143); 
		 	log.info(result);
	}
	
	
}
