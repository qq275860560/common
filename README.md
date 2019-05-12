[TOC]
小而美的代码片段

# RSA字符串生成器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RsaUtil.java)
## 适用场景
比如jwt对token进行非对称加密，需要公钥和私钥字符串（base64形式）
把生成的公钥和私钥字符串（base64形式）放到配置文件
## 使用方式
```
public static void main(String[] args) throws Exception {
	RsaUtil.generateRsaKeyBase64EncodeString();
}
```


# 跨域过滤器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/filter/CorsFilter.java)
## 适用场景
比如前后端跨域的项目，经常碰到跨域问题，尤其集成已有旧系统，
只要在web.xml或javaconfig，加入此过滤器即可 
## 使用方式
### web.xml方式
```
<filter>
	<filter-name>CORS</filter-name>
	<filter-class>com.github.qq275860560.common.filter.CorsFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>CORS</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
### javaconfig方式
```
@Bean
public FilterRegistrationBean filterRegistrationBean() {
	FilterRegistrationBean registration = new FilterRegistrationBean(new CorsFilter());
	registration.addUrlPatterns("/*");
	registration.setOrder(0);
	return registration;
}
```



# 请求对象解释器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RequestUtil.java)
## 适用场景
controller,filter，interceptor等接收参数有时候为了方便需要先解释成自行解释。次工具类可以将其解释到HashMap中，应用程序再从HashMap取值即可
对于ContentType为application/x-www-form-urlencoded的消息体格式，比如a=1&b=2&b=3，解析后是HashMap，包括两个key，其中一个key为a，value为"1"，另一个key为b，value为Arrays.asList(2,3);   
对于Content-Type=application/json;charset=UTF-8的消息体格式，比如{"a":"1","b":[2,3]}，解析后是HashMap，包括两个key，其中一个key为a，value为"1"，另一个key为b，value为Arrays.asList(2,3);


## 使用方式
当ContentType=application/x-www-form-urlencoded时
```
Map<String, Object> map=RequestUtil.parameterToMap(request);//解析类似这种格式a=1&b=2
log.info("请求参数="+map);
```
当Content-Type=application/json;charset=UTF-8时
```
Map<String, Object> map=RequestUtil.bodyToMap(request);//解析类似这种格式{"a":1,"b":"2"}
log.info("请求参数="+map);

```


# sql语句生成器
mysql增删改查语句模板生成
com.github.qq275860560.common.util.JdbcTemplateSqlGeneratorUtil
## 适用场景
有些项目使用轻量级的JdbcTemplate,
跟jpa不同的是，sql语句几乎要自己写，
然而，对于单表的增加，删除，修改，查询，分页等操作写法都是相似的，只是一些字段名不同，此工具类只要输入数据库表名，即可动态生成对应的sql语句

## 使用方式
```
public static void main(String[] args) throws Exception {
	JdbcTemplateSqlGeneratorUtil.url = "jdbc:mysql://10.18.96.50:3306/rest_home_hz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
	JdbcTemplateSqlGeneratorUtil.username="root";
	JdbcTemplateSqlGeneratorUtil.password = "123456";
	JdbcTemplateSqlGeneratorUtil.driverClassName = "com.mysql.jdbc.Driver";
	JdbcTemplateSqlGeneratorUtil.schemaName = "rest_home_hz";
	JdbcTemplateSqlGeneratorUtil.tableName = "c_service_catalog";
	JdbcTemplateSqlGeneratorUtil.modelName = "CServiceCatalog";
	JdbcTemplateSqlGeneratorUtil.generate();
}
```

# 温馨提醒
* 此项目将会长期维护，增加或改进实用的功能
* 右上角点击star，给我继续前进的动力,谢谢