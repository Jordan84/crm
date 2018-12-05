package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 */
@Getter
@Setter
public class QuestionRightAnswer {
    /**
     * 正确答案ID
     */
    private Integer id;
    /**
     * 题目ID
     */
    private Integer qbankId;
    /**
     * 正确答案一
     */
    private String answer1;
    /**
     * 正确答案二
     */
    private String answer2;
    /**
     * 正确答案三
     */
    private String answer3;
    /**
     * 正确答案四
     */
    private String answer4;
}
