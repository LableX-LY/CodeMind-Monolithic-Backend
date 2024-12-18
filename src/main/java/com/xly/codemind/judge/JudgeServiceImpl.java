package com.xly.codemind.judge;

import cn.hutool.json.JSONUtil;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.judge.codesandbox.CodeSandbocFactory;
import com.xly.codemind.judge.codesandbox.CodeSandbox;
import com.xly.codemind.judge.codesandbox.CodeSandboxProxy;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.mapper.QuestionSubmitMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.QuestionSubmit;
import com.xly.codemind.model.dto.question.JudgeCase;
import com.xly.codemind.model.enums.QuestionSubmitStatusEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/13 19:34
 * @description
 **/

@Service
public class JudgeServiceImpl implements JudgeService{

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Override
    public QuestionSubmit doJudge(long questionSubmitedId) {
        //首先查询题目提交记录
        QuestionSubmit questionSubmit = questionSubmitMapper.selectById(questionSubmitedId);
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
                .build();
        List<List<String>> inputList1 = executeCodeRequest.getInputList();
        //请求代码沙箱，执行代码
        ExecuteCodeResponse executeCodeResponse = remoteCodeSandbox.executeCode(executeCodeRequest);
        //获取输出响应，与数据库中的题目答案进行比对
        List<String> outputList = executeCodeResponse.getOutputList();

        return null;
    }

}
