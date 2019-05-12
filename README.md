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
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/JdbcTemplateSqlGeneratorUtil.java)
## 适用场景
对于mysql增删改查虽然可以使用JPA或者mybatis，
但是JPA对应用的侵入性太强，并且JPA很难对付复杂查询
mybatis，需要写配置文件或者使用一大堆乱七八糟的注解，很难进行断点调试，分页功能实现也是很麻烦
spring自带的JdbcTemplate刚好解决这两个问题，但是sql语句编写难度大，此工具类为了简化JdbcTemplate的使用，自动生成常用的增删改查模板(时间跟字符串相互转换)，然后简单修改模板即可
## 使用方式
mysql建表,字段命名使用驼峰，
字段类型通常尽量使用int,double,varchar,datetime,其中datetime被解释为字符串格式yyyy-MM-dd HH:mm:ss
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