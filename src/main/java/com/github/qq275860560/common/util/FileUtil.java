package com.github.qq275860560.common.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jiangyuanlin@163.com
 */
public class FileUtil {
	private static Log log = LogFactory.getLog(FileUtil.class);
 

	private FileUtil() {
	}

	public static File getFileFromClassPath(String path) throws Exception{
		ClassPathResource resource = new ClassPathResource(path);
		File destination = new File(FileUtils.getTempDirectoryPath()+"/"+path);
		
		//if(!destination.exists()){		 
			FileUtils.copyInputStreamToFile(resource.getInputStream(), destination);
		//}
		return destination;
		//return new ClassPathResource(path).getFile();
		//return new File(Thread.class.getResource("path").getFile());
	}
	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public static void copyFile(File srcFile, File destFile) throws Exception {
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * 复制文件到目标文件夹
	 * 
	 * @param srcFile
	 * @param destDir
	 * @throws Exception
	 */
	public static void copyFileToDirectory(File srcFile, File destDir) throws Exception {
		org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir);
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static List<String> readLines(File file, String encoding) throws Exception {
		return org.apache.commons.io.FileUtils.readLines(file, encoding);
	}

	/**
	 * 写入文件
	 * 
	 * @param file
	 * @param encoding
	 * @param lines
	 * @throws Exception
	 */
	public static void writeLines(File file, String encoding, Collection<?> lines) throws Exception {
		org.apache.commons.io.FileUtils.writeLines(file, encoding, lines);
	}
	
	/**
	 * 写入文件
	 * 
	 * @param file
	 * @param encoding
	 * @param lines
	 * @throws Exception
	 */
	public static void writeStringToFile(File file,String data, String encoding) throws Exception {
		org.apache.commons.io.FileUtils.writeStringToFile(file, data, encoding);
	}
	

	/**
	 * 网络流拷贝文件
	 * 
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public static void copyURLToFile(URL source, File destination) throws Exception {
		org.apache.commons.io.FileUtils.copyURLToFile(source, destination);
	}

	/**
	 * 获取前缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getPrefix(String filename) {
		return org.apache.commons.io.FilenameUtils.getPrefix(filename);
	}

	/**
	 * 获取后缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(File file) {
		return org.apache.commons.io.FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();
	}

	/**从输入流中读取制定起始位置和长度的字节
	 * @param inputStream
	 * @param offset
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static byte[] getFileContentByte(InputStream inputStream, long offset, long length) throws Exception {
		log.debug("getFileContentByte start");
		byte[] fileContent = null;
		byte[] tempBuf = new byte[(int) length];

		inputStream.skip(offset);
		int readLen = inputStream.read(tempBuf);
		if (readLen < 0) {
			fileContent = new byte[0];
			return fileContent;
		}
		if (readLen < length) {
			fileContent = new byte[readLen];
			System.arraycopy(tempBuf, 0, fileContent, 0, readLen);
		} else {
			fileContent = tempBuf;
		}
		inputStream.close();
		return fileContent;
	}

}
