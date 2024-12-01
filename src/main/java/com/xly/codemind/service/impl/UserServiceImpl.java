package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xly.codemind.mapper.UserMapper;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author x-ly
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-12-01 16:36:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }
}




