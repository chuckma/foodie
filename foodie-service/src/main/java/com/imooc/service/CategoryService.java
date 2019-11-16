package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

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


    /**
     * 根据一级分类 id 查询子分类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);


    /**
     * 查询首页每个一级分类下的 6 条最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
