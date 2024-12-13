package com.xly.codemind.judge;

import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.mapper.QuestionSubmitMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.QuestionSubmit;
import com.xly.codemind.model.enums.QuestionSubmitStatusEnum;

import javax.annotation.Resource;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/13 19:34
 * @description
 **/

public class JudgeServiceImpl implements JudgeService{

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //首先查询题目提交记录
        QuestionSubmit questionSubmit = questionSubmitMapper.selectById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目提交记录不存在!");
        }
        //获取对应的题目id，查询题目是否存在及题目状态是否正常
        long questionId = questionSubmit.getQuestionId();
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目不存在!");
        }
        if (question.getQuestionStatus() == 1) {
            throw new BusinessException(ErrorCode.QUESTION_BAN_ERROR,"题目被禁用,禁止作答!");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getJudgeStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        //更改题目的判题状态
        questionSubmit.setJudgeStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        int updateResult = questionSubmitMapper.updateById(questionSubmit);
        if (updateResult < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新失败!");
        }
        //调用代码沙箱，获取执行结果。沙箱只负责代码的执行（执行用户代码，输入判题用例，得到输出结果），不负责比对结果

        return null;
    }

}

