package com.xly.codemind.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/14 13:58
 * @description 执行代码请求类,包含题目输入用例、用户代码、编程语言
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {

    /**
     * 答题者的id
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 输入用例（数据库中获取）
     */
    private List<List<String>> inputList;

    /**
     * 用户代码
     */
    private String questionCode;

    /**
     * 编程语言
     */
    private String questionLanguage;

}
