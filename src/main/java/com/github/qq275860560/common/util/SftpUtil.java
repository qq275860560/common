package com.github.qq275860560.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class SftpUtil {

	public static ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			// jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
			log.error("", e);
		}
		return sftp;
	}

	public static void upload(String directory, String uploadFile, ChannelSftp channel) {
		try {
			creatDir(directory, channel);
			channel.cd(directory);
			File file = new File(uploadFile);
			channel.put(new FileInputStream(file), file.getName());
			sftpClose(channel);
		} catch (Exception e) {
			log.error("sftp upload exception:", e);
		} finally {
			if (channel != null) {
				sftpClose(channel);
			}
		}
	}

	public static void download(String directory, String downloadFilePath, String saveFile, ChannelSftp channel) {
		try {
			channel.cd(directory);
			File file = new File(saveFile);
			channel.get(downloadFilePath, new FileOutputStream(file));
		} catch (Exception e) {
			log.error("sftp download exception:", e);
		} finally {
			if (channel != null) {
				sftpClose(channel);
			}
		}
	}

	public static String downloadGetString(String directory, String downloadFile, String saveFile,
			ChannelSftp channelSftp) {
		try {
			channelSftp.cd(directory);
			File file = new File(saveFile);
			channelSftp.get(downloadFile, new FileOutputStream(file));
			return readFileByLines(file.getPath());
		} catch (Exception e) {
			log.error("sftp downloadGetString excetpin:", e);
		}
		return null;
	}

	public static String readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// 以行为单位读取文件内容，一次读一整行
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				sb.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			log.error("",e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("BufferedReader close exception:", e);
				}
			}
		}
		return sb.toString();
	}

	public static void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			log.error("",e);
		}
	}

	public static Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}

	public static void creatDir(String directory, ChannelSftp sftp) throws SftpException {
		String[] dirArr = directory.split("/");
		StringBuffer tempStr = new StringBuffer("");
		for (int i = 1; i < dirArr.length; i++) {
			tempStr.append("/" + dirArr[i]);
			try {
				sftp.cd(tempStr.toString());
			} catch (SftpException e) {
				sftp.mkdir(tempStr.toString());
			}
		}
	}

	public static void sftpClose(ChannelSftp channel) {
		try {
			channel.getSession().disconnect();
		} catch (JSchException e) {
			log.error("sftp disconnect exception:", e);
		}
	}

	public static String getFileContentFormSFTP(final ChannelSftp channelSftp, final String dataFilePath) {
		String property = System.getProperty("user.dir") + File.separator + "temp/";

		String directory = dataFilePath.substring(0, dataFilePath.lastIndexOf("/")); // 文件路径
		String downloadFile = dataFilePath.substring(dataFilePath.lastIndexOf("/") + 1); // 文件名称
		String saveFile = property + "/" + downloadFile; // 保存文件路径
		log.info("==>从SFTP获取文件内容，源文件路径[" + dataFilePath + "], 保存本地的临时文件路径[" + saveFile + "]");
		return downloadGetString(directory, downloadFile, saveFile, channelSftp);
	}

	public static File downFileFromSFTP(ChannelSftp channelSftp, final String filePath) {
		// 创建临时目录，用来存放下载的文件
		StringBuffer tempFilePath = new StringBuffer(System.getProperty("user.dir")).append(File.separator)
				.append("temp");
		isDir(tempFilePath.toString());
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		String tempPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);

		// 创建临时返回文件
		String saveFile = tempFilePath + "/" + fileName;
		File returnFile = new File(saveFile);
		try {
			download(tempPath, fileName, saveFile, channelSftp);
		} catch (Exception e) {
			log.error("==>对账文件下载失败：", e);
		} finally {
			if (channelSftp != null) {
				sftpClose(channelSftp);
			}
		}
		return returnFile;
	}

	public static void isDir(String path) {
		String[] paths = path.split("/");
		String filePath = "";
		for (int i = 0; i < paths.length; i++) {
			if (i == 0) {
				filePath = paths[0];
			} else {
				filePath += "/" + paths[i];
			}
			creatDir(filePath);
		}
	}

	public static void creatDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	// 测试例子
	public static void main(String[] args) {

		String host = "192.168.88.40";
		int port = 3210;
		String username = "gwpayfast";
		String password = "gzzyzz.com";
		String directory = "/home/gwpayfast/";

		String downloadFile = "Result.txt";
		String saveFile = "F:\\123.txt";

		String uploadFile = "E:\\PINGANBANK-NET-B2C-GZ20140523clear.txt";
		// String deleteFile = "delete.txt";
		ChannelSftp sftp = SftpUtil.connect(host, port, username, password);
		SftpUtil.upload(directory, uploadFile, sftp);
		// sf.download(directory, downloadFile, saveFile, sftp);
		// sf.delete(directory, deleteFile, sftp);

		// sf.creatDir(directory, sftp);
		// sftp.cd(directory);
		// System.out.println("finished");
		// sf.sftpClose(sftp);

	}

}
