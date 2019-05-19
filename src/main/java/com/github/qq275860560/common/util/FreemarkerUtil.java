package com.github.qq275860560.common.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 * Freemarker 模版渲染工具
 */
@Slf4j
public class FreemarkerUtil {

	public static void main1(String[] args) {
		// 模板字符串->目标字符串
		String templateString = "hello ${msg}";
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("msg", "world");
			}
		};
		String destString = generateString(templateString, map);
		log.info("destString=" + destString);// 最终打印hello world
	}

	public static void main2(String[] args) throws Exception {
		// 模板字符串->目标文件
		String templateString = "hello ${msg}";
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("msg", "world");
			}
		};
		File destFile = new File("c:/2.txt");
		generateFile(templateString, map, destFile);// 最终文件内容hello world

	}

	public static void main3(String[] args) throws Exception {
		// 模板文件->目标字符串
		File templateFile = new File("c:/1.txt");
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("msg", "world");
			}
		};
		String destString = generateString(templateFile, map);
		log.info("destString=" + destString);// 最终打印hello world

	}

	public static void main(String[] args) throws Exception {
		// 模板文件->目标文件
		File templateFile = new File("c:/1.txt");
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("msg", "world");
			}
		};
		File destFile = new File("c:/2.txt");
		generateFile(templateFile, map, destFile);// 最终文件内容hello world

	}

	//模板字符串->目标字符串	 
	public static String generateString(String templateString, Map<String, Object> map) {
		try (StringWriter writer = new StringWriter();) {
			Template t = new Template("name", new StringReader(templateString),
					new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
			t.process(map, writer);
			return writer.toString();
		} catch (Exception e) {
			log.error("", e);

		}
		return null;
	}

    //模板字符串->目标文件	 
	public static void generateFile(String templateString, Map<String, Object> map, File destFile) throws IOException {
		String destString = generateString(templateString, map);
		FileUtils.writeStringToFile(destFile, destString, "UTF-8");

	}

	///模板文件->目标字符串	 
	public static String generateString(File templateFile, Map<String, Object> map) throws Exception {
		String sourceString = FileUtils.readFileToString(templateFile, "UTF-8");
		return generateString(sourceString, map);
	}

	//模板文件->目标文件	 
	public static void generateFile(File templateFile, Map<String, Object> map, File destFile) throws Exception {
		String sourceString = FileUtils.readFileToString(templateFile, "UTF-8");
		String destString = generateString(sourceString, map);
		FileUtils.writeStringToFile(destFile, destString, "UTF-8");
	}

}
