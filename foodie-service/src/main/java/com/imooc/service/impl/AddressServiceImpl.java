package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.CarouselMapper;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.service.CarouselService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/15 21:18
 **/
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper  userAddressMapper;
    @Autowired
    private Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);

        return userAddressMapper.select(ua);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为 默认地址
        List<UserAddress> userAddresses = this.queryAll(addressBO.getUserId());
        Integer isDefault = 0;
        if (userAddresses == null || userAddresses.isEmpty() || userAddresses.size() == 0) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        // 2. 保存
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);
        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(newAddress);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, updateAddress);
        updateAddress.setId(addressId);
        updateAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);
        userAddressMapper.delete(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {

       // 1 查找默认地址，设置为非默认

        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> userAddressList = userAddressMapper.select(queryAddress);
        for (UserAddress address : userAddressList) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }
        // 2 根据地址id 设置为默认地址

        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        defaultAddress.setUserId(userId);
        defaultAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }
}