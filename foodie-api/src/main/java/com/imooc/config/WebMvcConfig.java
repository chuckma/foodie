package com.imooc.config;

import com.imooc.controller.interceptor.UserTokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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


    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        // 构建一个 UserTokenInterceptor
        return new UserTokenInterceptor();
    }

    /**
     * 注册拦截器，所有的拦截器都需要注册到容器里才能使用
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 是添加你需要拦截的路径
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/hello")
                .addPathPatterns("/shopcart/add")
                .addPathPatterns("/shopcart/del")
                .addPathPatterns("/address/list")
                .addPathPatterns("/address/add")
                .addPathPatterns("/address/update")
                .addPathPatterns("/address/setDefault")
                .addPathPatterns("/address/delete")
                .addPathPatterns("/orders/*")
                .addPathPatterns("/center/*")
                .addPathPatterns("/userInfo/*")
                .addPathPatterns("/myorders/*")
                .addPathPatterns("/mycomments/*")
                .excludePathPatterns("/myorders/deliver")
                .excludePathPatterns("/orders/notifyMerchantOrderPaid");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
