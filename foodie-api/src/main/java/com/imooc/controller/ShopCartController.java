package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Api(value = "购物车接口",tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("shopcart")
public class ShopCartController {


    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestParam String userId,
                          @RequestBody ShopCartBO shopCartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }

        // TODO 前端用户在未登录的情况下，添加商品到购物车，会同时在后端同步购物车到 redis 缓存

        System.out.println(shopCartBO);

        return JSONResult.ok();

    }



    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult add(@RequestParam String userId,
                          @RequestParam String  itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        if (StringUtils.isBlank(userId)|| StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }

        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品


        return JSONResult.ok();

    }
}


