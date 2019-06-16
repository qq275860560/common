package com.github.qq275860560.common.util;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;

/**
 * @author jiangyuanlin@163.com
 */
public class CompressUtil {

	public static File unZip(File file) throws Exception {
		String destPath=file.getParent()+File.separator+file.getName().substring(0,file.getName().indexOf(('.')));
		File destDir = new File(destPath);//解压到文件夹，文件夹名称和压缩包名称相同,
		destDir.mkdirs();
		try (ZipFile zipFile = new ZipFile(file);) {
			Enumeration<?> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
				if (entry.isDirectory()) {
					new File(destPath, entry.getName()).mkdirs();
				} else {
					File tmpFile = new File(destPath, entry.getName());
					InputStream is = zipFile.getInputStream(entry);
					FileUtils.copyInputStreamToFile(is, tmpFile);
				}
			}
		}
		return destDir;

	}
}
