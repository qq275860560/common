package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * @author jiangyuanlin@163.com
 */
public class MergeFileTest {
	private static Logger log = LoggerFactory.getLogger(MergeFileTest.class);

	public static void main(String[] args)
			throws Exception  {
		
		File srcDir = new File(MergeFileTest.class.getResource("/ps").getFile());
		File[] srcFiles = srcDir.listFiles();
		Map<Integer, File> map = new HashMap<>();
		for(File srcFile:srcFiles){
			String path = srcFile.getAbsolutePath();
			int index = path.lastIndexOf("-");
			int endIndex = path.lastIndexOf(".");
			log.info(""+Integer.parseInt(path.substring(index+1,endIndex)));
			map.put(Integer.parseInt(path.substring(index+1,endIndex)), srcFile);
		 
		}
 
		
		File targetFile = new File(MergeFileTest.class.getResource("/").getFile(),"target.dat");
		OutputStream outputStream = new FileOutputStream(targetFile);
		for(int i=0;i<map.size();i++){
			log.info(map.get(i+1).getAbsolutePath());
			outputStream.write(FileUtils.readFileToByteArray(map.get(i+1)));
		 
		}
		outputStream.close();
		 
		
		
	}
 
}