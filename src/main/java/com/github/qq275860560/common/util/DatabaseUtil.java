package com.github.qq275860560.common.util;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author jiangyuanlin@163.com
 */
public class DatabaseUtil {

	private static Log log = LogFactory.getLog(DatabaseUtil.class);

	private DatabaseUtil() {
	}

 
	public static String url;
	public static String username;
	public static String password;
	public static String driverClassName;
	
	public static DruidDataSource dataSource;
    public static JdbcTemplate jdbcTemplate;

	static {
		try {
			Configuration configuration = new Configurations()
					.properties(new File("/", "application.properties"));
			url = configuration.getString("spring.datasource.url"); 
			username = configuration.getString("spring.datasource.username");
			password = configuration.getString("spring.datasource.password");	
			driverClassName = configuration.getString("spring.datasource.driverClassName"); //"com.mysql.cj.jdbc.Driver"
			dataSource = getDataSource(driverClassName,url, username, password);
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			log.error("", e);
			//System.exit(1);// 配置不准确，直接退出
		}
	}
 
	
	public static DruidDataSource getDataSource(String driverClassName,String url, String username,
			String password) {
		DruidDataSource dataSource = new DruidDataSource();// 创建了一个实例
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);// 设置数据库连接地址
		dataSource.setUsername(username);// 设置用户名
		dataSource.setPassword(password); // 设置密码
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setPoolPreparedStatements(false);	
		List<String> connectionInitSqls = new ArrayList<String>();
		connectionInitSqls.add("SET GLOBAL time_zone = '+8:00'");
		dataSource.setConnectionInitSqls(connectionInitSqls);
		return dataSource;
	}
	
	
	// 取第一列，转换为字符串列表
	 
	public static List<String> jdbcQueryForFirstColumnString(DataSource dataSource, String sql, Object... args)throws Exception {
		List<Map<String, Object>> list = jdbcQueryForList(dataSource, sql, args);
		List<String> resultList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				resultList.add(entry.getValue().toString());
				break;
			}
		}
		return resultList;
	}

	//取第一列，转换为字符串列表
	
	public static List<Integer> jdbcQueryForFirstColumnInteger(DataSource dataSource, String sql, Object... args)throws Exception {
		List<Map<String, Object>> list = jdbcQueryForList(dataSource, sql, args);
		List<Integer> resultList = new ArrayList<Integer>();
		for (Map<String, Object> map : list) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				resultList.add(Integer.parseInt(entry.getValue().toString()));
				break;
			}
		}
		return resultList;
	}

	// 取第一行，第一列，转换为整形

	public static int jdbcQueryForInt(DataSource dataSource, String sql, Object... args) throws Exception {
		Map<String, Object> resultMap = jdbcQueryForMap(dataSource, sql, args);
		int result = 0;
		if (resultMap.size() > 0) {
			for (Object value : resultMap.values()) {
				result = Integer.parseInt(value.toString());
				break;
			}
		}
		return result;
	}
	
	//取第一行，第一列，转换为整形
	
	public static double jdbcQueryForDouble(DataSource dataSource, String sql, Object... args) throws Exception {
		Map<String, Object> resultMap = jdbcQueryForMap(dataSource, sql, args);
		double result = 0;
		if (resultMap.size() > 0) {
			for (Object value : resultMap.values()) {
				result = Double.parseDouble(value.toString());
				break;
			}
		}
		return result;
	}

	//取第一行，第一列，转换为字符串
	 
	public static String jdbcQueryForString(DataSource dataSource, String sql, Object... args) throws Exception {
		Map<String, Object> resultMap = jdbcQueryForMap(dataSource, sql, args);
		String result = "";
		if (resultMap.size() > 0) {
			for (Object value : resultMap.values()) {
				result = value.toString();
				break;
			}
		}
		return result;
	}

	//取第一行，转换为HashMap，比如{"name_":XXX,"age_":YYY,"sex_":ZZZ}
	 
	public static Map<String, Object> jdbcQueryForMap(DataSource dataSource, String sql, Object... args)throws Exception {
		List<Map<String, Object>> resultList = jdbcQueryForList(dataSource, sql, args);
		if (resultList.isEmpty())
			return Collections.emptyMap();
		else
			return resultList.get(0);
	}

	//取第一行，转换为字符串列表
	 
	public static List<String> jdbcQueryForFirstRowString(DataSource dataSource, String sql, Object... args)
			throws Exception {
		Map<String, Object> resultMap = jdbcQueryForMap(dataSource, sql, args);
		List<String> resultList = new ArrayList<String>();
		if (resultMap.size() > 0) {
			for (Object value : resultMap.values()) {
				resultList.add(value.toString());
			}
		}
		return resultList;
	}

	public static List<Map<String, Object>> jdbcQueryForList(DataSource dataSource, String sql, Object... args)  throws Exception {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			List<Map<String, Object>> list0 = new ArrayList<Map<String, Object>>();
			PreparedStatement stmt = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				stmt.setObject(i + 1, o);
			}
			long start = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery();
			long end = System.currentTimeMillis();
			log.info((end - start) + "ms,sql=" + sql + ",args=" + Arrays.toString(args));
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int columns = resultSetMetaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columns; i++) {
					String columnTypeName = resultSetMetaData.getColumnTypeName(i);
					Object o = null;

					if (columnTypeName.equalsIgnoreCase("date")) {
						o = rs.getString(i); // + " 00:00:00";
					} else if (columnTypeName.equalsIgnoreCase("datetime")) {
						String s = rs.getString(i);
						if (StringUtils.isNotBlank(s))
							s = s.substring(0, 19);
						o = s;
					} else if (columnTypeName.equalsIgnoreCase("timestamp")) {
						String s = rs.getString(i);
						if (StringUtils.isNotBlank(s))
							s = s.substring(0, 19);
						o = s;
					} else if (columnTypeName.equalsIgnoreCase("mediumtext")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("text")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("char")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("varchar")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("longtext")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("enum")) {
						o = rs.getString(i);
					} else if (columnTypeName.equalsIgnoreCase("tinyint")) {
						o = rs.getInt(i);
					} else if (columnTypeName.equalsIgnoreCase("int")) {
						o = rs.getInt(i);
					} else if (columnTypeName.equalsIgnoreCase("smallint")) {
						o = rs.getInt(i);
					} else if (columnTypeName.equalsIgnoreCase("bigint")) {
						o = rs.getInt(i);
					} else if (columnTypeName.equalsIgnoreCase("decimal")) {
						o = rs.getDouble(i);
					} else if (columnTypeName.equalsIgnoreCase("double")) {
						o = rs.getDouble(i);
					} else if (columnTypeName.equalsIgnoreCase("varbinary")) {
						o = rs.getBytes(i);
					} else {
						o = rs.getString(i);
					}
					map.put(resultSetMetaData.getColumnName(i), o);
				}
				list0.add(map);
			}
			return list0;
		} catch (Exception e) {
			throw e;
		} finally {
			// 从连接池获取的连接connection跟JDK中的connection有点不同，前者的close方法并没有关闭与数据库的连接，而是将连接返回到池中
			if (connection != null)
				try {
					connection.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}		
	}

	// 增删改
 
	public static int jdbcUpdate(DataSource dataSource, String sql, Object... args)  throws Exception{
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				stmt.setObject(i + 1, o);
			}
			return stmt.executeUpdate();
		} catch (Exception e) {
			throw e;		 
		} finally {
			// 从连接池获取的连接connection跟JDK中的connection有点不同，前者的close方法并没有关闭与数据库的连接，而是将连接返回到池中
			if (connection != null)
				try {
					connection.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}	 
	}

	  

}
