package com.xly.codemind.model.vo;


import lombok.Data;

/**
 * @author X-LY。
 * @version 1.0
 * @className LoginUserVO
 * @description 已登录用户视图（脱敏）
 **/
@Data
public class LoginUserVO {

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户性别
     */
    private Integer userGender;

    /**
     * 用户生日
     */
    private String userBirthday;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 用户简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;

}
