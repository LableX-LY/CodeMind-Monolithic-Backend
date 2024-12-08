package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.common.IdWorker;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.dto.question.AdminQueryQuestionRequest;
import com.xly.codemind.model.dto.question.JudgeConfig;
import com.xly.codemind.model.dto.question.UserQueryQuestionRequest;
import com.xly.codemind.model.vo.AdminQuestionVO;
import com.xly.codemind.model.vo.UserQuestionVO;
import com.xly.codemind.service.QuestionService;
import com.xly.codemind.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xly.codemind.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author x-ly
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2024-12-01 16:36:16
*/

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService{

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @Override
    public Long addQuestion(String questionTitle, String questionContent, String questionAnswer, String questionTagsJsonString, String judgeCaseObjectJsonString, String judgeConfigObjectJsonString, int questionDifficulty, HttpServletRequest request) {
        //校验信息
        if (StringUtils.isAnyBlank(questionTitle,questionContent,questionTagsJsonString,judgeCaseObjectJsonString,judgeConfigObjectJsonString,questionAnswer)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        Question question = new Question();
        long id = IdWorker.getInstance().nextId();
        if (id == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"IdWorker发生错误,id生成失败!");
        }
        // todo 只有管理员才可以添加题目
        question.setQuestionTitle(questionTitle);
        question.setQuestionContent(questionContent);
        question.setQuestionAnswer(questionAnswer);
        question.setQuestionTags(questionTagsJsonString);
        question.setJudgeCase(judgeCaseObjectJsonString);
        question.setJudgeConfig(judgeConfigObjectJsonString);
        question.setQuestionDifficulty(questionDifficulty);
        question.setCreateUser(userService.getLoginUser(request).getId());
        //题目状态、题目提交数、题目通过数、创建时间、更新时间数据库有默认值
        int insertNum = questionMapper.insert(question);
        if (insertNum < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"添加失败,请稍后重试");
        }
        return question.getId();
    }

    @Override
    public Boolean deleteQuestion(long questionId) {
        // todo 只有管理员才可以删除题目
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目id为空!");
        }
        //查询题目是否存在
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目不存在!");
        }
        if (question.getQuestionStatus() == 1) {
            throw new BusinessException(ErrorCode.QUESTION_BAN_ERROR,"题目被禁用,无法删除!");
        }
        int deleteNum = questionMapper.deleteById(question);
        if (deleteNum < 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目删除失败,请稍后重试!");
        }
        return true;
    }

    @Override
    public Boolean editQuestion(long questionId,String questionTitle, String questionContent, String questionAnswer, String questionTagsJsonString, String judgeCaseObjectString, String judgeConfigObjectString, int questionDifficulty, Integer questionStatus, HttpServletRequest request) {
        //校验信息
        if (StringUtils.isAnyBlank(questionTitle,questionContent,questionAnswer,questionTagsJsonString,judgeCaseObjectString,judgeConfigObjectString)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        if (questionStatus  == null || questionStatus < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置有误!");
        }
        //题目存在才可以修改
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目不存在!");
        }
        // todo 只有管理员才可以修改题目
        if (!compareQuestionContent(questionTitle, questionContent, questionAnswer, questionTagsJsonString, judgeCaseObjectString, judgeConfigObjectString, questionDifficulty, questionStatus,question)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"至少要有一处修改!");
        }
        //获取修改人
        Object sessionUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (sessionUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录!");
        }
        User loginUser = (User) sessionUser;
        question.setQuestionTitle(questionTitle);
        question.setQuestionContent(questionContent);
        question.setQuestionAnswer(questionAnswer);
        question.setQuestionTags(questionTagsJsonString);
        question.setJudgeCase(judgeCaseObjectString);
        question.setJudgeConfig(judgeConfigObjectString);
        question.setEditUser(loginUser.getId());
        int editedNum = questionMapper.updateById(question);
        if (editedNum < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"修改失败,请稍后重试!");
        }
        return true;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     * @param userQueryQuestionRequest 用户题目查询请求
     * @return 用户视角下的题目查询包装类
     */
    @Override
    public QueryWrapper<Question> getUserQueryWrapper(UserQueryQuestionRequest userQueryQuestionRequest) {
        QueryWrapper<Question> userQuestionQueryWrapper = new QueryWrapper<>();
        if (userQueryQuestionRequest == null) {
            return userQuestionQueryWrapper;
        }
        //获取查询条件
//        long questionId = userQueryQuestionRequest.getId();
        String questionTitle = userQueryQuestionRequest.getQuestionTitle();
        String questionContent = userQueryQuestionRequest.getQuestionContent();
        List<String> questionTags = userQueryQuestionRequest.getQuestionTags();
        String questionTagsJson = GSON.toJson(questionTags);
        JudgeConfig judgeConfig = userQueryQuestionRequest.getJudgeConfig();
        String judgeConfigJson = GSON.toJson(judgeConfig);
        Integer questionDifficulty = userQueryQuestionRequest.getQuestionDifficulty();
        Integer submitNum = userQueryQuestionRequest.getSubmitNum();
        Integer acceptedNum = userQueryQuestionRequest.getAcceptedNum();
        String sortField = userQueryQuestionRequest.getSortField();
        String sortOrder = userQueryQuestionRequest.getSortOrder();

        //拼接查询条件
//        questionQueryWrapper.like(questionId > 0,"id",questionId);
        userQuestionQueryWrapper.like(StringUtils.isNotBlank(questionTitle),"questionTitle",questionTitle);
        userQuestionQueryWrapper.like(StringUtils.isNotBlank(questionContent),"questionContent",questionContent);
        if (CollectionUtils.isNotEmpty(questionTags)) {
            for (String tag : questionTags) {
                userQuestionQueryWrapper.like("questionTags", "\"" + tag + "\"");
            }
        }

        userQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(judgeConfig),"judgeConfig",judgeConfigJson);
        userQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(questionDifficulty),"questionDifficulty",questionDifficulty);
        userQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(submitNum),"submitNum",submitNum);
        userQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(acceptedNum),"acceptedNum",acceptedNum);
        return userQuestionQueryWrapper;
    }

    @Override
    public Page<UserQuestionVO> getUserQuestionVOByPage(Page<Question> userQuestionPageQueryWrapper) {
        List<Question> questionList = userQuestionPageQueryWrapper.getRecords();
        Page<UserQuestionVO> userQuestionVOPage = new Page<>(userQuestionPageQueryWrapper.getCurrent(), userQuestionPageQueryWrapper.getSize(), userQuestionPageQueryWrapper.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return userQuestionVOPage;
        }
//        // 1. 关联查询用户信息
//        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
//        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
//                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<UserQuestionVO> questionVOList = questionList.stream().map(question -> {
            UserQuestionVO questionVO = UserQuestionVO.objToVo(question);
//            Long userId = question.getUserId();
//            User user = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        userQuestionVOPage.setRecords(questionVOList);
        return userQuestionVOPage;
    }

    /**
     * 获取查询包装类（管理员根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     * @param adminQueryQuestionRequest 管理员题目查询请求
     * @return 管理员视角下的题目查询包装类
     */
    @Override
    public QueryWrapper<Question> getAdminQueryWrapper(AdminQueryQuestionRequest adminQueryQuestionRequest) {
        QueryWrapper<Question> adminQuestionQueryWrapper = new QueryWrapper<>();
        if (adminQueryQuestionRequest == null) {
            return adminQuestionQueryWrapper;
        }
        //获取查询条件
        Long questionId = adminQueryQuestionRequest.getId();
        String questionTitle = adminQueryQuestionRequest.getQuestionTitle();
        String questionContent = adminQueryQuestionRequest.getQuestionContent();
        List<String> questionTags = adminQueryQuestionRequest.getQuestionTags();
        JudgeConfig judgeConfig = adminQueryQuestionRequest.getJudgeConfig();
        String judgeConfigJson = GSON.toJson(judgeConfig);
        //使用包装类，因为包装类允许为空（前端不传递这个字段，即不通过这个条件查询）
        Integer questionDifficulty = adminQueryQuestionRequest.getQuestionDifficulty();
        Integer submitNum = adminQueryQuestionRequest.getSubmitNum();
        Integer acceptedNum = adminQueryQuestionRequest.getAcceptedNum();
        Long createUser = adminQueryQuestionRequest.getCreateUser();
        Long editUser = adminQueryQuestionRequest.getEditUser();
        Integer questionStatus = adminQueryQuestionRequest.getQuestionStatus();
        Date createTime = adminQueryQuestionRequest.getCreateTime();
        Date updateTime = adminQueryQuestionRequest.getUpdateTime();
        String sortField = adminQueryQuestionRequest.getSortField();
        String sortOrder = adminQueryQuestionRequest.getSortOrder();

        //拼接查询条件
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(questionId),"id",questionId);
        adminQuestionQueryWrapper.like(StringUtils.isNotBlank(questionTitle),"questionTitle",questionTitle);
        adminQuestionQueryWrapper.like(StringUtils.isNotBlank(questionContent),"questionContent",questionContent);
        if (CollectionUtils.isNotEmpty(questionTags)) {
            for (String tag : questionTags) {
                adminQuestionQueryWrapper.like("questionTags", "\"" + tag + "\"");
            }
        }
        adminQuestionQueryWrapper.like(judgeConfig != null,"judgeConfig",judgeConfigJson);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(questionDifficulty),"questionDifficulty",questionDifficulty);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(submitNum),"submitNum",submitNum);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(acceptedNum),"acceptedNum",acceptedNum);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(createUser),"createUser",createUser);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(editUser),"editUser",editUser);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(questionStatus),"questionStatus",questionStatus);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(createTime),"createTime",createTime);
        adminQuestionQueryWrapper.like(ObjectUtils.isNotEmpty(updateTime),"updateTime",updateTime);
        return adminQuestionQueryWrapper;
    }

    @Override
    public Page<AdminQuestionVO> getAdminQuestionVOByPage(Page<Question> adminQuestionPageQueryWrapper) {
        List<Question> questionList = adminQuestionPageQueryWrapper.getRecords();
        Page<AdminQuestionVO> adminQuestionVOPage = new Page<>(adminQuestionPageQueryWrapper.getCurrent(), adminQuestionPageQueryWrapper.getSize(), adminQuestionPageQueryWrapper.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return adminQuestionVOPage;
        }
        // 1. 关联查询创建者和修改者信息
        Set<Long> createUserSet = questionList.stream().map(Question::getCreateUser).collect(Collectors.toSet());
        Map<Long, List<User>> createUserListMap = userService.listByIds(createUserSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        Set<Long> editUserSet = questionList.stream().map(Question::getCreateUser).collect(Collectors.toSet());
        Map<Long, List<User>> editUserListMap = userService.listByIds(editUserSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<AdminQuestionVO> questionVOList = questionList.stream().map(question -> {
            AdminQuestionVO adminQuestionVO = AdminQuestionVO.objToVo(question);
            Long createUser = question.getCreateUser();
            User user1 = null;
            if (createUserListMap.containsKey(createUser)) {
                user1 = createUserListMap.get(createUser).get(0);
            }
            adminQuestionVO.setCreateUser(userService.getUserVO(user1).getId());
            return adminQuestionVO;
        }).collect(Collectors.toList());
        adminQuestionVOPage.setRecords(questionVOList);
        return adminQuestionVOPage;
    }

    private boolean compareQuestionContent(String questionTitle, String questionContent, String questionAnswer, String questionTagsJsonString, String judgeCaseObjectString, String judgeConfigObjectString, int questionDifficulty, Integer questionStatus,Question question) {
        String oldQuestionTitle = question.getQuestionTitle();
        String oldQuestionContent = question.getQuestionContent();
        String oldQuestionAnswer = question.getQuestionAnswer();
        String oldQuestionTags = question.getQuestionTags();
        String oldJudgeCase = question.getJudgeCase();
        String oldJudgeConfig = question.getJudgeConfig();
        int oldQuestionDifficulty = question.getQuestionDifficulty();
        int oldQuestionStatus = question.getQuestionStatus();
        //至少有一处内容有修改
        boolean isChanged = false;
        if (!StringUtils.equals(oldQuestionTitle,questionTitle)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldQuestionContent,questionContent)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldQuestionAnswer,questionAnswer)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldQuestionTags, questionTagsJsonString)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldJudgeCase,judgeCaseObjectString)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldJudgeConfig,judgeConfigObjectString)) {
            isChanged = true;
        }
        if (oldQuestionDifficulty != questionDifficulty) {
            isChanged = true;
        }
        if (oldQuestionStatus != questionStatus) {
            isChanged = true;
        }
        return isChanged;
    }


}




