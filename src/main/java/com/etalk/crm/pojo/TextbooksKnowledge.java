package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/10/24 14:33
 * @Version 1.0
 * @Description 教材页码知识点关联
 **/
@Getter
@Setter
public class TextbooksKnowledge {
    /**
     * 知识点ID
     */
    private Integer qknowledgeId;
    /**
     * 教材ID
     */
    private Integer textbooksId;
    /**
     * 页码索引
     */
    private Integer textbooksPage;
    /**
     * 页码
     */
    private String textbooksPageName;
    /**
     * 创建人
     */
    private String recorder;
    /**
     * 创建时间
     */
    private Date recordTime;
}
