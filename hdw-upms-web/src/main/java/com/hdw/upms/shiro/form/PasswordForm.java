package com.hdw.upms.shiro.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description 密码表单
 * @Author TuMinglong
 * @Date 2018/6/11 17:07
 */
@ApiModel(value = "密码信息")
public class PasswordForm implements Serializable {
    /**
     * 原密码
     */
    @ApiModelProperty(value = "原密码")
    private String password;
    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
