package com.imooc;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author mcg
 * @Date 2019/11/23 19:43
 **/
// 打包 war 4 增加 war 的启动类
public class WarStarterApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向 Application 这个 springboot 启动类
        return builder.sources(Application.class);
    }
}
