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

	public static void unZip(File file) throws Exception {
		try (ZipFile zipFile = new ZipFile(file);) {
			Enumeration<?> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
				if (entry.isDirectory()) {
					new File(file.getParent(), entry.getName()).mkdirs();
				} else {
					File tmpFile = new File(file.getParent(), entry.getName());
					InputStream is = zipFile.getInputStream(entry);
					FileUtils.copyInputStreamToFile(is, tmpFile);
				}
			}
		}

	}
}
