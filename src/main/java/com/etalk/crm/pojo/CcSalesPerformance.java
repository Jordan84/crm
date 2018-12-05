package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: James
 * @Date: 2018/9/13 14:37
 * @Description: cc  销售目标
 */
@Entity
@Getter
@Setter
public class CcSalesPerformance implements Serializable {
    private int id ;
     /**
     * 操作人
     */
    private int addUserId;
     /**
     * 销售目标
     */
    private int salesTarget;
    /**
     *添加日期
     */
    private Date addTime;
    /**
     * 搜索日期
     */
    private String startDate;
     /**
     * 课程顾问
     */
    private int ccId;

    /**
     * 月销售额
     */
    private int total;

}
