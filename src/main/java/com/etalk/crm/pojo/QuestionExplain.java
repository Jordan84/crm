package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Terwer
 * @Date 2018/11/7 17:26
 * @Version 1.0
 * @Description 答案解析
 **/
@Getter
@Setter
public class QuestionExplain {
    /**
     * 题目ID
     */
    private Integer qbankId;
    /**
     * 解析类型
     */
    private Integer expType;
    /**
     * 解析内容
     */
    private String expContent;
}
