package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.judge.JudgeService;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.mapper.QuestionSubmitMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.QuestionSubmit;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.enums.QuestionSubmitStatusEnum;
import com.xly.codemind.service.QuestionSubmitService;
import com.xly.codemind.utils.IdWorkerUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
* @author x-ly
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-12-01 16:36:16
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService{

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Autowired
    private JudgeService judgeService;

    @Override
    public Long doQuestionSubmit(Long questionId, String questionCode, String questionLanguage, User loginUser) {
        if (ObjectUtils.isEmpty(questionId) || questionId < 0 || StringUtils.isAnyBlank(questionCode,questionLanguage)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数错误!");
        }
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录,请先登录!");
        }
        //获取题目信息（题目存在才可以答题）
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"所答题目不存在!");
        }
        //设置题目提交的初始状态
        QuestionSubmit questionSubmit = new QuestionSubmit();
        //此条记录的id
        questionSubmit.setId(IdWorkerUtil.generateId());
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setQuestionTitle(question.getQuestionTitle());
        questionSubmit.setQuestionCode(questionCode);
        questionSubmit.setQuestionLanguage(questionLanguage);
        questionSubmit.setJudgeStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        int insert = questionSubmitMapper.insert(questionSubmit);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交记录插入失败!");
        }
        Long questionSubmitedId = questionSubmit.getId();
        // 执行判题服务,异步操作
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitedId);
        });
        return questionSubmitedId;
    }
}




