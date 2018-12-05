package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: James
 * @Date: 2018/10/26 14:28
 * @Description:
 */
@Entity
@Getter
@Setter
public class CcSalesCommissionsConf implements Serializable {
     private int id;
     /*
        底薪区间 顺序
     */
    private int sort;
     /*
        底薪最小值
     */
    private int intervalMinValue;
     /*
        底薪最大值
     */
    private int intervalMaxValue;
      /*
        底薪最小值符号
     */
    private int intervalMinSign;
     /*
    底薪最大值符号
     */
    private int intervalMaxSign;
    /*
    提成比例
     */
    private BigDecimal percentage;
    /*
    base_salary_info  id
     */
    private int salesCcommissionId;

}
