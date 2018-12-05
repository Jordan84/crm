package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: James
 * @Date: 2018/10/26 14:33
 * @Description:销售提成配置
 */
@Getter
@Setter
@Entity
public class SalesCommissionInfo implements Serializable {
     private int id ;
    /**
     * 底薪表 名称
     */
    private String salesCommissionName;
     /*
    添加时间
     */
    private Date addTime;
    /*
    添加人
     */
    private int  addUserId;
    /*
    转台  0：无效   1：有效
     */
    private int state;
    /**
     * 配置  区间
     */
   private String addUserName;
}
