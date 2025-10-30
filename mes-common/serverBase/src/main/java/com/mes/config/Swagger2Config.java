package com.mes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 11:42
 * @Description:
 */
@Configuration
public class Swagger2Config {


    @Bean
    public Docket moduleDocket() {
        return docket("mes接口文档", "com.mes");
    }


    private Docket docket(String groupName, String basePackages) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackages))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("mes接口文档系统")
                .description("mesApi接口文档系统")
                .license("Powered By mes")
                .licenseUrl("http://127.0.0.1")
                .termsOfServiceUrl("http://127.0.0.1")
                .contact(new Contact("mes", "http://127.0.0.1", "beibo@mes.com"))
                .version("V1.0.0")
                .build();
    }
}
