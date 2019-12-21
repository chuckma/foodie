package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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


    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private RestTemplate restTemplate; // 可以向别的系统发起http请求

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

        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());

        if (StringUtils.isBlank(shopCartJson)) {
            return JSONResult.errorMsg("购物车数据有误");
        }

        List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);


        //1. 创建订单
        OrderVO orderVO = orderService.createOrder(shopCartBOList,submitOrderBO);
        String orderId = orderVO.getOrderId();


        //2. 创建订单以后，移除购物车中已经结算的（已经提交的）商品

        /** 购物车
         *  1001
         *  1002  购买
         *  1003  购买
         *  1004
         */

        // 清理覆盖现有的 redis 汇总的数据
        shopCartBOList.removeAll(orderVO.getTobeRemovedShopCartList());

        redisOperator.set(FOODIE_SHOPCART+":"+submitOrderBO.getUserId(), JsonUtils.objectToJson(shopCartBOList));

        //  整合 redis 之后，清除购物车已结算的商品，同步到 前端 cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartBOList), true);

        //3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为方便测试，所有的支付金额都默认为 1 分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", imoocUserId);
        headers.add("password", password);

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity,
                JSONResult.class);

        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }
        return JSONResult.ok(orderId);
    }


    /**
     * 支付中心接受到微信支付成功之后，调用本接口，修改订单状态
     * @param merchantOrderId
     * @return
     */
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {

        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }


    /**
     * 用户提交订单后页面轮训查询订单支付状态
     * @param orderId
     * @return
     */
    @PostMapping("/getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);

        return JSONResult.ok(orderStatus);
    }

}
