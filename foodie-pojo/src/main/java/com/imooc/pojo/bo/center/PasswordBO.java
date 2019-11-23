package com.imooc.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author mcg
 * @Date 2019/11/22 22:11
 **/

@ApiModel(value = "用户密码对象",description = "用户修改密码的数据封装在此")
public class PasswordBO {


    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "用户旧密码", name = "oldPassword", required = true)
    private String oldPassword;


    @NotBlank(message = "新密码不能为空")
    @Length(min = 2,message = "密码长度不能少于 6 位")
    @ApiModelProperty(value = "新密码", name = "password", required = true)
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Length(min = 2,message = "密码长度不能少于 6 位")
    @ApiModelProperty(value = "确认密码不能为空", name = "confirmPassword", required = true)
    private String confirmPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
