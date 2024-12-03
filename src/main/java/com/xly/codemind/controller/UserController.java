package com.xly.codemind.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.xly.codemind.common.BaseResponse;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.request.UserLoginRequest;
import com.xly.codemind.model.request.UserRegisterRequest;
import com.xly.codemind.model.vo.LoginUserVO;
import com.xly.codemind.service.UserService;
import com.xly.codemind.utils.ActionResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @className UserController
 * @description 测试使用
 **/

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "注册")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ActionResultUtil.fail(ErrorCode.PARAMS_NULL_ERROR);
        }
        //获取账号、密码、确认密码
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkedPassword = userRegisterRequest.getCheckedPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkedPassword)){
            return ActionResultUtil.fail(ErrorCode.PARAMS_NULL_ERROR);
        }
        Long userId = userService.userRegister(userAccount, userPassword, checkedPassword);
        return ActionResultUtil.success(userId);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "登录")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"账号或密码有误，请重试!");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"账号或密码有误，请重试!");
        }
        LoginUserVO userVO = userService.userLogin(userAccount, userPassword, request);
        return ActionResultUtil.success(userVO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录",notes = "退出登录(注销)")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数有误!");
        }
        Boolean result = userService.userLogout(request);
        return ActionResultUtil.success(result);
    }

}
