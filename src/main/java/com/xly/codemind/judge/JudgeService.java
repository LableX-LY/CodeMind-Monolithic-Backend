package com.xly.codemind.judge;

import com.xly.codemind.model.bean.QuestionSubmit;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/13 19:31
 * @description 判题服务接口
 **/

public interface JudgeService {

    /**
     * 判题（执行用户代码，根据判题用例得到输出结果，将输出结果与数据库中的答案进行比较，最后得到用户作答正确与否）
     * @param questionSubmitedId 题目提交记录的id
     * @return 判题信息
     */
    QuestionSubmit doJudge(long questionSubmitedId,long userId,long questionId);
}
