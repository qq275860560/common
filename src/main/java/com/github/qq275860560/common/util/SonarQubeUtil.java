package com.github.qq275860560.common.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class SonarQubeUtil {
	private static Log log = LogFactory.getLog(SonarQubeUtil.class);

	private SonarQubeUtil() {
	}

	 

	private static String url ;
	private static String username  ;
	private static String password  ;
	
	
	
	static {
		try {
			Configuration configuration = new Configurations()
					.properties(new File("/", "sonarqube.properties"));
			url = configuration.getString("url"); 		 
			username = configuration.getString("username");	
			password = configuration.getString("password");			
		} catch (Exception e) {
			log.error("", e);
			System.exit(1);// 配置不准确，直接退出
		}
	}

	public static void main (String[] args)  throws Exception{
		
	}
	public   static String getUrl(String componentKey) {
		String fmt = "%s/dashboard?id=%s";	
		return String.format(fmt, url,componentKey );
	}
	 
	public   static String getUrl(String sonarUrl,String componentKey) {
		String fmt = "%s/dashboard?id=%s";	
		return String.format(fmt, sonarUrl,componentKey );
	}
	
	
	public   static String listTest() {
		String fmt = "%s/api/measures/search";
		String urlString = String.format(fmt, url);
		urlString+="?projectKeys=com.github.qq275860560%3Agithub-qq275860560%2Ccom.github.qq275860560%3Agithub-qq275860560%2Ccom.github.edocker%3Agithub-edocker-plan&metricKeys=alert_status%2Cbugs%2Creliability_rating%2Cvulnerabilities%2Csecurity_rating%2Ccode_smells%2Csqale_rating%2Cduplicated_lines_density%2Ccoverage%2Cncloc%2Cncloc_language_distribution";
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	public   static String showTest() {
		String fmt = "%s/api/tests/show";
		String urlString = String.format(fmt, url);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	public   static Map<String, Object> measuresComponent(String componentKey) {
		Map<String, Object> resultMap = new HashMap<>();
		
		String fmt = "%s/api/measures/component?additionalFields=periods&componentKey=%s&metricKeys=new_technical_debt,blocker_violations,bugs,burned_budget,business_value,classes,code_smells,cognitive_complexity,comment_lines,comment_lines_density,branch_coverage,new_branch_coverage,conditions_to_cover,new_conditions_to_cover,confirmed_issues,coverage,new_coverage,critical_violations,complexity,directories,duplicated_blocks,new_duplicated_blocks,duplicated_files,duplicated_lines,duplicated_lines_density,new_duplicated_lines,new_duplicated_lines_density,effort_to_reach_maintainability_rating_a,false_positive_issues,files,functions,generated_lines,generated_ncloc,info_violations,violations,line_coverage,new_line_coverage,lines,ncloc,lines_to_cover,new_lines_to_cover,sqale_rating,new_maintainability_rating,major_violations,minor_violations,new_blocker_violations,new_bugs,new_code_smells,new_critical_violations,new_info_violations,new_violations,new_lines,new_major_violations,new_minor_violations,new_vulnerabilities,open_issues,projects,alert_status,reliability_rating,new_reliability_rating,reliability_remediation_effort,new_reliability_remediation_effort,reopened_issues,security_rating,new_security_rating,security_remediation_effort,new_security_remediation_effort,skipped_tests,statements,team_size,sqale_index,sqale_debt_ratio,new_sqale_debt_ratio,uncovered_conditions,new_uncovered_conditions,uncovered_lines,new_uncovered_lines,test_execution_time,test_errors,test_failures,test_success_density,tests,vulnerabilities,wont_fix_issues";
		String urlString = String.format(fmt, url,componentKey);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		String result = HttpConnectionUtil.send(urlString, "GET", headerMap, null);
		if(result==null) {
			resultMap.put("errorMsg","sonar没有配置");
			return resultMap;
		}
		List<Map<String, Object>> list =  (List<Map<String, Object>>)(((Map<String, Object> )(JsonUtil.parse(result,Map.class).get("component"))).get("measures"));
		 
		for(Map<String, Object> map:list){
			resultMap.put((String)map.get("metric"),map.get("value"));
		}		
		return resultMap;
	}
	
	public   static String showComponents(String componentKey) {
		String fmt = "%s/api/components/show?component=%s";
		String urlString = String.format(fmt, url,componentKey);
		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Basic " + encoding);		
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");		
		return HttpConnectionUtil.send(urlString, "GET", headerMap, null);
	}
	
	
	
	
}

 