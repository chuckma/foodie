package com.imooc.service;

import com.imooc.pojo.Category;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:42
 **/
public interface CategoryService {


    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();
}
