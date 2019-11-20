package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Controller
public class BaseController {

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    String imoocUserId = "2011596-1364502989";
    String password = "aw15-pe1d-vbd1-0p0e";

    // 微信支付成功->支付中心->foodie 平台
    //                    |-> 回调通知的url
    // String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    String payReturnUrl = "http://iv5uy2.natappfree.cc/orders/notifyMerchantOrderPaid";

    // 支付中心调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";


    // 用户头像上传的位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "lucasma" +
            File.separator + "developer" +
            File.separator + "images" +
            File.separator + "foodie" +
            File.separator + "faces";


}
