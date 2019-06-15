[TOC]
小而美的代码片段

# 使用方式
## spring-boot项目pom文件引入依赖
```
<dependency>
 	<groupId>com.github.qq275860560</groupId>
	<artifactId>github-qq275860560-common</artifactId>
	<version>20190518</version>
</dependency>	
```

# 搞笑代码
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/FunnyUtil.java)
## 适用场景
娱乐
## 使用方式
```
public static void main(String[] args) throws Exception {
	// 升序排序
	System.out.println(sort(Arrays.asList(6, 7, 8, 9, 10, 1, 2, 3, 4, 5)));
	// 获取一天后的时间
	System.out.println(getNextDate());
	// 显示佛祖
	System.out.println(getSakyamuni());
}
```

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

# Frame过滤器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/filter/FrameFilter.java)
## 适用场景
返回的页面要嵌套在当前页面时。
只要在web.xml或javaconfig，加入此过滤器即可 
## 使用方式
### web.xml方式
```
<filter>
	<filter-name>FRAME</filter-name>
	<filter-class>com.github.qq275860560.common.filter.FrameFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>FRAME</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
### javaconfig方式
```
@Bean
public FilterRegistrationBean filterRegistrationBean() {
	FilterRegistrationBean registration = new FilterRegistrationBean(new FrameFilter());
	registration.addUrlPatterns("/*");
	registration.setOrder(0);
	return registration;
}
```


# 请求对象解释器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/RequestUtil.java)

## 适用场景
controller,filter，interceptor等接收参数有时候为了方便需要先解释成自行解释。此工具类可以将其解释到HashMap中，应用程序再从HashMap取值即可
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



# http响应工具
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/ResponseUtil.java)

## 适用场景
发送自定义格式的字符串
发送文件
发送内存中的文件字节数组


## 使用方式


### 发送自定义格式的字符串时

```
ResponseUtil.sendResult(response, result)
```

### 发送文件时

```

ResponseUtil.sendFile(response, file,   responseContentType)
```
### 发送内存中的文件字节数组

```
ResponseUtil.sendFileByteArray(response, byteArray,  fileName, responseContentType)
```
其中responseContentType通常为一下几种
"application/vnd.ms-excel;charset=utf-8"
"application/octet-stream;charset=UTF-8"


# sql语句生成器
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/JdbcTemplateSqlGeneratorUtil.java)
## 适用场景
* 对于mysql增删改查常用的框架是JPA或者mybatis，
* JPA对应用的侵入性太强，并且很难对付复杂查询，也很难针对性进行优化
* mybatis需要写配置文件或者使用一大堆乱七八糟的注解，两种都很难进行断点调试，分页功能实现也是很不优雅，前者还不符合javaConfig方式，当前流行的springboot也不推荐使用xml,因此xml可能作为历史存在，新项目xml能不用就不用
* spring自带的JdbcTemplate刚好解决这两个问题，既容易对付复杂场景，又能针对性调优，容易断点编程，写起来相对优雅，但是sql语句编写难度较大，此工具类为了简化JdbcTemplate的使用，自动生成常用的统计数量，校验唯一性，增加,删除,修改,查询，分页搜索模板，作为模板简单修改即可满足业务

## 使用方式

### mysql建表
* 字段命名使用驼峰，原因是大多数知名开源项目都使用驼峰命名，足以说明最适合工程化，此外如果程序和数据库的命名保持一致可以提高开发维护效率，比如cityName,CITY_NAME,总感觉前者的理解速度会快一些,因此统一使用驼峰
* 字段类型通常尽量使用int,double,varchar,datetime,性能优化以后再说，注意datetime跟字符串(格式yyyy-MM-dd HH:mm:ss)相互转换,而不是Date对象
* 此两个规则适用于前后端分离时双方的交互

```
drop table t_user;
create table `t_user` (
  `id` varchar(32) NOT NULL COMMENT '主键,为了跟应用程序代码风格兼容,尽量使用varchar',
  `sex` int(1)    COMMENT '性别{0:男,1:女},为了跟应用程序代码风格兼容,尽量使用int',
  `weight` double(2,2)    COMMENT '体重，单位公斤,为了跟应用程序代码风格兼容,尽量使用double',
  `mobilePhone` varchar(20)  COMMENT '手机,为了跟应用程序代码风格兼容,尽量使用驼峰',
  `birthDay` datetime  COMMENT '生日,时间格式统一使用datetime，程序中使用字符串（格式yyyy-MM-dd HH:mm:ss)跟数据库交互' ,
  PRIMARY KEY (`id`)
)    COMMENT='用户表，约定大于配置';
desc t_user;
```

### 生成代码

```
public static void main(String[] args) throws Exception {
	JdbcTemplateSqlGeneratorUtil.url = "jdbc:mysql://10.18.96.50:3306/rest_home_hz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
		JdbcTemplateSqlGeneratorUtil.username = "root";
		JdbcTemplateSqlGeneratorUtil.password = "123456";
		JdbcTemplateSqlGeneratorUtil.driverClassName = "com.mysql.jdbc.Driver";
		JdbcTemplateSqlGeneratorUtil.schemaName = "test";
		JdbcTemplateSqlGeneratorUtil.tableName = "t_user";
		JdbcTemplateSqlGeneratorUtil.modelName = "User";
		JdbcTemplateSqlGeneratorUtil.generate();
}
```
### 运行结果
把需要的函数拷贝到项目中的dao层,引入@JdbcTemplate,根据业务功能简单修改一下查询条件即可使用，
如果需要缓存，还可以添加spring的@Cacheable注解

```
@Autowired
private JdbcTemplate jdbcTemplate;

public int countUser(String name) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count from t_user where 1=1 "); 
    sb .append(" and name = ? ");
    condition.add(name);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    return jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
}

public boolean checkUser(String id,String name) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count  from t_user where 1=1 "); 
    if (StringUtils.isNotBlank(id)) {
    	sb .append(" and id != ? ");
    	condition.add(id);
    }
    sb .append(" and name= ? ");
    condition.add(name);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    int count = jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
    if(count>0) return false;
    else return true;
}

public int deleteUser(String id) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" delete  from t_user where 1=1 "); 
    sb .append(" and id = ? ");
    condition.add(id);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    return jdbcTemplate.update( sb.toString(), condition.toArray());
}

public Map<String,Object> getUser(String id) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,sex,weight,mobilePhone,date_format(birthDay,	'%Y-%m-%d %H:%i:%s') birthDay from t_user where 1=1 "); 
    if (StringUtils.isNotBlank(id)) {
    	sb .append(" and id = ? ");
    	condition.add(id);
    }
    sb.append(" limit ? ,?  ");
    condition.add(0);
    condition.add(1);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    Map<String,Object> map = Collections.EMPTY_MAP;
    try{
    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());
    }catch (Exception e) {
    }
    return map;
}

public Map<String,Object> getUserByKeyValue(String key,Object value) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,sex,weight,mobilePhone,date_format(birthDay,	'%Y-%m-%d %H:%i:%s') birthDay from t_user where 1=1 "); 
   	sb .append(" and "+key+" = ? ");
   	condition.add(value);
    sb.append(" limit ? ,?  ");
    condition.add(0);
    condition.add(1);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    Map<String,Object> map = Collections.EMPTY_MAP;
    try{
    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());
    }catch (Exception e) {
    }
    return map;
}

public int saveUser( Map<String,Object> map)  throws Exception  {
    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb1.append("id").append(",");
    sb2.append("?,");
    condition.add(map.get("id"));

    sb1.append("sex").append(",");
    sb2.append("?,");
    condition.add(map.get("sex"));

    sb1.append("weight").append(",");
    sb2.append("?,");
    condition.add(map.get("weight"));

    sb1.append("mobilePhone").append(",");
    sb2.append("?,");
    condition.add(map.get("mobilePhone"));

    sb1.append("birthDay").append(",");
    sb2.append("?,");
    condition.add(map.get("birthDay"));

    if (sb1.length() > 0)
        sb1.deleteCharAt(sb1.length() - 1);
    if (sb2.length() > 0)
        sb2.deleteCharAt(sb2.length() - 1);
    String sql = "insert into t_user(" + sb1.toString() + ") values(" + sb2.toString() + ")";
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    return jdbcTemplate.update( sql, condition.toArray());

}

public int updateUser( Map<String,Object> map) throws Exception  {
    StringBuilder sb = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" sex = ? ,");
    condition.add(map.get("sex"));
    
    sb.append(" weight = ? ,");
    condition.add(map.get("weight"));
    
    sb.append(" mobilePhone = ? ,");
    condition.add(map.get("mobilePhone"));
    
    sb.append(" birthDay = ? ,");
    condition.add(map.get("birthDay"));
    
    if (sb.length() > 0)
        sb.deleteCharAt(sb.length() - 1);	
    String sql = "update t_user set " + sb.toString() + " where    id=?";
    condition.add(map.get("id"));
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    return jdbcTemplate.update(  sql, condition.toArray());
}

public List<Map<String,Object>> listUser( String id,Integer sex,Double weight,String mobilePhone,String startBirthDay,String endBirthDay) throws Exception  {
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,sex,weight,mobilePhone,date_format(birthDay,	'%Y-%m-%d %H:%i:%s') birthDay from t_user where 1=1 "); 
    if (StringUtils.isNotBlank(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (sex!=null) {
    	sb .append(" and sex = ? ");
    	condition.add(sex);
    }
    if (weight!=null) {
    	sb .append(" and weight = ? ");
    	condition.add(weight);
    }
    if (StringUtils.isNotBlank(mobilePhone)) {
    	sb .append(" and mobilePhone like ? ");
    	condition.add("%"+mobilePhone+"%");
    }
    if (StringUtils.isNotBlank(startBirthDay)) {
    	sb .append(" and birthDay >=  ?  ");
    	condition.add(startBirthDay);
    }
    if (StringUtils.isNotBlank(endBirthDay)) {
    	sb .append(" and birthDay <=  ?  ");
    	condition.add(endBirthDay);
    }
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    return jdbcTemplate.queryForList( sb.toString(), condition.toArray());

}

public Map<String,Object> pageUser( String id,Integer sex,Double weight,String mobilePhone,String startBirthDay,String endBirthDay,Integer pageNum,Integer pageSize) throws Exception  {
    if(pageNum==null) pageNum=1;//取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号
    if(pageSize==null)pageSize=10;//取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综合比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量
    int from = (pageNum-1)*pageSize;
    int size = pageSize;
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,sex,weight,mobilePhone,date_format(birthDay,	'%Y-%m-%d %H:%i:%s') birthDay from t_user where 1=1 "); 
    if (StringUtils.isNotBlank(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (sex!=null) {
    	sb .append(" and sex = ? ");
    	condition.add(sex);
    }
    if (weight!=null) {
    	sb .append(" and weight = ? ");
    	condition.add(weight);
    }
    if (StringUtils.isNotBlank(mobilePhone)) {
    	sb .append(" and mobilePhone like ? ");
    	condition.add("%"+mobilePhone+"%");
    }
    if (StringUtils.isNotBlank(startBirthDay)) {
    	sb .append(" and birthDay >=  ?  ");
    	condition.add(startBirthDay);
    }
    if (StringUtils.isNotBlank(endBirthDay)) {
    	sb .append(" and birthDay <=  ?  ");
    	condition.add(endBirthDay);
    }
    String countSql = "select count(1) count from ( " + sb.toString()+") t";
    int count = jdbcTemplate.queryForObject(countSql, condition.toArray(),Integer.class);
    sb.append(" order by birthDay desc ");
    sb.append(" limit ? ,?  ");
    condition.add(from);
    condition.add(size);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));
    List<Map<String, Object>> list = jdbcTemplate.queryForList( sb.toString(), condition.toArray());
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total", count);//取名total为了兼容mybatis-pageHelper中的page对象的total,spring框架的PageImpl也使用total
    map.put("list", list);//不同的框架取名不一样，可以把list改成array,rows,data,content,result等,spring框架使用的是content,mybatis因为page是继承ArrayList，字段命名乱七八糟，有时pages，有时pageList，有时result，综上感觉list会更加直观和简洁,不需要看上下文能猜出字段是列表
    return map;

}
```

# 模版渲染工具
[源码](https://github.com/qq275860560/common/blob/master/src/main/java/com/github/qq275860560/common/util/FreemarkerUtil.java)

## 适用场景
* 有时为了生成格式固定某些内容不同的字符串或文件，需要使用大量的字符串和和文件操作,代码混乱难于维护
* 此时采用模板引擎是一种很好的选择，比如Freemarker，相对直观
* 然而应用程序通常只需要简单的几种功能，模板引擎的设置参数却过于复杂，使用成本较高
* 为了达到直观并且易于使用的目的，本工具类基于freemarker进行二次封装

## 使用方式

```
public static void main1(String[] args) {
	// 模板字符串->目标字符串
	String templateString = "hello ${msg}";
	Map<String, Object> map = new HashMap<String, Object>() {
		{
			put("msg", "world");
		}
	};
	String destString = generateString(templateString, map);
	log.info("destString=" + destString);// 最终打印hello world
}

public static void main2(String[] args) throws Exception {
	// 模板字符串->目标文件
	String templateString = "hello ${msg}";
	Map<String, Object> map = new HashMap<String, Object>() {
		{
			put("msg", "world");
		}
	};
	File destFile = new File("c:/2.txt");
	generateFile(templateString, map, destFile);// 最终文件内容hello world

}

public static void main3(String[] args) throws Exception {
	// 模板文件->目标字符串
	File templateFile = new File("c:/1.txt");
	Map<String, Object> map = new HashMap<String, Object>() {
		{
			put("msg", "world");
		}
	};
	String destString = generateString(templateFile, map);
	log.info("destString=" + destString);// 最终打印hello world

}

public static void main(String[] args) throws Exception {
	// 模板文件->目标文件
	File templateFile = new File("c:/1.txt");
	Map<String, Object> map = new HashMap<String, Object>() {
		{
			put("msg", "world");
		}
	};
	File destFile = new File("c:/2.txt");
	generateFile(templateFile, map, destFile);// 最终文件内容hello world

}
```

# 温馨提醒
* 此项目将会长期维护，增加或改进实用的功能
* 右上角点击star，给我继续前进的动力,谢谢