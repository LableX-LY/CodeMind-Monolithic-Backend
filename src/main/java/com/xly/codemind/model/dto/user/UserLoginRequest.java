package com.xly.codemind.model.dto.user;

/**
 * @author X-LY。
 * @version 1.0
 * @className UserLoginRequest
 * @description 用户登录请求类
 **/
public class UserLoginRequest {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    public String getUserAccount() {
        return userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
