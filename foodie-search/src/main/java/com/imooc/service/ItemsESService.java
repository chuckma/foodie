package com.imooc.service;

import com.imooc.utils.PagedGridResult;

/**
 * @Author mcg
 * @Date 2020/1/16 20:56
 **/
public interface ItemsESService {


    public PagedGridResult searchItems(String keywords, String sort, int page, int pageSize);
}
