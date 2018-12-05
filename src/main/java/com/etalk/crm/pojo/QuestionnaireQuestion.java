package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * oc课问卷调查问题
 */
@Entity
@Setter
@Getter
public class QuestionnaireQuestion implements Serializable {
    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 问卷
     */
    private Integer paperId;

    /**
     * 问卷题目
     */
    private String questionName;

    /**
     *尾注
     */
    private String endnotes;

    /**
     *题注
     */
    private String caption;

    /**
     * 是否开启备注状态  默认关闭 0
     */
    private Integer state;
}
