package com.imooc.pojo.vo;

/**
 * @author mcg
 * @create 2019-11-18-15:40
 */
public class OrderVO {

    private MerchantOrdersVO merchantOrdersVO;
    private String orderId;


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
