package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/9/17 14:09
 * @Version 1.0
 * @Description TODO
 **/
@Getter
@Setter
public class QuestionTerm {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 父ID
     */
    private Integer parentId;
    /**
     * 中文
     */
    private String cnName;
    /**
     * 英文
     */
    private String enName;
    /**
     * 排序
     */
    private String sort;
    /**
     * 操作人
     */
    private String recorder;
    /**
     * 操作时间
     */
    private Date recordTime;
}
