package com.imooc.controller;

import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@ApiIgnore
@RestController
public class StuFooController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Object getStu(int id) {

        return stuService.getStuInfo(id);
    }


    @PostMapping("/saveStu")
    public Object saveStu() {

        stuService.saveStu();
        return "OK";
    }

    @PostMapping("/updateStu")
    public Object saveStu(int id) {

        stuService.updateStu(id);
        return "OK";
    }


    @PostMapping("/deleteStu")
    public Object deleteStu(int id) {

        stuService.deleteStu(id);
        return "OK";
    }

}
