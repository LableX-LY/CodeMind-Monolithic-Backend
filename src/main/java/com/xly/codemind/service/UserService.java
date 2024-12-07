package com.xly.codemind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.vo.LoginUserVO;
import com.xly.codemind.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;


/**
* @author x-ly
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-12-01 16:36:16
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkedPassword 确认密码
     * @return 注册成功后的用户id
     */
    Long userRegister(String userAccount, String userPassword,String checkedPassword);

    /**
     * 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @param request HttpServletRequest
     * @return 用户登录视图（脱敏）
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户退出登录（注销）
     * @param request HttpServletRequest
     * @return 是否成功退出登录
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     * @param user 用户
     * @return 脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取登录用户的信息
     * @param request http
     * @return 当前Session中的用户信息
     */
    User getLoginUser(HttpServletRequest request);


}
