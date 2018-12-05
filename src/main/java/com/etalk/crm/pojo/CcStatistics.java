package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: James
 * @Date: 2018/10/17 11:24
 * @Description:作为销售总监，希望CC可以看到渠道跟进数据，以便CC了解自己的资源情况（225）
 */
@Entity
@Getter
@Setter
public class CcStatistics implements Serializable {
    private int id;

    private int leadsCounter;

    private int  demoCounter;

    /**
     * 合同 统计数据量
     */
    private int orderCounter;

    /**
     * demo/ctr
     */
    private double ratio;

     /**
     *所有 cc 中 demo/ctr  最大值
     */
    private double maxRatio;

    /**
     * 渠道名字
     */
    private String chinnelName;

    private String  time;

    private int  days;

    /**
     * 最近 一个  订单时间
     */
    private Date maxBuyTime;

    private int totalAmount;

    private int deposit;

    private Date date;

    /*
    打败 cc  百分比
     */
    private BigDecimal percentage;
     /*
     平均关单天数
     */
    private BigDecimal closeLeadsDays;

    /**
     * 关单 最大值
     */
    private BigDecimal maxDemoCounter;

    /*
      数据类型 1 int 2  BigDecimal
     */
    private int dataType;

}
