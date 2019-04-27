package com.github.qq275860560.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangyuanlin@163.com
 * 仿照redis的语法建立的本地jvm缓存 *
 */
public class XssUtil {
	 public static String cleanXSS(String value) {
//       logger.info("清除恶意的XSS脚本  before value="+value);
       // 移除特殊标签
       if(StringUtils.isNotEmpty(value)){
           value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
           value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
           value = value.replaceAll("'", "&#39;");
           value = value.replaceAll("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
           value = value.replaceAll("[\\\"\\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\\']", "\"\"");
           value = value.replaceAll("[s|S][c|C][r|R][i|C][p|P][t|T]", "");
       }
       return value;
   }

   public static String delHTMLTag(String htmlStr)
   {
       String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";//定义script的正则表达式
       String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";   //定义style的正则表达式
       String regEx_html = "<[^>]+>";                               //定义HTML标签的正则表达式
       String regExSpace = "(&nbsp;)+";                            //定义空格的正则表达式
       String regEx_space = "\\s*|\t|\r|\n";                      //定义空格\t、回车\n、换行符\r、制表符\t的正则表达式

       Pattern p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
       Matcher m_script = p_script.matcher(htmlStr);
       htmlStr = m_script.replaceAll("");//过滤script标签

       Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
       Matcher m_style = p_style.matcher(htmlStr);
       htmlStr = m_style.replaceAll("");//过滤style标签

       Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
       Matcher m_html = p_html.matcher(htmlStr);
       htmlStr = m_html.replaceAll("");//过滤html标签


       htmlStr = htmlStr.replaceAll(regExSpace, "");//过滤空格

       Pattern pSpace = Pattern.compile(regEx_space);
       Matcher m = pSpace.matcher(htmlStr);
       htmlStr = m.replaceAll("");//过滤字符串中的空格\t、回车\n、换行符\r、制表符\t

       htmlStr = htmlStr.replaceAll(" +", "");
       return htmlStr.trim();
   }


   public static void main(String[] args) {
//     String str = "<scRipt><font>未整合单位的联系电话</font>,关键字[] 单位名称</scRipt> 员工数据 \t\t \t\t \t\t\t \t\t\t\t归属公司      \r\n   热线号码    \r\n    业务功能    \r\n      市府办     \r\n     22831628    \r\n";
//     HtmlScriptUtils h=new HtmlScriptUtils();
////     str=h.cleanXSS(str);
////     System.out.println(str);
//
//     str=h.delHTMLTag(str);
//     System.out.println(str);


     //去除html转义
     String content="&#40;家庭&#41;e8天翼宽带套餐退订异常\n";
     content= StringEscapeUtils.unescapeHtml(content);
     System.out.println(content);
   }
 }