package com.xly.codemind.judge.strategy;

import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.judge.codesandbox.model.JudgeContext;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.dto.question.JudgeConfig;
import com.xly.codemind.model.dto.questionsubmit.JudgeInfo;
import com.xly.codemind.model.enums.JudgeInfoMessageEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/23 19:38
 * @description Java判题策略的具体实现类
 **/

public class JavaLanguageJudgeStrategy {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //1.参数校验
        if (judgeContext == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数异常!");
        }
        List<String> outputList = judgeContext.getOutputList();
        String questionAnswer = judgeContext.getQuestionAnswer();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        Question question = judgeContext.getQuestion();
        if (ObjectUtils.isEmpty(outputList) || StringUtils.isBlank(questionAnswer)
                || ObjectUtils.isEmpty(judgeConfig)
                || ObjectUtils.isEmpty(judgeInfo)
                || ObjectUtils.isEmpty(question)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数异常!");
        }
        //如果运行时间和内存占用为空的话就设为0
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        JudgeInfo judgeInfoResponse = new JudgeInfo("",memory,time);
        //2.首先判断outputList中的输出数量是否和questionAnswer的数量一致，不一致就直接视为答题错误
        String[] splitQuestionAnswer = StringUtils.split(questionAnswer, ',');
        if (splitQuestionAnswer.length != outputList.size()) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResponse;
        }
        return null;
    }

}
