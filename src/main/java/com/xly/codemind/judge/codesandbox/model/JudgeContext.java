package com.xly.codemind.judge.codesandbox.model;

import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.dto.question.JudgeConfig;
import com.xly.codemind.model.dto.questionsubmit.JudgeInfo;
import lombok.Data;

import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/23 19:36
 * @description 上下文，用于判题时传递各种判题信息
 **/
@Data
public class JudgeContext {

    private List<String> outputList;

    private JudgeInfo judgeInfo;

    private String questionAnswer;

    private JudgeConfig judgeConfig;

    private Question question;

}
