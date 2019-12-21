package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.utils.JSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Api(value = "购物车接口",tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("shopcart")
public class ShopCartController extends BaseController{


    @Autowired
    private RedisOperator redisOperator;



    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestParam String userId,
                          @RequestBody ShopCartBO ShopCartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }

        //  前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到 redis 缓存
        // 需要判断当前购物车中存在的相同的商品，如果一样，就要进行数量的累加

        System.out.println(ShopCartBO);
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        List<ShopCartBO> shopCartBOList;
        if (StringUtils.isNotBlank(shopCartJson)) {
            // redis中已经有购物车了
            shopCartBOList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopCartBO sc: shopCartBOList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(ShopCartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + ShopCartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopCartBOList.add(ShopCartBO);
            }
        } else {
            // redis中没有购物车
            shopCartBOList = new ArrayList<>();
            // 直接添加到购物车中
            shopCartBOList.add(ShopCartBO);
        }

        // 覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartBOList));

        return JSONResult.ok();

    }



    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult add(@RequestParam String userId,
                          @RequestParam String  itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        if (StringUtils.isBlank(userId)|| StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }

        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除 redis 购物车中的商品
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopCartJson)) {
            // redis中已经有购物车了
            List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopCartBO sc: shopCartBOList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopCartBOList.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartBOList));
        }

        return JSONResult.ok();

    }
}


