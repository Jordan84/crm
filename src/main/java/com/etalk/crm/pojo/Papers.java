package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;


@Entity
@Setter
@Getter
public class Papers implements Serializable {
    /**
     * 问卷ID号
     */
    private int paperId;
    /**
     * 用户ID
     */
	private int addUserId;
    /**
     * 问卷标题
     */
	private String paperTitle;

    /**
     * 问卷说明
     */
    private  String  paperSummary;

    /**
     * 问卷发布日期
     */
	private Date createDate;

//	 /**
//     * 问卷分类
//     */
//	private int paperType;

    /**
     * /问卷状态
     */
	private int paperStatus;

    /**
     * 问卷回答次数
     */
	private int paperCount;

    /**
     * 添加人
     */
	private String addUserName;

}
