package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.common.IdWorker;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Override
    public Long addQuestion(String questionTitle, String questionContent, String questionAnswer, String questionTags, String judgeCase, String judgeConfig, int questionDifficulty, HttpServletRequest request) {
        //校验信息
        if (StringUtils.isAnyBlank(questionTitle,questionContent,questionAnswer,questionTags,judgeCase,judgeConfig)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        Question question = new Question();
        long id = IdWorker.getInstance().nextId();
        if (id == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统发生内部错误,id生成失败!");
        }
        // todo 只有管理员才可以添加题目
        question.setQuestionTitle(questionTitle);
        question.setQuestionContent(questionContent);
        question.setQuestionAnswer(questionAnswer);
        question.setQuestionTags(questionTags);
        question.setJudgeCase(judgeCase);
        question.setJudgeConfig(judgeConfig);
        question.setQuestionDifficulty(questionDifficulty);
        // todo 获取登录用户方法优化？
        Object sessionUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (sessionUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录!");
        }
        User loginUser = (User) sessionUser;
        question.setCreateUser(loginUser.getId());
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
    public Boolean editQuestion(long questionId,String questionTitle, String questionContent, String questionAnswer, String questionTags, String judgeCase, String judgeConfig, int questionDifficulty, HttpServletRequest request) {
        //校验信息
        if (StringUtils.isAnyBlank(questionTitle,questionContent,questionAnswer,questionTags,judgeCase,judgeConfig)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        //题目存在才可以修改
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目不存在!");
        }
        // todo 只有管理员才可以修改题目
        //旧内容
        String oldQuestionTitle = question.getQuestionTitle();
        String oldQuestionContent = question.getQuestionContent();
        String oldQuestionAnswer = question.getQuestionAnswer();
        String oldQuestionTags = question.getQuestionTags();
        String oldJudgeCase = question.getJudgeCase();
        String oldJudgeConfig = question.getJudgeConfig();
        int oldQuestionDifficulty = question.getQuestionDifficulty();
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
        if (!StringUtils.equals(oldQuestionTags, questionTags)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldJudgeCase,judgeCase)) {
            isChanged = true;
        }
        if (!StringUtils.equals(oldJudgeConfig,judgeConfig)) {
            isChanged = true;
        }
        if (oldQuestionDifficulty != questionDifficulty) {
            isChanged = true;
        }
        if (!isChanged) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目内容至少要有一处修改!");
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
        question.setQuestionTags(questionTags);
        question.setJudgeCase(judgeCase);
        question.setJudgeConfig(judgeConfig);
        question.setEditUser(loginUser.getId());
        int editedNum = questionMapper.updateById(question);
        if (editedNum < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"修改失败,请稍后重试!");
        }
        return true;
    }
}




