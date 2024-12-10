package com.xly.codemind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xly.codemind.model.bean.QuestionSubmit;
import com.xly.codemind.model.bean.User;

/**
* @author x-ly
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-12-01 16:36:16
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 执行题目提交（将题目交给判题服务）
     * @param questionId 题目id
     * @param questionCode 用户代码
     * @param questionLanguage 编程语言
     * @return 生成的判题记录的id
     */
    Long doQuestionSubmit(Long questionId, String questionCode, String questionLanguage, User loginUser);

}
