package com.xly.codemind.judge.codesandbox;

import com.xly.codemind.judge.codesandbox.impl.ExampleCodeSandboxImpl;
import com.xly.codemind.judge.codesandbox.impl.RemoteCodeSandboxImpl;
import com.xly.codemind.judge.codesandbox.impl.ThirdPartyCodeSandboxImpl;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 14:22
 * @description 代码沙箱静态工厂（根据字符串参数创建指定的代码沙箱实例）
 **/

public class CodeSandbocFactory {

    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandboxImpl();
            case "remote":
                return new RemoteCodeSandboxImpl();
            case "thirdParty":
                 return new ThirdPartyCodeSandboxImpl();
            default:
                return new ExampleCodeSandboxImpl();
        }
    }

}
