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
        //Question question = judgeContext.getQuestion();
//        if (ObjectUtils.isEmpty(outputList) || StringUtils.isBlank(questionAnswer)
//                || ObjectUtils.isEmpty(judgeConfig)
//                || ObjectUtils.isEmpty(judgeInfo)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数异常!");
//        }
        if (ObjectUtils.isEmpty(outputList) || StringUtils.isBlank(questionAnswer)
                || ObjectUtils.isEmpty(judgeConfig)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数异常!");
        }
        //如果运行时间和内存占用为空的话就设为0
        //Long usedMemory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        //Long usedTime = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        JudgeInfo judgeInfoResponse = new JudgeInfo("",0L,0L);
        //2.首先判断outputList中的输出数量是否和questionAnswer的数量一致，不一致就直接视为答题错误
        String[] splitQuestionAnswer = StringUtils.split(questionAnswer, ',');
        if (splitQuestionAnswer.length < outputList.size()) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.OUTPUT_LIMIT_MISSING.getValue());
            return judgeInfoResponse;
        }
        if (splitQuestionAnswer.length > outputList.size()) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.OUTPUT_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        //3.outputList中的输出数量和questionAnswer的数量一致，进一步判断是否每个都一致
        for (int i = 0;i < splitQuestionAnswer.length;i++) {
            if (!outputList.get(i).equals(splitQuestionAnswer[i])) {
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }
        //4.若每个输出和答案都一致，则进一步判断题目限制是否符合要求（运行时间、堆栈占用等信息）
//        Long maxMemoryLimit = judgeConfig.getMemoryLimit();
//        Long maxTimeLimit = judgeConfig.getTimeLimit();
//        Long maxStackLimit = judgeConfig.getStackLimit();
//        if (usedMemory > maxMemoryLimit) {
//            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
//            return judgeInfoResponse;
//        }
//        if (usedTime > maxTimeLimit) {
//            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
//            return judgeInfoResponse;
//        }
        // todo Java编译可能会耗费另外的时间，需要手动剔除？
        //5.若前面所有的条件都判断通过，则视为答案正确
        judgeInfoResponse.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        return judgeInfoResponse;
    }

}
