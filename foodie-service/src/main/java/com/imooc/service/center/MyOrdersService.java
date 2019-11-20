package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.utils.PagedGridResult;

/**
 * @author mcg
 * @create 2019-11-19-15:32
 */
public interface MyOrdersService {


    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus,
                                         Integer page, Integer pageSize);



    /**
     * @Description: 订单状态 --> 商家发货
     */
    public void updateDeliverOrderStatus(String orderId);
}

