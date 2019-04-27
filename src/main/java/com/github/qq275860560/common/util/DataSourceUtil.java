package com.github.qq275860560.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author jiangyuanlin@163.com
 */
public class DataSourceUtil {

	private static Log log = LogFactory.getLog(DataSourceUtil.class);

	private DataSourceUtil() {
	}

	public static DataSource dataSource;

	static {
		try {
			// 1.注册
			Class.forName("com.mysql.jdbc.Driver");
			Properties prop = new Properties();
			InputStream is = DataSourceUtil.class.getClassLoader().getResourceAsStream("database.properties");
			prop.load(is);
			// 使用的是DBCP方式来加载数据库连接信息
			dataSource = createDataSource((String) prop.getProperty("jdbc.driverClassName"),
					(String) prop.getProperty("jdbc.url"), (String) prop.getProperty("jdbc.username"),
					(String) prop.getProperty("jdbc.password"));
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static DataSource createDataSource(String driverClassName, String url, String username, String password) {
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

}
