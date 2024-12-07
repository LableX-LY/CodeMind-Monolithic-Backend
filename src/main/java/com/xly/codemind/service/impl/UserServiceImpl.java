package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.common.IdWorker;
import com.xly.codemind.constant.UserConstant;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.mapper.UserMapper;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.vo.LoginUserVO;
import com.xly.codemind.model.vo.UserVO;
import com.xly.codemind.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author x-ly
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-12-01 16:36:16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "CodeMind";

    @Resource
    private UserMapper userMapper;


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkedPassword) {

        //参数不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "请求参数为空");
        }
        //校验账号和密码的合法性
        verifyUserAccountAndUserPassword(userAccount, userPassword);
        //密码和二次确认密码必须相同
        if (!userPassword.equals(checkedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致!");
        }
        //账号不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        Long userNum = userMapper.selectCount(userQueryWrapper);
        if (userNum > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在,请重新输入!");
        }
        //密码MD5加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //保存用户,昵称默认为账号；头像、用户标签、用户简介有默认值
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        user.setUserName(userAccount);
        user.setUserAvatar("https://img1.baidu.com/it/u=534429813,2995452219&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800");
        user.setUserTags("这个人很无聊，什么标签都没有");
        user.setUserProfile("这个人很懒，什么都没有写");
        user.setUserRole("user");
        user.setUserStatus(0);
        long id = IdWorker.getInstance().nextId();
        if (id < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统内部异常,id生成失败!");
        }
        user.setId(id);
        int insert = userMapper.insert(user);
        if (insert < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统内部异常，请稍后重试!");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"账号或密码为空，请重新输入!");
        }
        //校验账号和密码的合法性
        verifyUserAccountAndUserPassword(userAccount, userPassword);
        //密码MD5加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //账号密码匹配且没有被Ban
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount).eq("userPassword",encryptedPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误!");
        }
        //查询账号状态
        if (user.getUserStatus() == 1) {
            throw new BusinessException(ErrorCode.USER_BAN_ERROR,"账号被禁用,请联系管理员!");
        }
        //保存登录态到session
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        //返回脱敏用户
        return userToLoginUser(user);
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户未登录!");
        }
        //清空Session
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (attribute == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录!");
        }
        User loginUser = (User) attribute;
        return loginUser;
    }

    /**
     * 内部方法，将user转换为脱敏用户loginUser
     * @param user 完整的用户
     * @return 脱敏用户
     */
    private LoginUserVO userToLoginUser(User user) {
        LoginUserVO loginUser = new LoginUserVO();
        loginUser.setId(user.getId());
        loginUser.setUserAccount(user.getUserAccount());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserAvatar(user.getUserAvatar());
        loginUser.setUserTags(user.getUserTags());
        loginUser.setUserProfile(user.getUserProfile());
        loginUser.setUserBirthday(user.getUserBirthday());
        loginUser.setUserGender(user.getUserGender());
        loginUser.setUserEmail(user.getUserEmail());
        return loginUser;
    }

    /**
     * 内部方法，校验账号的密码的合法性
     * @param userAccount 账号
     * @param userPassword 密码
     */
    private void verifyUserAccountAndUserPassword(String userAccount, String userPassword) {
        //账号长度大于8小于18，且不包含特殊字符
        if (userAccount.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于8位!");
        }
        if (userAccount.length() > 18) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度超过18位!");
        }
        String validPatternUserAccount = "[`~!#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~!#¥%,,,,,,&*（）——+｜{}【】'; ;]";
        Matcher userAccountMatcher = Pattern.compile(validPatternUserAccount).matcher(userAccount);
        if(userAccountMatcher.find()){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含禁止使用的特殊字符!");
        }
        //密码长度大于8小于18，且不包含特殊字符
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8位!");
        }
        if (userPassword.length() > 18) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度超过18位!");
        }
        String validPatternUserPassword = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~!@#¥%,,,,,,&*（）——+｜{}【】'; ;]";
        Matcher userPasswordmatcher = Pattern.compile(validPatternUserPassword).matcher(userPassword);
        if(userPasswordmatcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码包含禁止使用的特殊字符!");
        }
    }

}




