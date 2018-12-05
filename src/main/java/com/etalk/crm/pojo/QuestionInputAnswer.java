package com.etalk.crm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 * @date 2018/06/04
 */
@Getter
@Setter
public class QuestionInputAnswer {
    /**
     * 分数ID
     */
    //@JsonIgnore
    private Integer tpaperScoreId;
    /**
     * 题目ID
     */
    //@JsonIgnore
    private Integer qbankId;
    /**
     * 是否正确
     */
    private boolean istrue;
    /**
     * 该题目所得分数
     */
    private String score;
    /**
     * 学生填写的答案
     */
    private String inputAnswer;
    /**
     * 答案排序（针对多选题和填空题）
     */
    private String sort;
}
