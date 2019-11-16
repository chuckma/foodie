package com.imooc.pojo.vo;

/**
 * @Author mcg
 * @Date 2019/11/16 13:44
 *
 * 6 个最新商品的简单数据类型
 **/

public class SimpleItemVO {


    private String  itemId;
    private String itemName;
    private String itemUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
