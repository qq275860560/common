package com.ctbiyi.dataxweb.constant;

/**
 * @author jiangyuanlin@163.com
 *
 */
public class Constant {
	private Constant() {
		
	}
    public static final String DATAX_HOME = System.getenv("DATAX_HOME");    
   
    public static final int JOB_STATUS_DISABLE =0;//计划任务停用
    public static final int JOB_STATUS_ENABLE =1;//计划任务启用
    public static final int JOB_STATUS_RUNNING =2;//计划任务运行中    
    
    public static final int BUILD_STATUS_RUNNING =2;//构建中
    public static final int BUILD_STATUS_EXIT =3;//构建完毕
    
    public static final int BUILD_RESULT_SUCCESS = 1;//构建成功
    public static final int BUILD_RESULT_FAILURE = 2;//构建失败
    public static final int BUILD_RESULT_ABORTED = 3;//构建取消    
    
    public static final String INPUT_TYPE_MYSQLREADER = "mysqlreader";//mysql类型的输入流
    public static final String INPUT_TYPE_TXTFILEREADER = "txtfilereader";//文本类型输入流
    public static final String INPUT_TYPE_HTTPREADER = "httpreader";//http类型输入流
    public static final String INPUT_TYPE_FTPREADER = "ftpreader";//FTP类型输入流

    public static final String OUTPUT_TYPE_MYSQLWRITER = "mysqlwriter";//mysql类型的输出流
    public static final String OUTPUT_TYPE_TXTFILEWRITER = "txtfilewriter";//文本类型输出流
    public static final String OUTPUT_TYPE_HTTPWRITER = "httpwriter";//文本类型输出流
    public static final String OUTPUT_TYPE_FTPWRITER = "ftpwriter";//FTP输出流

    public static final String TRANSFORMER_TYPE_GROOVY = "dx_groovy";//交换清洗规则

    
}
