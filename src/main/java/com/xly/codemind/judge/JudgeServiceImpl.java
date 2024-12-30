package com.xly.codemind.judge;

import cn.hutool.json.JSONUtil;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.judge.codesandbox.CodeSandbocFactory;
import com.xly.codemind.judge.codesandbox.CodeSandbox;
import com.xly.codemind.judge.codesandbox.CodeSandboxProxy;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;
import com.xly.codemind.judge.codesandbox.model.JudgeContext;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.mapper.QuestionSubmitMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.QuestionSubmit;
import com.xly.codemind.model.dto.question.JudgeCase;
import com.xly.codemind.model.dto.question.JudgeConfig;
import com.xly.codemind.model.dto.questionsubmit.JudgeInfo;
import com.xly.codemind.model.enums.JudgeInfoMessageEnum;
import com.xly.codemind.model.enums.QuestionSubmitStatusEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/13 19:34
 * @description 判题服务（判题流程调用judgeManager）
 *              该类负责调用代码沙箱,根据judgeManager的返回结果修改数据库
 **/

@Service
public class JudgeServiceImpl implements JudgeService{

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitedId,long userId,long questionId) {
        //首先查询题目提交记录
        QuestionSubmit questionSubmit = questionSubmitMapper.selectById(questionSubmitedId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"题目提交记录不存在!");
        }
        //获取对应的题目id，查询题目是否存在及题目状态是否正常
        long questionIdBySubmited = questionSubmit.getQuestionId();
        Question question = questionMapper.selectById(questionIdBySubmited);
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
        //调用代码沙箱，获取执行结果。
        // 沙箱只负责代码的执行（执行用户代码，输入判题用例，得到输出结果），不负责比对结果
        CodeSandbox remoteCodeSandbox = CodeSandbocFactory.newInstance("remote");
        //代理模式
        remoteCodeSandbox = new CodeSandboxProxy(remoteCodeSandbox);
        //获取代码执行请求参数
        // todo judgeCase格式问题？
        String questionCode = questionSubmit.getQuestionCode();
        String questionLanguage = questionSubmit.getQuestionLanguage();
        String judgeCaseString = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseString, JudgeCase.class);
        List<List<String>> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        System.out.println(inputList.size());
        System.out.println(inputList);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .questionCode(questionCode)
                .questionLanguage(questionLanguage)
                .inputList(inputList)
                .userId(userId)
                .questionId(questionId)
                .build();
//        List<List<String>> inputList1 = executeCodeRequest.getInputList();
        //请求代码沙箱，执行代码
        ExecuteCodeResponse executeCodeResponse = remoteCodeSandbox.executeCode(executeCodeRequest);
        //获取输出响应，首先判断题目是否发生编译错误
        System.out.println("代码沙箱响应" + executeCodeResponse);
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        int status = executeCodeResponse.getStatus();
        //输出为空、判题信息为空且status为3，一定发生编译异常
        if (status == 3 && ObjectUtils.isEmpty(outputList) && ObjectUtils.isEmpty(judgeInfo)) {
            //题目编译异常，设置题目提交状态为编译异常，judgeInfo也为编译异常
            //题目发生编译异常时，其实根本就没有触发判题服务（判题服务是指根据代码沙箱的输出和数据库中的答案进行比对）
            //所以当题目发生编译异常时题目提价状态应该单独设置
            //同步更新题目提交记录数
            questionSubmit.setJudgeStatus(QuestionSubmitStatusEnum.COMPILEFAILED.getValue());
            JudgeInfo compileErrorJudgeInfo = new JudgeInfo("编译失败",0L,0L);
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(compileErrorJudgeInfo));
            question.setSubmitNum(question.getSubmitNum() + 1);
            // todo 这里应该加事务，确保题目提交状态的修改和题目数量的修改是同步的
            if (questionMapper.updateById(question) < 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交数更新失败");
            }
            if (questionSubmitMapper.updateById(questionSubmit) < 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新失败");
            }
            return questionSubmitMapper.selectById(questionSubmit.getId());
        }
        //输出不为空，则代码编译和运行正常，根据outoutlist比对数据库，进行判题
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setOutputList(outputList);
        //todo 前端传递题目答案时需要在每个答案之间加上逗号？后端自行解析？
        judgeContext.setQuestionAnswer(question.getQuestionAnswer());
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        JudgeInfo judgeInfoResult = judgeManager.doJudge(questionLanguage, judgeContext);
        //根据返回的judgeInfoResult中的message判断答题情况，进而修改数据库中的答题情况
        String judgeResult = judgeInfoResult.getMessage();
        if (judgeResult.equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            JudgeInfo acceptedJudgeInfo = new JudgeInfo("答案正确",0L,0L);
            questionSubmit.setJudgeStatus(2);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(acceptedJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmit;
        }
        if (judgeResult.equals(JudgeInfoMessageEnum.WRONG_ANSWER.getValue())) {
            JudgeInfo wrongAnswerJudgeInfo = new JudgeInfo("答案错误",0L,0L);
            questionSubmit.setJudgeStatus(3);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(wrongAnswerJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmitMapper.selectById(questionSubmitedId);
        }
        if (judgeResult.equals(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue())) {
            JudgeInfo timeLimitExceededJudgeInfo = new JudgeInfo("运行超时",0L,0L);
            questionSubmit.setJudgeStatus(5);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(timeLimitExceededJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmitMapper.selectById(questionSubmitedId);
        }
        if (judgeResult.equals(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue())) {
            JudgeInfo memoryLimitExceededJudgeInfo = new JudgeInfo("内存溢出",0L,0L);
            questionSubmit.setJudgeStatus(6);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(memoryLimitExceededJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmitMapper.selectById(questionSubmitedId);
        }
        if (judgeResult.equals(JudgeInfoMessageEnum.OUTPUT_LIMIT_MISSING.getValue())) {
            JudgeInfo outputLimitMissingJudgeInfo = new JudgeInfo("输出缺失",0L,0L);
            questionSubmit.setJudgeStatus(7);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(outputLimitMissingJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmitMapper.selectById(questionSubmitedId);
        }
        if (judgeResult.equals(JudgeInfoMessageEnum.OUTPUT_LIMIT_EXCEEDED.getValue())) {
            JudgeInfo outputLimitExceededJudgeInfo = new JudgeInfo("输出溢出",0L,0L);
            questionSubmit.setJudgeStatus(8);
            questionSubmit.setUpdateTime(new Date());
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(outputLimitExceededJudgeInfo));
            if (questionSubmitMapper.updateById(questionSubmit) < 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败!");
            }
            return questionSubmitMapper.selectById(questionSubmitedId);
        }
        return questionSubmitMapper.selectById(questionSubmitedId);

    }

}
