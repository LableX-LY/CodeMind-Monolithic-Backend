package com.xly.codemind.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/7 15:09
 * @description 用户视图（脱敏）
 **/

@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}

