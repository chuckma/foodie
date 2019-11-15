package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

/**
 * @author mcg
 * @create 2019-11-15-10:32
 */

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUserNameIsExist(String username);


    /**
     * 创建用户
     * @return
     */
    public Users createUser(UserBO userBO);
}
