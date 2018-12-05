package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Terwer
 * @Date 2018/11/10 14:02
 * @Version 1.0
 * @Description 做题记录
 **/
@Getter
@Setter
public class QuestionResultRecord {
    /**
     * 题目ID
     */
    private Integer qbankId;
    /**
     * 用户ID
     */
    private Integer personId;
    /**
     * 课程ID
     */
    private Integer lessonId;
    /**
     * 知识点ID
     */
    private Integer qknowledgeId;
    /**
     * 单元ID
     */
    private Integer unitId;
    /**
     * 答案
     */
    private String questionResult;
    /**
     * 是否答对
     */
    private Integer isTrue;
    /**
     * 答对次数
     */
    private Integer rightNum;
    /**
     * 答错次数
     */
    private Integer errorNum;
    /**
     * 版本号
     */
    private Integer version;
}
