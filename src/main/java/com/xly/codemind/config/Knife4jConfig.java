package com.xly.codemind.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
 * @author X-LY。
 * @version 1.0
 * @className Knife4jConfig
 * @description Knife4j接口文档配置类
 **/

@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) // 使用自定义的 apiInfo 方法
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xly.codemind.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CodeMind智慧编程学习平台接口文档")
                .description("CodeMind智慧编程学习平台后端API文档")
                .version("1.0")
                .termsOfServiceUrl("https://www.example.com/terms") // 服务条款链接
                .contact(new Contact("X-LY。", "https://github.com/LableX-LY", "501028734@qq.com")) // 作者信息
                .license("Apache 2.0") // 许可证名称
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0") // 许可证链接
                .build();
    }

}