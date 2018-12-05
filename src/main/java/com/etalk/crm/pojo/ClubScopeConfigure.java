package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @Auther: James
 * @Date: 2018/10/23 10:46
 * @Description: 底薪配置表
 */
@Entity
@Getter
@Setter
public class ClubScopeConfigure implements Serializable {
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
    底薪
     */
    private int salesClubType;
    /*
    base_salary_info  id
     */
    private int clubConfigureId;

}
