package com.github.qq275860560.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class FtpUtil {
	 	private static FTPClient ftpClient = new FTPClient();

	static {
		ftpClient.setConnectTimeout(20000);
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	public FTPFile[] listFiles(String tempDir) {
		FTPFile[] ff = null;
		try {
			ff = ftpClient.listFiles(tempDir);
		} catch (IOException e) {
			return null;
		}
		return ff;
	}

	public boolean connect(String hostname, int port, String username, String password) throws IOException {
		ftpClient.connect(hostname, port);
		log.info("FTP 远程连接成功");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				log.info("FTP 远程登陆成功");
				return true;
			}
		}
		log.info("FTP 远程连接成功");
		return false;
	}

	public boolean download(String remote, OutputStream out) {
		log.info("FTP 远程连接，文件开始下载... ...");
		ftpClient.enterLocalPassiveMode();
		boolean result = false;
		try {
			result = ftpClient.retrieveFile(remote, out);
			out.close();
			disconnect();
		} catch (IOException e) {
			result = false;
		}

		return result;
	}

	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

}
