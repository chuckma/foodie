package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.bo.center.PasswordBO;

/**
 * @author mcg
 * @create 2019-11-19-15:32
 */
public interface CenterUserService {

    /**
     * 用户id 查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);


    public Users queryUserInfoContainsPwd(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);


    /**
     * 用户头像更新
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFace(String userId, String faceUrl);


    /**
     * 修改用户密码
     * @param userId
     * @param passwordBO
     */
    public void updatePassword(String userId, PasswordBO passwordBO);

}
