package com.xly.codemind.service.impl;

import cn.hutool.core.lang.Assert;
import com.xly.codemind.mapper.UserMapper;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.vo.LoginUserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author X-LY。
 * @version 1.0
 * @className UserServiceImplMockTest
 * @description
 **/
class UserServiceImplMockTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserMapper userMapper;

    @Mock
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 用户注册，测试成功
     */
    @Test
    void userRegisterSuccess() {
        String userAccount = "17736396716";
        String userPassword = "Xly20020618";
        String checkedPassword = "Xly20020618";
        when(userMapper.selectCount(any())).thenReturn(0L);
        when(userMapper.insert(any())).thenReturn(1);
        Long userId = userServiceImpl.userRegister(userAccount, userPassword, checkedPassword);
        assertNotNull(userId);
    }

    /**
     * 用户注册，测试密码和确认密码不一致
     */
    @Test
    void userRegisterDifferentPassword() {
        String userAccount = "17736396716";
        String userPassword = "Xly20020618";
        String checkedPassword = "Xly20020618.";
        when(userMapper.selectCount(any())).thenReturn(0L);
        when(userMapper.insert(any())).thenReturn(1);
        Long userId = userServiceImpl.userRegister(userAccount, userPassword, checkedPassword);
    }

    /**
     * 用户注册，账号长度不符
     */
    @Test
    void userRegisterUnValidUserAccount() {
        String userAccount = "2024";
        String userPassword = "Xly20020618";
        String checkedPassword = "Xly20020618.";
        when(userMapper.selectCount(any())).thenReturn(0L);
        when(userMapper.insert(any())).thenReturn(1);
        Long userId = userServiceImpl.userRegister(userAccount, userPassword, checkedPassword);
    }

    /**
     * 用户注册，密码包含特殊字符
     */
    @Test
    void userRegisterUnValidUserPassword() {
        String userAccount = "17736396716";
        String userPassword = "Xly20020618~";
        String checkedPassword = "Xly20020618~";
        when(userMapper.selectCount(any())).thenReturn(0L);
        when(userMapper.insert(any())).thenReturn(1);
        Long userId = userServiceImpl.userRegister(userAccount, userPassword, checkedPassword);
    }

    /**
     * 用户登录测试，测试账号密码匹配失败
     */
    @Test
    void userLoginFailed() {
        String userAccount = "17736396716";
        String userPassword = "Xly20020618";
        when(userMapper.selectOne(any())).thenReturn(null);
        LoginUserVO loginUserVO = userServiceImpl.userLogin(userAccount, userPassword, request);
    }

    /**
     * 用户登录测试，测试账号被禁用
     */
    @Test
    void userLoginBanned() {
        String userAccount = "17736396716";
        String userPassword = "Xly20020618";
        User user = new User();
        user.setUserStatus(1);
        when(userMapper.selectOne(any())).thenReturn(user);
        LoginUserVO loginUserVO = userServiceImpl.userLogin(userAccount, userPassword, request);
    }


}