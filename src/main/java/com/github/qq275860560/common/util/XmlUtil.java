package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jiangyuanlin@163.com
 */
public class XmlUtil {
	private static Log log = LogFactory.getLog(XmlUtil.class);

	private XmlUtil() {
	}

	/**
	 * 将对象序列化成Json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toXMLString(Object obj) {
		String jsonString = null;
		try {
			jsonString = null;// mapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error(null, e);
		}
		return jsonString;

	}

	/**
	 * 将Json字符串反序列化成对象,适用于单层节点转换
	 * 
	 * @param xmlString
	 * @param clazz
	 * @return
	 */
	public static Map<String, Object> parse(String xmlString) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		T t = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xmlString));

			Element root = document.getRootElement();

			List<Element> list = root.getChildren();
			for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {

				Element child = iterator.next();
				String key = child.getName();

				List<Element> children = child.getChildren();
				if (children.size() == 0)
					responseMap.put(key, child.getText());
				else {
					Map<String, Object> responseMap2 = new HashMap<String, Object>();
					for (Iterator<Element> iterator2 = children.iterator(); iterator2.hasNext();) {
						Element child2 = iterator2.next();
						String key2 = child2.getName();
						List<Element> children2 = child.getChildren();
						responseMap2.put(key2, child2.getText());

					}
					responseMap.put(key, responseMap2);

				}

			}

		} catch (Exception e) {
			log.error(null, e);
		}
		return responseMap;

	}

	public static String getValue(String jsonKey, String content) {
		com.fasterxml.jackson.databind.JsonNode jsoNode = null;
		try {
			jsoNode = null;// mapper.readTree(content);
			return jsoNode.get(jsonKey).asText();
		} catch (Exception e) {
			log.error(null, e);
		}
		return null;
	}

	/**
	 * 格式化xml
	 * 
	 * @param content
	 *            要格式化的xml字符串
	 * @return
	 */
	public static String format(String content) {
		String header = getHeader(content);
		return (StringUtils.isEmpty(header) ? "" : header) + format(null, content, 0);
	}

	private static String format(String tag, String content, int depth) {
		String format = "";
		String firstTag = "";
		if (StringUtils.isEmpty(tag)) {
			firstTag = getFirstTag(content);
		} else {
			firstTag = tag;
		}

		String inside = getInsideContent(firstTag, content);
		String outside = getOutsideContent(firstTag, content);

		String insideTag = "";
		try {
			insideTag = getFirstTag(inside);
		} catch (Exception e) {
			insideTag = null;
		}
		if (StringUtils.isEmpty(insideTag)) {
			format = "\r\n" + indent(depth) + "<" + firstTag + ">" + inside + "</" + firstTag.split(" ")[0] + ">";
		} else {
			format = "\r\n" + indent(depth) + "<" + firstTag + ">" + format(insideTag, inside, depth + 1)
					+ indent(depth) + "</" + firstTag.split(" ")[0] + ">";
		}

		String outsideTag = "";
		if (StringUtils.isEmpty(outside)) {
			outsideTag = null;
		} else {
			outsideTag = getFirstTag(outside);
		}
		if (!StringUtils.isEmpty(outsideTag)) {
			format += indent(depth) + format(outsideTag, outside, depth);
		} else if (StringUtils.isEmpty(outside)) {
			format += "\r\n";
		} else {
			log.error("xml报文格式不正确");
			return null;
		}
		return format;
	}

	/**
	 * 获取xml头部数据，格式：<? …… ?>
	 * 
	 * @return xml头部数据，null表示不存在
	 */
	private static String getHeader(String content) {
		for (int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
				continue;
			}

			if (c == '<' && content.charAt(i + 1) == '?') {
				String header = "<?";
				for (i = i + 2; i < content.length(); i++) {
					char end = content.charAt(i);
					if (end == '?' && content.charAt(i + 1) == '>') {
						header += "?>";
						content = content.substring(i + 2);
						return header;
					} else {
						header += end;
					}
				}
			}

			return null;
		}

		return null;
	}

	/**
	 * 获取xml报文的第一个标签
	 * 
	 * @param content
	 *            xml报文
	 * @return 标签名称
	 */
	private static String getFirstTag(String content) {
		StringBuilder tag = new StringBuilder();
		int index = 0;

		for (; index < content.length(); index++) {
			char temp = content.charAt(index);
			if (temp == ' ' || temp == '\r' || temp == '\n' || temp == '\t') { // 忽略空格回车字符
				continue;
			}

			if (temp != '<') {
				log.error("xml报文格式不正确");
				return null;
			}
			break;
		}

		for (int i = index + 1; i < content.length(); i++) {

			char c = content.charAt(i);
			if (c == '>') {
				return tag.toString();
			}
			tag.append(c);
		}
		log.error("xml报文格式不正确");
		return null;
	}

	private static String getOutsideContent(String tag, String content) {
		String endTag = "</" + tag.split(" ")[0] + ">";
		int endIndex = content.indexOf(endTag) + endTag.length();

		return content.substring(endIndex);
	}

	private static String getInsideContent(String tag, String content) {
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag.split(" ")[0] + ">";

		int startIndex = content.indexOf(startTag) + startTag.length();
		int endIndex = content.indexOf(endTag);

		return content.substring(startIndex, endIndex);
	}

	private static String indent(int num) {
		String space = "";
		if (num == 0) {
			return space;
		} else {
			return space + PER_SPACE + indent(num - 1);
		}
	}

	private static final String PER_SPACE = "    "; // 缩进字符串

	public static org.w3c.dom.Document createDocument(File file) throws Exception {
		InputStream inputStream = null;
		org.w3c.dom.Document document = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			inputStream = new FileInputStream(file);
			document = db.parse(inputStream);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e2) {

				}
			return document;

		}
	}

	public static HashMap<String, Object> xmlToMap(org.w3c.dom.Document document) {
		org.w3c.dom.Element root = document.getDocumentElement();
		org.w3c.dom.NodeList iterator = root.getChildNodes();
		Stack<Object> stack = new Stack<Object>();
		Integer j = 0;
		HashMap<String, Object> tmp = new HashMap<String, Object>();
		do {
			for (; j < iterator.getLength(); j++) {
				Node childNode = iterator.item(j);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					if (childNode.getChildNodes().getLength() == 1) {
						tmp.put(childNode.getNodeName(), childNode.getTextContent());
					} else {
						HashMap<String, Object> cc = new HashMap<String, Object>();
						tmp.put(childNode.getNodeName(), cc);
						stack.push(tmp);
						stack.push(j + 1);
						stack.push(iterator);
						tmp = cc;
						j = 0;
						iterator = childNode.getChildNodes();
					}
				}
			}
			if (stack.isEmpty()) {
				break;
			}
			iterator = (NodeList) stack.pop();
			j = (Integer) stack.pop();
			tmp = (HashMap<String, Object>) stack.pop();
		} while (true);
		return tmp;
	}

	public static void main(String[] args) throws Exception {

		org.w3c.dom.Document document = XmlUtil.createDocument(new
				  File("D:/workspace/github-qq275860560-common/pom.xml"));
		
		Map<String, Object> map = XmlUtil.xmlToMap(document);
		log.info(JsonUtil.toJSONString(map));
		// String
		// name=getValue("age","<person><name>zhangsan</name><age>100</age></person>");
		// log.info(name);

		// Map<String,Object>
		// map=parse("<person><name>zhangsan</name><age>100</age></person>",Map.class);
		// log.info(map);

		// log.info(toXMLString(map));

		// log.info(format(toXMLString(map)));

		// String xmlString = FileUtils.readFileToString(new
		// File("D:/workspace/github-qq275860560-common/pom.xml"));
		// log.info(xmlString);
		// Map<String, Object> map = parse(xmlString);
		// log.info(map);
		// log.info(format(toXMLString(map)));
	}

}
