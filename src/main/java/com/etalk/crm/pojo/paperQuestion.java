package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class paperQuestion implements Serializable {
    /**
     * 题号ID
     */
    private int qstId;

    /**
     * 所属问卷ID
     */
    private int paperId;

    /**
     * 题目类型
     */
    private int qstType;

    /**
     * 题干
     */
    private String qstTitle;

    /**
     * 选项
     */
    private String qstOption;

    /**
     * 统计结果
     */
    private String qstAnswer;
}
