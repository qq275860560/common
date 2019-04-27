package com.github.qq275860560.common.util;  
  
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

 
  
  
public class TestJdbcDriver {
	
	public static String url = "jdbc:mysql://10.18.96.50:3306/rest_home_hz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8"; 
	public static String username = "resthome";
	public static String password = "resthome&*()";	
	public static String driverClassName = "com.mysql.jdbc.Driver";
	public static String schemaName ="rest_home_hz";

	public static String tableName = "c_service_item";


    public static void main(String[] args) throws Exception{
    	 Class.forName(driverClassName);
		Connection connection = DriverManager.getConnection(url, username, password);
	   	
    	DatabaseMetaData  databaseMetaData  =connection.getMetaData();
		ResultSet rssultSet = databaseMetaData.getColumns(null, schemaName, tableName, "%"); 
		
		while (rssultSet.next()) {
		System.out.println(rssultSet.getString("COLUMN_NAME"));
		}
		connection.close();
	} 
}  