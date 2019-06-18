package com.github.qq275860560.common.util;

/**
 * @author jiangyuanlin@163.com
 */
public class JenkinsUtil {

	private JenkinsUtil() {
	}

	public static String generateJenkinsJobXml(String command) {
		String builderNodeXml = null;
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
			builderNodeXml = "<hudson.tasks.BatchFile>      <command>" + command
					+ "</command>    </hudson.tasks.BatchFile>";
		} else {
			builderNodeXml = "<hudson.tasks.Shell>      <command>" + command + "</command>    </hudson.tasks.Shell>";
		}
		return "<?xml version='1.1' encoding='UTF-8'?><project>  <description></description>  <keepDependencies>false</keepDependencies>  <properties/>  <scm class=\"hudson.scm.NullSCM\"/>  <canRoam>true</canRoam>  <disabled>false</disabled>  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>  <triggers/>  <concurrentBuild>false</concurrentBuild>  <builders>"
				+ builderNodeXml + "</builders>  <publishers/>  <buildWrappers/></project>";
	}
}
