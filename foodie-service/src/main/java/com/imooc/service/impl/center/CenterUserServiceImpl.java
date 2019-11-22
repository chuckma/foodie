package com.imooc.service.impl.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.bo.center.PasswordBO;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author mcg
 * @create 2019-11-19-15:33
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoContainsPwd(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        return users;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return  queryUserInfo(userId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return  queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updatePassword(String userId, PasswordBO passwordBO) {
        Users updateUser = new Users();
        try {
            String md5Pwd = MD5Utils.getMD5Str(passwordBO.getPassword());
            updateUser.setPassword(md5Pwd);
            updateUser.setId(userId);
            updateUser.setUpdatedTime(new Date());
            usersMapper.updateByPrimaryKeySelective(updateUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
