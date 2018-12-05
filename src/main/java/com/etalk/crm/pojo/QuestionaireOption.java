package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 问卷调查 选项
 * @author Jordan
 */
@Entity
@Getter
@Setter
public class QuestionaireOption implements Serializable {
    /**
     * 选项
     */
    private Integer optionId;

    /**
     * 问题
     */
    private Integer questionId;

    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 问卷ID
     */
    private Integer paperId;

    /**
     * 排序
     */
    private  Integer sort;

    /**
     *分值
     */
    private Integer optionValue;
}
