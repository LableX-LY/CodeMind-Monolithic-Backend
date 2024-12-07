package com.xly.codemind.model.vo;

import cn.hutool.json.JSONUtil;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.dto.question.JudgeConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/5 23:07
 * @description 普通用户视角下的题目信息
 **/

@Data
public class UserQuestionVO implements Serializable {

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
    private JudgeConfig judgeConfig;

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

    private static final long serialVersionUID = 1L;

   /**
     * 包装类转对象
     *
     * @param userQuestionVO
     * @return
     */
    public static Question voToObj(UserQuestionVO userQuestionVO) {
        if (userQuestionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(userQuestionVO, question);
        List<String> tagList = userQuestionVO.getQuestionTags();
        if (tagList != null) {
            question.setQuestionTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudgeConfig = userQuestionVO.getJudgeConfig();
        if (voJudgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(voJudgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static UserQuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        UserQuestionVO questionVO = new UserQuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getQuestionTags(), String.class);
        questionVO.setQuestionTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return questionVO;
    }

}
