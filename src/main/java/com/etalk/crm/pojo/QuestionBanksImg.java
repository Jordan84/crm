package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Terwer
 */
@Entity
@Getter
@Setter
public class QuestionBanksImg {
    /**
     * 图片ID
     */
    private Integer id;
    /**
     * 图片对应的题目ID
     */
    private Integer qbankId;
    /**
     * 图片标题
     */
    private String imgTitle;
    /**
     * 图片链接
     */
    private String imgUrl;
    /**
     * 排序
     */
    private Integer sort;
}
