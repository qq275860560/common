平常工作中积累的小而美的代码


# 跨域过滤器
com.github.qq275860560.common.filter.CorsFilter
## 使用场景
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
