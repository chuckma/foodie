package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

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
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);


    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);


    /**
     * 查询订单状态
     * @param orderId
     */
    public OrderStatus queryOrderStatusInfo(String orderId);


    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
