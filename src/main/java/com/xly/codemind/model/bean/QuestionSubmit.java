package com.xly.codemind.model.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交表
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 提交者
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目标题
     */
    private String questionTitle;;

    /**
     * 题目使用的编程语言
     */
    private String questionLanguage;

    /**
     * 用户代码
     */
    private String questionCode;

    /**
     * 判题状态,0-待判题、1-判题中、2-判题成功、3-判题失败
     */
    private Integer judgeStatus;

    /**
     * 判题信息(JSON对象)
     */
    private String judgeInfo;

    /**
     * 提交时间
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
        QuestionSubmit other = (QuestionSubmit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getQuestionId() == null ? other.getQuestionId() == null : this.getQuestionId().equals(other.getQuestionId())) && (this.getQuestionTitle() == null ? other.getQuestionTitle() == null : this.getQuestionTitle().equals(other.getQuestionTitle()))
            && (this.getQuestionLanguage() == null ? other.getQuestionLanguage() == null : this.getQuestionLanguage().equals(other.getQuestionLanguage()))
            && (this.getQuestionCode() == null ? other.getQuestionCode() == null : this.getQuestionCode().equals(other.getQuestionCode()))
            && (this.getJudgeStatus() == null ? other.getJudgeStatus() == null : this.getJudgeStatus().equals(other.getJudgeStatus()))
            && (this.getJudgeInfo() == null ? other.getJudgeInfo() == null : this.getJudgeInfo().equals(other.getJudgeInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getQuestionId() == null) ? 0 : getQuestionId().hashCode());
        result = prime * result + ((getQuestionTitle() == null) ? 0 : getQuestionTitle().hashCode());
        result = prime * result + ((getQuestionLanguage() == null) ? 0 : getQuestionLanguage().hashCode());
        result = prime * result + ((getQuestionCode() == null) ? 0 : getQuestionCode().hashCode());
        result = prime * result + ((getJudgeStatus() == null) ? 0 : getJudgeStatus().hashCode());
        result = prime * result + ((getJudgeInfo() == null) ? 0 : getJudgeInfo().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", questionId=").append(questionId);
        sb.append(", questionTitle=").append(questionTitle);
        sb.append(", questionLanguage=").append(questionLanguage);
        sb.append(", questionCode=").append(questionCode);
        sb.append(", judgeStatus=").append(judgeStatus);
        sb.append(", judgeInfo=").append(judgeInfo);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}