package com.xly.codemind.service.impl;

import com.xly.codemind.mapper.QuestionMapper;
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
 * @createtime 2024/12/4 20:49
 * @version 1.0
 * @description
 **/
 
 
class QuestionServiceImplMockTest {

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试添加题目，测试属性为空
     */
    @Test
    void addQuestion() {
        String questionTitle = "xxx";
        String questionContent = "xxx";
        String questionAnswer = "xxx";
        String questionTags = "xxx";
        String judgeCase = "xxx";
        String judgeConfig = "";
        int questionDifficulty = 2;
        when(questionMapper.insert(any())).thenReturn(1);
        Long l = questionService.addQuestion(questionTitle, questionContent, questionAnswer, questionTags, judgeCase, judgeConfig,questionDifficulty,request);
    }

    /**
     * 测试删除题目，题目不存在
     */
    @Test
    void deleteQuestionNotExists() {
        long questionId = 1863941192572489728L;
        when(questionMapper.selectById(questionId)).thenReturn(null);
        Boolean b = questionService.deleteQuestion(questionId);
    }

}