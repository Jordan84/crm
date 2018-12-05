package com.etalk.crm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 */
@Getter
@Setter
public class QuestionOptionAnswer {
    /**
     * 选项ID
     */
    private Integer id;
    /**
     * 题目ID
     */
    private Integer qbankId;

    /**
     * 1文字选项，2图片选项
     */
    private Integer optionType;

    /**
     * 文字
     */
    private String answer;

    /**
     * 图片
     */
    private String imgUrl;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否正确
     */
    @JsonIgnore
    private boolean isRight;
}
