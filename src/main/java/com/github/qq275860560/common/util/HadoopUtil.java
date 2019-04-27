package com.github.qq275860560.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
/**
 * @author jiangyuanlin@163.com
 *
 */
public class HadoopUtil {

	public static void main8(String[] args) throws Exception {

		// 配置读取，读取默认配置
		Configuration conf = new Configuration();
		//conf.set("dfs.client.use.datanode.hostname", "true");
		// hdfs客户端对象获取
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://GITHUB-QQ275860560-hadoop:8020"), conf, "root");
		// 从hdfs系统下载文件到本地
		fileSystem.copyToLocalFile(new Path("/input/words.txt"), new Path("d:/words.txt"));
		fileSystem.close();

	}

	public static void main(String[] args) throws Exception {

		// 配置读取，读取默认配置
		Configuration conf = new Configuration();
		conf.set("dfs.client.use.datanode.hostname", "true");

		// hdfs客户端对象获取
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://GITHUB-QQ275860560-hadoop:8020"), conf, "root");
		// 从本地上传到hdfs系统 
		fileSystem.mkdirs(new Path("/input/"));
		fileSystem.copyFromLocalFile(
				new Path("D:/workspace/GITHUB-QQ275860560-web/src/main/resources/application.properties"),
				new Path("/input/test3.txt"));
		 
		fileSystem.close();

	}

	public static void main2(String[] args) throws Exception{
		// 配置读取，读取默认配置
		Configuration conf = new Configuration();
		//conf.set("dfs.client.use.datanode.hostname", "true");
		FileSystem fileSystem = FileSystem.get(new URI("hdfs:github-build-hadoop:8020"), conf, "root");
		// 创建文件夹
		try {
			fileSystem.mkdirs(new Path("/springhdfs/"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		Path newPath = new Path("/springhdfs/test2.txt");

		// 文件信息查看
		try {
			RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(newPath, true);
			while (listFiles.hasNext()) {
				LocatedFileStatus file = listFiles.next();
				System.out.println(file.getPath().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FSDataInputStream in = fileSystem.open(newPath);
			FileOutputStream out = new FileOutputStream(
					"D:/workspace/GITHUB-QQ275860560-web/src/main/resources/test.txt");
			IOUtils.copyBytes(in, out, new Configuration());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FSDataOutputStream outputStream = fileSystem.create(new Path("/springhdfs/application.properties"));
			FileInputStream inputStream = new FileInputStream(
					"D:/workspace/GITHUB-QQ275860560-web/src/main/resources/application1.properties");
			IOUtils.copyBytes(inputStream, outputStream, new Configuration());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 创建文件
		try {
			FSDataOutputStream outputStream = fileSystem.create(newPath);
			outputStream.write("12345".getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 读取文件
		try {
			InputStream in = fileSystem.open(newPath);
			String result = org.apache.commons.io.IOUtils.toString(in, "utf-8");
			System.out.println(result);
			IOUtils.closeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (fileSystem != null)
			try {
				fileSystem.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

}
