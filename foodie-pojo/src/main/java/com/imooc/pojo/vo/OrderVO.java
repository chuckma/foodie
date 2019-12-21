package com.imooc.pojo.vo;

import java.util.List;

import com.imooc.pojo.bo.ShopCartBO;


/**
 * @author mcg
 * @create 2019-11-18-15:40
 */
public class OrderVO {

    private MerchantOrdersVO merchantOrdersVO;
    private String orderId;


    private List<ShopCartBO> tobeRemovedShopCartList;

    public List<ShopCartBO> getTobeRemovedShopCartList() {
        return tobeRemovedShopCartList;
    }

    public void setTobeRemovedShopCartList(List<ShopCartBO> tobeRemovedShopCartList) {
        this.tobeRemovedShopCartList = tobeRemovedShopCartList;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
