package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key,String value) {

        redisOperator.set(key,value);
        return "OK";
    }



    @GetMapping("/get")
    public String get(String key) {
        Object o = redisOperator.get(key);
        return o.toString();
    }


    @GetMapping("/delete")
    public Object del(String key) {
        redisOperator.del(key);
        return "OK";
    }


    @GetMapping("/batchSet")
    public Object batchSet(String key) {
        for (int i = 0; i < 10; i++) {
            redisOperator.set("c"+i,UUID.randomUUID().toString());

        }
        return "OK";
    }

    @GetMapping("/batchGet")
    public Object batchGet(String... keys) {
        List<String> keysList = Arrays.asList(keys);
        return redisOperator.mget(keysList);
    }




}
