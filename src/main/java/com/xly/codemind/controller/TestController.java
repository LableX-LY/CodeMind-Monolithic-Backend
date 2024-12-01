package com.xly.codemind.controller;

import com.xly.codemind.model.bean.User;
import com.xly.codemind.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @className TestController
 * @description 测试使用
 **/

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户信息管理接口")
public class TestController {

    @Resource
    private UserService userService;

    @GetMapping("/search")
    @ApiOperation(value = "获取用户信息",notes = "获取所有用户信息")
    public String search() {
        List<User> userList = userService.getUserList();
        if (userList.isEmpty()){
            return "数据库连接成功！查询结果为空！";
        } else {
            return "应该不会执行到这里";
        }
    }

}
