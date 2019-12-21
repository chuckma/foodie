package com.imooc.controller;

import com.google.common.base.Strings;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.JSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {


    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        List<Carousel> list;
        String carouselStr = redisOperator.get("carousel");
        if (StringUtils.isBlank(carouselStr)) {
            list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }

        return JSONResult.ok(list);
    }

    /**
     * 首页分类需求
     * 1. 第一次刷新主页查询大分类，渲染到首页
     * 2. 鼠标移动到大分类上，则加载其子分类的内容，如果已经存在子分类，就不需要加载（懒加载）
     */


    @ApiOperation(value = "获取一级商品分类",notes = "获取一级商品分类",httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats() {

        List<Category> list;
        String catsStr = redisOperator.get("cats");
        if (StringUtils.isBlank(catsStr)) {
            list = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats", JsonUtils.objectToJson(list));
        }else {
            list = JsonUtils.jsonToList(catsStr, Category.class);
        }
        return JSONResult.ok(list);
    }




    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list;
        String subCatStr = redisOperator.get("subCat");
        if (StringUtils.isBlank(subCatStr)) {
             list = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat", JsonUtils.objectToJson(list));
        }else {
            list = JsonUtils.jsonToList(subCatStr, CategoryVO.class);
        }

        return JSONResult.ok(list);
    }



    @ApiOperation(value = "查询每个一级分类下的最新 6 条商品数据",notes = "查询每个一级分类下的最新 6 条商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);

        return JSONResult.ok(list);
    }
}
