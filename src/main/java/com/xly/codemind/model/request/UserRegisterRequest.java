package com.xly.codemind.model.request;

import lombok.Data;

/**
 * @author X-LY。
 * @version 1.0
 * @className UserRegisterRequest
 * @description
 **/

@Data
public class UserRegisterRequest {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkedPassword;

}
