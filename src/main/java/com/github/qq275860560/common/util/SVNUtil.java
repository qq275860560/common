package com.github.qq275860560.common.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

/**
 * @author jiangyuanlin@163.com
 */
public class SVNUtil {
	private static Log log = LogFactory.getLog(SVNUtil.class);

	private SVNUtil() {
	}

	private static SVNClientManager getSVNClientManager(String username, String password) {
		DefaultSVNOptions options = new DefaultSVNOptions();
		SVNClientManager manager = SVNClientManager.newInstance(options);
		return SVNClientManager.newInstance(options, username, password);

	}

	// 获取svn中文件中的内容，适用于utf8格式的文本型

	public static String getFileContent(String url, String username, String password) throws Exception {
		String result = null;
		try {

			SVNClientManager manager = getSVNClientManager(username, password);

			SVNRepository svnRepository = manager.createRepository(SVNURL.parseURIEncoded(url), true);

			SVNDirEntry entry = svnRepository.getDir("", -1, false, null);
			int size = (int) entry.getSize();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
			SVNProperties svnProperties = new SVNProperties();
			svnRepository.getFile("", -1, svnProperties, outputStream);
			result = new String(outputStream.toByteArray(), Charset.forName("utf-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	// 列出所有项目

	public static List<String> listProjectName(String url, String username, String password) throws Exception {

		List<String> resultList = new ArrayList<>();
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				return true;
			}
		}).build();

		CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
				.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		HttpGet httpGet = new HttpGet(url);

		String encoding = new String((new Base64()).encode((username + ":" + password).getBytes()));
		httpGet.setHeader("Authorization", "Basic " + encoding);
		HttpResponse response = httpClient.execute(httpGet);
		String httpEntityContent = EntityUtils.toString(response.getEntity(), "UTF-8");
		httpGet.abort();

		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new StringReader(httpEntityContent));

		Element root = document.getRootElement().getChild("index");

		List<Element> list = root.getChildren();
		for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {

			Element child = iterator.next();

			String projectName = child.getAttributeValue("name");

			resultList.add(projectName);

		}

		return resultList;
	}

	// curl 'https://192.168.115.56:9000/svn/' -H 'Authorization: Basic
	// amVua2luc19hZG1pbjoxMjM0NTY3OA==' --insecure

	public static void main(String[] args) throws Exception {
		String url = "https://192.168.115.56:9000/svn/";
		String username = "jenkins_admin";
		String password = "12345678";
		List<String> resultList = listProjectName(url, username, password);
		log.info(JsonUtil.toJSONString(resultList));
	}
}
