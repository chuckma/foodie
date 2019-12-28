package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UserVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.JSONResult;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.UUID;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Controller
public class BaseController {

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    String imoocUserId = "2011596-1364502989";
    String password = "aw15-pe1d-vbd1-0p0e";

    @Autowired
    private RedisOperator redisOperator;

    // 微信支付成功->支付中心->foodie 平台
    //                    |-> 回调通知的url
    // String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
//    String payReturnUrl = "192.168.5.201:8088/foodie-api/orders/notifyMerchantOrderPaid";

    /**
     * 如果用内网穿透的方式，必须要加项目入口地址 foodie-api，内网穿透时注意映射的端口是否一致。
     */
    String payReturnUrl = "http://a9858840.ngrok.io/foodie-api/orders/notifyMerchantOrderPaid";

    // 支付中心调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";


    // 用户头像上传的位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "lucasma" +
            File.separator + "developer" +
            File.separator + "images" +
            File.separator + "foodie" +
            File.separator + "faces";




    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @return
     */
    public JSONResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JSONResult.errorMsg("订单不存在！");
        }
        return JSONResult.ok(order);
    }


    public UserVO conventUsersVO(Users user) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN+":"+user.getId(),uniqueToken);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }
}
