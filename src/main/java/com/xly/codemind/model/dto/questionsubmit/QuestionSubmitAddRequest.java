package com.xly.codemind.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/10 19:40
 * @description 题目提交请求类
 **/

@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目代码
     */
    private String questionCode;

    /**
     * 题目语言
     */
    private String questionLanguage;

    /**
     * 题目标题
     */
    private String questionTitle;

    private static final long serialVersionUID = 1L;

}
