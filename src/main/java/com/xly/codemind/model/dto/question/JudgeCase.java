package com.xly.codemind.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/5 22:08
 * @description 题目中的判题用例（JSON对象，数据库中存放JSON转换的String字符串）
 **/

@Data
public class JudgeCase implements Serializable {

    /**
     * 输入用例
     */
    private List<String> input;

    /**
     * 输出用例
     */
    private String output;

    private static final long serialVersionUID = 1L;

}
