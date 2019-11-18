package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.bo.SubmitOrderBO;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:16
 * <p>
 * 订单
 **/

public interface OrderService {


    /**
     * 创建订单相关信息
     * @param submitOrderBO
     */
    public String  createOrder(SubmitOrderBO submitOrderBO);
}
