package com.xly.codemind.model.dto.question;

import com.xly.codemind.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/7 14:18
 * @description 管理员题目查询请求类
 **/
@Data
public class AdminQueryQuestionRequest extends PageRequest implements Serializable {

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

}
