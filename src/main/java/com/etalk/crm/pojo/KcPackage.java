package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class KcPackage implements Serializable {
    private Integer id;
    private Integer classifyId;
    private List<Integer> listClassifyId;
    private Integer classifyLevel = 0;
    private Integer pgroupId;
    private String name;
    /**
     * 第一级目录名称
     */
    private String firstName;
    /**
     * 第二级目录名称
     */
    private String secondName;
    /**
     * 总课时
     */
    private Integer lessonCount;
    /**
     * 有效期/天数
     */
    private Integer valid;
    private Integer classCount;
    private Double price;
    /**
     * 套餐说明
     */
    private String introduce;
    private Integer state;
    /**
     * 礼包
     */
    private Integer gift;
    private String recorder;
    private Date recordTime;
    private Integer storesId;
    /**
     * 赠送积分
     */
    private Integer giveIntegral;
    /**
     * 常用老师是否跨组（1允许，0不允许）
     */
    private Integer crossGroupFlag;
    /**
     * 每次上课时长
     */
    private Integer classLong;
    /**
     * 按实际课程17分钟算，课程时长占用几节课
     */
    private Integer classNum;
    /**
     * 所属机构信息
     */
    private Stores stores;
    private KcClassify kcClassify;

    private CurrencyType currencyType;

    private PersonGroup personGroup;
    /**
     * 关联的教材组别id
     */
    private Integer groupId;
    /**
     * 能否积分兑换标志 0为可以1为不可以
     */
    private String flag;
    /**
     * 兑换所需积分
     */
    private int integration;
    /**
     * 套餐分类  0.正常套餐1.体验课(DEMO)2.包月套餐3.促销套餐4.公开课套餐5.OPT
     */
    private String typeFlag;
    /**
     * 套餐支付级别：0.课时，1、白银，2、铂金，3、钻石
     */
    private int level;
    /**
     * 教材类型 0非所有教材1所有教材
     */
    private String textbooksType;
}