package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static com.imooc.controller.BaseController.FOODIE_SHOPCART;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Api(value = "注册登录",tags = {"用于用户注册登录的接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * RequestParam 代表是一种请求类型的参数，而不是路径参数
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username) {
        // 用户名不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        boolean exist = userService.queryUserNameIsExist(username);
        if (exist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "注册用户",notes = "注册用户",httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO,HttpServletRequest request,
                             HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String comfirmPwd = userBO.getConfirmPassword();

        // 0 用户名和密码不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(comfirmPwd)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 1 查询用户名是否存在
        boolean exist = userService.queryUserNameIsExist(username);
        if (exist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        // 2 密码长度不能太短
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6");
        }
        // 3 判断 2 次密码是否一致
        if (!password.equals(comfirmPwd)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }
        // 4 实现注册
        Users userResult = userService.createUser(userBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        synchShopCartData(userResult.getId(), request, response);
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0 用户名和密码不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }


        // 4 实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return JSONResult.errorMsg("用户名或者密码不正确");
        }
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        // TODO 生成用户 token 存入 redis 会话
        //  同步购物车数据
        synchShopCartData(userResult.getId(), request, response);
        return JSONResult.ok(userResult);
    }


    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopCartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {

        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        // 从redis中获取购物车
        String shopCartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // 从cookie中获取购物车
        String shopCartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopCartJsonRedis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopCartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopCartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopCartStrCookie)) {

                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中
                 */

                List<ShopCartBO> shopCartListRedis = JsonUtils.jsonToList(shopCartJsonRedis, ShopCartBO.class);
                List<ShopCartBO> shopCartListCookie = JsonUtils.jsonToList(shopCartStrCookie, ShopCartBO.class);

                // 定义一个待删除list
                List<ShopCartBO> pendingDeleteList = new ArrayList<>();

                for (ShopCartBO redisShopCart : shopCartListRedis) {
                    String redisSpecId = redisShopCart.getSpecId();

                    for (ShopCartBO cookieShopCart : shopCartListCookie) {
                        String cookieSpecId = cookieShopCart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopCart.setBuyCounts(cookieShopCart.getBuyCounts());
                            // 把cookieShopCart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopCart);
                        }

                    }
                }

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopCartListCookie.removeAll(pendingDeleteList);

                // 合并两个list
                shopCartListRedis.addAll(shopCartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartListRedis));
            } else {
                // redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopCartJsonRedis, true);
            }

        }
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,HttpServletRequest request,
                             HttpServletResponse response) {

        // 清除 cookie
        CookieUtils.deleteCookie(request, response, "user");
        //  TODO 分布式会话中需要清除用户数据
        //  退出登录，清空 cookie 购物车
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        return JSONResult.ok();
    }

}
