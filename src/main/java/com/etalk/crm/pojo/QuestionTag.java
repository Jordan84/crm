package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Terwer
 * @Date 2018/9/17 11:44
 * @Version 1.0
 * @Description TODO
 **/
@Getter
@Setter
public class QuestionTag {

    /**
     * 题目ID
     */
    private Integer qbankId;

    /**
     * 学期ID
     */
    private String tagName;

    /**
     * 学期ID
     */
    private String tagTitle;
}
