package com.xly.codemind.model.vo;

import com.xly.codemind.model.dto.question.JudgeConfig;

import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/5 23:07
 * @description
 **/

public class QuestionVO {

    /**
     * id
     */
    private Long id;

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
     * 判题配置(JSON对象)
     */
    private List<JudgeConfig> judgeConfig;

    /**
     * 题目难度，1-简单,2-一般,3-中等,4-难,5-困难，默认为2
     */
    private Integer questionDifficulty;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;
}
