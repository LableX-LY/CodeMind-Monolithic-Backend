package com.xly.codemind.model.vo;

import cn.hutool.json.JSONUtil;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.dto.question.JudgeConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/7 14:19
 * @description 管理员视角下的题目信息
 **/
@Data
public class AdminQuestionVO implements Serializable {

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

    /**
     * 题目创建人
     */
    private Long createUser;

    /**
     * 题目修改人
     */
    private Long editUser;

    /**
     * 题目状态,0-正常,1-禁用
     */
    private Integer questionStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param adminQuestionVO
     * @return
     */
    public static Question voToObj(AdminQuestionVO adminQuestionVO) {
        if (adminQuestionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(adminQuestionVO, question);
        List<String> tagList = adminQuestionVO.getQuestionTags();
        if (tagList != null) {
            question.setQuestionTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudgeConfig = adminQuestionVO.getJudgeConfig();
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
    public static AdminQuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        AdminQuestionVO adminQuestionVO = new AdminQuestionVO();
        BeanUtils.copyProperties(question, adminQuestionVO);
        List<String> tagList = JSONUtil.toList(question.getQuestionTags(), String.class);
        adminQuestionVO.setQuestionTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        adminQuestionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return adminQuestionVO;
    }

}
