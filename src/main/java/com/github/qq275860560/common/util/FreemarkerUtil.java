package com.github.qq275860560.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author jiangyuanlin@163.com
 * Freemarker 模版引擎类
 */
public class FreemarkerUtil {

	private static Logger log = LoggerFactory.getLogger(FreemarkerUtil.class);

	public static void main(String[] args) throws Exception {
		File srcFile = new File(Thread.class.getResource("/callDevice.txt").getFile());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contentLength", 215);
		File destFile = new File("c:/444.txt");
		generateFile(srcFile,map,destFile);
		String result = generateString(srcFile,map);
		log.info(result);

	}

	public static void generateFile(File srcFile ,Map<String, Object> map,File destFile) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));	
		convertWriter(srcFile, map, writer);
	}
	
	public static String generateString(File srcFile ,Map<String, Object> map) throws IOException {
	 	Writer writer = new StringWriter();;
		convertWriter(srcFile, map, writer);
		return writer.toString();
	}
	 
	
	

	protected static void convertWriter(File srcFile, Map<String, Object> map, Writer writer) throws IOException {
		try {			
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			configuration.setDirectoryForTemplateLoading( srcFile.getParentFile()); 
			Template template = configuration.getTemplate(srcFile.getName());	 
			template.process(map, writer);
			writer.close();		 	 
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	   public static String renderString(String templateString, Map<String, ?> model) {
	        try {
	            StringWriter writer = new StringWriter();
	            Template t = new Template("name", new StringReader(templateString), new Configuration());
	            t.process(model, writer);
	            return writer.toString();
	        } catch (Exception e) {
	        	log.error("", e);
	        }
	        return null;
	    }

	 

	    public static Configuration buildConfiguration(String directory) throws IOException {
	        Configuration cfg = new Configuration();
	        Resource path = new DefaultResourceLoader().getResource(directory);
	        cfg.setDirectoryForTemplateLoading(path.getFile());
	        return cfg;
	    }

	
}
