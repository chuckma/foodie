package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:16
 * <p>
 * 轮播图
 **/

public interface CarouselService {


    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}
