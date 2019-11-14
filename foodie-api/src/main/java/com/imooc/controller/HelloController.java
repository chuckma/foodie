package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "Hello World";
    }
}
