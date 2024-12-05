package com.xly.codemind.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.dto.question.UserQueryQuestionRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author x-ly
* @description 针对表【question(题目表)】的数据库操作Service
* @createDate 2024-12-01 16:36:16
*/

public interface QuestionService extends IService<Question> {

    /**
     * 新增题目
     * @param questionTitle 题目标题
     * @param questionContent 题目内容
     * @param questionAnswer 题目答案
     * @param questionTagsJsonString 题目标签
     * @param judgeCaseObjectJsonString 判题用例
     * @param judgeConfigObjectJsonString 判题配置
     * @param questionDifficulty 题目难度
     * @return 新增题目的id
     */
    Long addQuestion(String questionTitle, String questionContent, String questionAnswer, String questionTagsJsonString, String judgeCaseObjectJsonString, String judgeConfigObjectJsonString, int questionDifficulty, HttpServletRequest request);

    /**
     * 删除题目
     * @param questionId 题目id
     * @return 是否删除成功
     */
    Boolean deleteQuestion(long questionId);

    /**
     * 修改题目
     * @param questionId 题目id
     * @param questionTitle 题目标题
     * @param questionContent 题目内容
     * @param questionAnswer 题目答案
     * @param questionTagsJsonString 题目标签
     * @param judgeCaseObjectJsonString 判题用例
     * @param judgeConfigObjectJsonString 判题配置
     * @param questionDifficulty 题目难度
     * @return 是否修改成功
     */
    Boolean editQuestion(long questionId,String questionTitle, String questionContent, String questionAnswer, String questionTagsJsonString, String judgeCaseObjectJsonString, String judgeConfigObjectJsonString, int questionDifficulty, HttpServletRequest request);

    /**
     * 获取用户题目查询包装类
     * @param userQueryQuestionRequest 用户题目查询请求
     * @return 用户视角下的题目查询包装类
     */
    QueryWrapper<Question> getQueryWrapper(UserQueryQuestionRequest userQueryQuestionRequest);

}
