package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 * @date 2018/06/01
 */
@Getter
@Setter
public class StudentPaper {
    /**
     * 学生ID
     */
    private Integer studentId;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 试卷ID
     */
    private Integer paperId;
    /**
     * 试卷名称
     */
    private String paperName;
    /**
     * 分数ID
     */
    private Integer scoreId;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 试卷状态
     */
    private Integer state;
}
