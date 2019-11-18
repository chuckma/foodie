package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Api(value = "订单相关的api 接口", tags = {"订单相关的api 接口"})
@RestController
@RequestMapping("orders")
public class OrderController extends BaseController{


    public static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        System.out.println(submitOrderBO);
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return JSONResult.errorMsg("支付方式不支持");
        }
        //1. 创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        //2. 创建订单以后，移除购物车中已经结算的（已经提交的）商品

        /** 购物车
         *  1001
         *  1002  购买
         *  1003  购买
         *  1004
         */
        // TODO 整合 redis 之后，清除购物车已结算的商品，同步到 前端 cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        //3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        //

        return JSONResult.ok(orderId);
    }


}
