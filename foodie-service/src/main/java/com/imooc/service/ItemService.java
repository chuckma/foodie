package com.imooc.service;

import com.imooc.pojo.*;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:16
 * <p>
 * 轮播图
 **/

public interface ItemService {


    /**
     * 根据 商品id 查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);


    /**
     * 根据商品id 查询图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);


    /**
     * 根据商品id 查询规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);


    /**
     * 根据商品id 查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);
}
