package com.xly.codemind.judge;

import com.xly.codemind.judge.codesandbox.model.JudgeContext;
import com.xly.codemind.judge.strategy.JavaLanguageJudgeStrategy;
import com.xly.codemind.model.dto.questionsubmit.JudgeInfo;
import org.springframework.stereotype.Component;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/23 19:38
 * @description 根据不同的题目语言选择不同的判题策略
 **/
@Component
public class JudgeManager {

    JudgeInfo doJudge(String questionLanguage, JudgeContext judgeContext) {
        if (questionLanguage.equals("java")) {
            JavaLanguageJudgeStrategy javaLanguageJudgeStrategy = new JavaLanguageJudgeStrategy();
            return javaLanguageJudgeStrategy.doJudge(judgeContext);
        }
        return null;
    }
}
