package com.xly.codemind.model.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目表
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @TableId
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
    private String questionTags;

    /**
     * 判题用例(JSON数组)
     */
    private String judgeCase;

    /**
     * 判题配置(JSON对象)
     */
    private String judgeConfig;

    /**
     * 题目答案
     */
    private String questionAnswer;

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

    /**
     * 是否删除,0-正常,1-删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Question other = (Question) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getQuestionTitle() == null ? other.getQuestionTitle() == null : this.getQuestionTitle().equals(other.getQuestionTitle()))
            && (this.getQuestionContent() == null ? other.getQuestionContent() == null : this.getQuestionContent().equals(other.getQuestionContent()))
            && (this.getQuestionTags() == null ? other.getQuestionTags() == null : this.getQuestionTags().equals(other.getQuestionTags()))
            && (this.getJudgeCase() == null ? other.getJudgeCase() == null : this.getJudgeCase().equals(other.getJudgeCase()))
            && (this.getJudgeConfig() == null ? other.getJudgeConfig() == null : this.getJudgeConfig().equals(other.getJudgeConfig()))
            && (this.getQuestionAnswer() == null ? other.getQuestionAnswer() == null : this.getQuestionAnswer().equals(other.getQuestionAnswer()))
            && (this.getSubmitNum() == null ? other.getSubmitNum() == null : this.getSubmitNum().equals(other.getSubmitNum()))
            && (this.getAcceptedNum() == null ? other.getAcceptedNum() == null : this.getAcceptedNum().equals(other.getAcceptedNum()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getQuestionStatus() == null ? other.getQuestionStatus() == null : this.getQuestionStatus().equals(other.getQuestionStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQuestionTitle() == null) ? 0 : getQuestionTitle().hashCode());
        result = prime * result + ((getQuestionContent() == null) ? 0 : getQuestionContent().hashCode());
        result = prime * result + ((getQuestionTags() == null) ? 0 : getQuestionTags().hashCode());
        result = prime * result + ((getJudgeCase() == null) ? 0 : getJudgeCase().hashCode());
        result = prime * result + ((getJudgeConfig() == null) ? 0 : getJudgeConfig().hashCode());
        result = prime * result + ((getQuestionAnswer() == null) ? 0 : getQuestionAnswer().hashCode());
        result = prime * result + ((getSubmitNum() == null) ? 0 : getSubmitNum().hashCode());
        result = prime * result + ((getAcceptedNum() == null) ? 0 : getAcceptedNum().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getQuestionStatus() == null) ? 0 : getQuestionStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", questionTitle=").append(questionTitle);
        sb.append(", questionContent=").append(questionContent);
        sb.append(", questionTags=").append(questionTags);
        sb.append(", judgeCase=").append(judgeCase);
        sb.append(", judgeConfig=").append(judgeConfig);
        sb.append(", questionAnswer=").append(questionAnswer);
        sb.append(", submitNum=").append(submitNum);
        sb.append(", acceptedNum=").append(acceptedNum);
        sb.append(", createUser=").append(createUser);
        sb.append(", questionStatus=").append(questionStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}