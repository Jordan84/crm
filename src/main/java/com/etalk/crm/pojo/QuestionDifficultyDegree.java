package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Terwer
 */
@Entity
@Getter
@Setter
public class QuestionDifficultyDegree implements Serializable {
    private Integer id;

    private String cnName;

    private String enName;

    /**
     * 顺序排序，值越大，难度越大
     */
    private String sort;

    /**
     * 状态：0无效1有效
     */
    private Integer state;
}