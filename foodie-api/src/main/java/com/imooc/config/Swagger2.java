package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author mcg
 * @create 2019-11-15-12:57
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    // http://localhost:8088/swagger-ui.html  官方原路径
    // http://localhost:8088/doc.html  ui 优化之后的路径
    // 配置Swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 指定 api 类型 为 swagger2
                .apiInfo(apiInfo())  // 用于定义 api 文档汇总信息
                .select().apis(RequestHandlerSelectors
                        .basePackage("com.imooc.controller")) // 指定 controller 包
                .paths(PathSelectors.any()) // 所有 controller
                .build();

    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("foodie 接口 api") // 文档标题
                .contact(new Contact("mcg", "github.com/chuckma", "lucasmavip@qq.com"))
                .description("fooie 提供的 api 文档")
                .version("1.0.1") // 文档版本;
                .termsOfServiceUrl("github.com/chuckma").build();
    }

}
