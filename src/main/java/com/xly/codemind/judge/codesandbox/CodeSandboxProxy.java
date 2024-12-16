package com.xly.codemind.judge.codesandbox;

import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 17:16
 * @description 代理模式
 **/
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    // y、定义为final，因为只使用一次
    // 使用代理模式的意义在于增强某个类，所以要注入codeSandbox
    private final CodeSandbox codeSandbox;


    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }

}
