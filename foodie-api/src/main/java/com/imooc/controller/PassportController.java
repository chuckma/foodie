package com.imooc.controller;

import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    /**
     * RequestParam 代表是一种请求类型的参数，而不是路径参数
     *
     * @param username
     * @return
     */
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


    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO) {
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
        userService.createUser(userBO);
        return JSONResult.ok();
    }

}
