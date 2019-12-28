package com.imooc.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@ApiIgnore // 忽略这个类里的所有 controller 方法，不会在 Swagger2 接口文档里显示
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "Hello World";
    }


    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new User");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");

        return "ok";
    }
}
