package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author mcg
 * @Date 2019/11/15 21:18
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO  createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();

        // 邮费设置为 0 （包邮）
        Integer postAmount = 0;
        UserAddress address = addressService.queryUserAddress(userId, addressId);

        String orderId = sid.nextShort();
        // 1. 新订单数据保存
        Orders newOrders = new Orders();
        newOrders.setId(orderId);
        newOrders.setUserId(userId);
        newOrders.setReceiverName(address.getReceiver());
        newOrders.setReceiverMobile(address.getMobile());
        newOrders.setReceiverAddress(address.getProvince() + " " + address.getCity() + " "
                + address.getDistrict() + " " + address.getDetail());


        newOrders.setPostAmount(postAmount);
        newOrders.setPayMethod(payMethod);
        newOrders.setLeftMsg(leftMsg);
        newOrders.setIsDelete(YesOrNo.NO.type);
        newOrders.setIsComment(YesOrNo.NO.type);
        newOrders.setCreatedTime(new Date());
        newOrders.setUpdatedTime(new Date());

        // 2. 循环根据 itemSpecIds 保存订单商品信息
        String[] itemSpecArr = itemSpecIds.split(",");
        Integer totalAmount = 0; // 商品原价总计
        Integer realPayAmount = 0;// 优惠后的实际支付累计
        for (String itemSpecId : itemSpecArr) {
            // TODO 商品总价=单价*下单数量，这里设置为1，后续要从 redis 里获取购物车信息 buyCounts
            int buyCounts = 1;
            // 2.1  根据规格Id ，查询规格信息，主要是价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据规格ID 获取商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            //查询商品主图url
            String imgUrl = itemService.queryItemMainImgById(itemId);
            // 2.3 循环保存子订单数据
            String subOrderId = sid.nextShort();
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemName(items.getItemName());
            subOrderItems.setItemImg(imgUrl);
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(itemSpecId); // 该规格下的商品Id
            subOrderItems.setItemSpecName(itemsSpec.getName()); // 商品名称
            subOrderItems.setPrice(itemsSpec.getPriceDiscount()); // 商品的优惠价

            orderItemsMapper.insert(subOrderItems);

            // 2.4 用户提交订单以后，减库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }
        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrders);
        // 3. 保存订单状态
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {

        OrderStatus paidOrderStatus = new OrderStatus();
        paidOrderStatus.setOrderStatus(orderStatus);
        paidOrderStatus.setOrderId(orderId);
        paidOrderStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidOrderStatus);
    }


}
