package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class OrderBooks implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 用户id
     */
    private Integer personId;
    /**
     * 教材id
     */
    private Integer textbooksId;
    /**
     * 教材上课顺序
     */
    private Integer sequence;
    /**
     * 整本教材是否已上完（0未上完，1已上完）
     */
    private Integer finish;
    /**
     * 材已学当前页
     */
    private String currentpage;
    
    private Integer pageindex;

    /**
     * 教材最大页
     */
    private Integer maxpage;

    /**
     * 当前教材页学习状态（0未上完、1已上完）
     */
    private Integer complete;

    /**
     * 教材设置人
     */
    private String register;

    /**
     * 教材设置时间
     */
    private Date registTime;

    private Integer storesId;
    /**
     * 订单学生
     */
    private String studentName;
    /**
     * 套餐名称
     */
    private String packageName;
    /**
     * 所属机构信息
     */
    private Stores stores;
    
    private KcOrders kcOrders;
    
    private Textbooks textbooks;
    /**
     * 统计学生上课数
     */
    private int countLessons;
}