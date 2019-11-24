package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author mcg
 * @Date 2019/11/15 20:23
 *
 *
 * 跨域配置
 **/

@Configuration
public class CorsConfig {
    public CorsConfig() {

    }


    @Bean
    public CorsFilter corsFilter() {
        // 1 添加 cors 配置信息

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://192.168.5.201:8080");

        // 设置是否发送 cookie 信息
        config.setAllowCredentials(true);

        // 设置运行请求的方式
        config.addAllowedMethod("*");

        // 设置运行的header
        config.addAllowedHeader("*");

        // 2 为 url 设置映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);

        // 3. 返回重新定义好的 corsSource
        return new CorsFilter(corsSource);

    }
}
