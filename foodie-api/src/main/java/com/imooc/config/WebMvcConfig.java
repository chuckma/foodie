package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mcg
 * @create 2019-11-18-15:51
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**") // 映射所有的资源
                .addResourceLocations("classpath:/META-INF/resources/") // 映射 swagger2
                .addResourceLocations("file:/Users/lucasma/developer/images/"); // 映射本地资源文件
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 构建一个 RestTemplate
        return builder.build();
    }
}
