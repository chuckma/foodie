package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:16
 * <p>
 **/

public interface AddressService {


    /**
     * 根据用户id 查询收货地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);


    /**
     * 用户新增地址
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);


    /**
     * 用户修改收货地址
     * @param addressBO
     */
    public void updateUserAddress(AddressBO addressBO);


    /**
     * 删除用户收货地址
     * @param userId
     * @param addressId
     */
    public void deleteUserAddress(String userId, String addressId);


    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    public void updateUserAddressToBeDefault(String userId, String addressId);

}
