小而美的代码片段

# 跨域过滤器
com.github.qq275860560.common.filter.CorsFilter
## 适用场景
适用于前后端跨域的项目，只要在web.xml或javaconfig，加入此过滤器即可
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

# http请求的参数变成Map或者List的工具类
com.github.qq275860560.common.util.RequestUtil
## 适用场景
* 对于ContentType为application/x-www-form-urlencoded的格式一般为a=1&b=2&b=3，解析后的Map有两个key，其中一个key为a，value为"1"，另一个key为b，value为new String[]{"2","3"};   
* 对于Content-Type=application/json;charset=UTF-8的消息体格式为标准json字符串，调用jackson反序列化

## 使用方式
ContentType=application/x-www-form-urlencoded时
```
Map<String, Object> map=RequestUtil.parameterToMap(request);//解析类似这种格式a=1&b=2
log.info("请求参数="+map);
```
Content-Type=application/json;charset=UTF-8时
```
Map<String, Object> map=RequestUtil.bodyToMap(request);//解析类似这种格式{"a":1,"b":"2"}
log.info("请求参数="+map);

List list=RequestUtil.bodyToMap(request);//解析类似这种格式[{"a":1},{"a":2}]
log.info("请求参数="+list);
```


# sql语句生成器
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
* 此项目将会得到长期维护，增加或改进实用的功能
* 喜欢请留下<font color="#0000FF">start</font>,给我一点前进的动力,谢谢