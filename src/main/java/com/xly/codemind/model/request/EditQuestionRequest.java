package com.xly.codemind.model.request;

import lombok.Data;

/**
 * @author X-LY。
 * @version 1.0
 * @datetime 2024/12/4 19:50
 * @description 修改题目请求类
 **/

@Data
public class EditQuestionRequest {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * 题目内容
     */
    private String questionContent;

    /**
     * 题目标签列表(JSON数组)
     */
    private String questionTags;

    /**
     * 判题用例(JSON数组)
     */
    private String judgeCase;

    /**
     * 判题配置(JSON对象)
     */
    private String judgeConfig;

    /**
     * 题目答案
     */
    private String questionAnswer;

    /**
     * 题目难度,1-简单,2-一般,3-中等,4-难,5-困难，默认为2
     */
    private Integer questionDifficulty;

}

