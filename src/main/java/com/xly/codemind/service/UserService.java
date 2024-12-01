package com.xly.codemind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xly.codemind.model.bean.User;

import java.util.List;


/**
* @author x-ly
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-12-01 16:36:16
*/
public interface UserService extends IService<User> {

    List<User> getUserList();

}
