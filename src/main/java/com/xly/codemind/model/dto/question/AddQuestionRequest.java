package com.xly.codemind.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @datetime 2024/12/4 19:49
 * @description 新增题目请求类
 **/

@Data
public class AddQuestionRequest implements Serializable {

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
    private List<String> questionTags;

    /**
     * 判题用例(JSON数组)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置(JSON对象)
     */
    private JudgeConfig judgeConfig;

    /**
     * 题目答案
     */
    private String questionAnswer;

    /**
     * 题目难度,1-简单,2-一般,3-中等,4-难,5-困难，默认为2
     */
    private Integer questionDifficulty;

    private static final long serialVersionUID = 1L;

}
