package com.xly.codemind.judge.codesandbox;

import com.xly.codemind.judge.codesandbox.model.ExecuteCodeRequest;
import com.xly.codemind.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 14:26
 * @description 代码沙箱接口,定义代码沙箱及沙箱中的方法，具体的实现要靠实现类执行
 **/

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
