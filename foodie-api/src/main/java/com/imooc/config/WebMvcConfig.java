package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author mcg
 * @create 2019-11-18-15:51
 */
@Configuration
public class WebMvcConfig {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 构建一个 RestTemplate
        return builder.build();
    }
}
