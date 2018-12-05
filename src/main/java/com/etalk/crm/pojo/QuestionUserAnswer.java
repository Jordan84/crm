package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * oc课用户问卷答案表
 */
@Entity
@Setter
@Getter
public class QuestionUserAnswer implements Serializable {
    /**
     *答案主键
     */
    private Integer answerId;
    /**
     *问题
     */
    private Integer questionId;
    /**
     *问卷
     */
    private Integer paperId;
    /**
     *个人
     */
    private Integer personId;

    /**
     * 选项id
     */
    private Integer optionId;


    /**
     * 题目备注
     */
    private String optionSummary;

}
