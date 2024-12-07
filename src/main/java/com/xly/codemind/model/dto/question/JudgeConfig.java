package com.xly.codemind.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/5 22:09
 * @description 题目中的判题配置信息（JSON对象，数据库中存放JSON转换的String字符串）
 **/

@Data
public class JudgeConfig implements Serializable {

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;

    private static final long serialVersionUID = 1L;

}
