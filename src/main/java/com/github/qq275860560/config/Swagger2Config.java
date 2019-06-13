package com.github.qq275860560.config;

import java.util.ArrayList;
import java.util.List;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//复制到项目中要启用本行@Configuration
//复制到项目中要启用本行@EnableSwagger2
/**
 * @author jiangyuanlin@163.com
 *
 */
public class Swagger2Config {

	//复制到项目中要启用本行@Bean
	public Docket createRestApi() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		tokenPar.name("Authorization").description("令牌token").modelRef(new ModelRef("string")).parameterType("header")
				.required(false)
				.defaultValue(
						"Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsImV4cCI6MTg3NDQwMDQxMSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiI2NDNmMDUxYi0xMTdjLTQxYzItYjg3ZC0zNDZkMGVjNTUwN2QifQ.agsyUs1LENpgVhtQrEb-aoX6s0mAJphpnuXLEqkrWBu_EIxFsXswhmKTauVakEQHEnXMPTvKZ_zfFZWKIorTQQ")
				.build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/api/.*")).paths(PathSelectors.any()).build()
				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("security-demo").description("security-demo")
				.termsOfServiceUrl("https://github.com/qq275860560/security-demo").contact(new Contact("qq275860560",
						"https://github.com/qq275860560/security-demo", "jiangyuanlin@163.com"))
				.version("1.0.0").build();
	}
	// 补充login logout的api文档

}
