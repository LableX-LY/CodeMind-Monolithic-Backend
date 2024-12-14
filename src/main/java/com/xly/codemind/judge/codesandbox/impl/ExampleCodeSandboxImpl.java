package com.xly.codemind.judge.codesandbox.impl;

import com.xly.codemind.judge.codesandbox.CodeSandbox;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;
import com.xly.codemind.model.dto.questionsubmit.JudgeInfo;

import java.util.ArrayList;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 14:33
 * @description 示例代码沙箱
 **/

public class ExampleCodeSandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("==========调用示例代码沙箱==========");
        System.out.println("用户代码：" + executeCodeRequest.getQuestionCode());
        System.out.println("编程语言：" + executeCodeRequest.getQuestionLanguage());
        System.out.println("判题用例：" + executeCodeRequest.getInputList());
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setStatus(0);
        executeCodeResponse.setMessage("示例代码沙箱执行成功");
        ArrayList<String> outputList = new ArrayList<>();
        outputList.add("202412141455");
        outputList.add("202412141456");
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("示例代码沙箱执行成功");
        judgeInfo.setTime(1L);
        judgeInfo.setMemory(2L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
