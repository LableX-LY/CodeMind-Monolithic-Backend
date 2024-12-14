package com.xly.codemind.judge.codesandbox.impl;

import com.xly.codemind.judge.codesandbox.CodeSandbox;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 15:02
 * @description 第三方代码沙箱
 **/

public class ThirdPartyCodeSandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
