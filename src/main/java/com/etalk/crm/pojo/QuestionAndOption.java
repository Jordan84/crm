package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 问卷  题目  和选项
 */
@Getter
@Setter
public class QuestionAndOption implements Serializable {
    /**
     * 问卷题目对象
     */
    private QuestionnaireQuestion question;

    /**
     * 用户选择答案
     */
    private QuestionUserAnswer questionUserAnswer;

    /**
     * 问卷选项列表
     */
    List<QuestionaireOption> OptionList;
}
