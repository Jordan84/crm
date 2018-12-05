package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/9/26 10:45
 * @Version 1.0
 * @Description 活动赠送
 **/
@Getter
@Setter
public class ActivityGive {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 活动名称
     */
    private String activity;
    /**
     * 活动ID
     */
    private Integer activity_primary_key;
    /**
     * 是否有礼品
     */
    private Integer isGift;
    /**
     * 礼物名称
     */
    private String gift;
    /**
     * 礼物ID
     */
    private Integer gift_primary_key;
    /**
     * 礼品描述
     */
    private String giftDesc;
    /**
     * 课程生效（1即使生效,2正式套餐或促销后生效）
     */
    private Integer packageType;
    /**
     * 创建人
     */
    private String recorder;
    /**
     * 创建时间
     */
    private Date recordTime;
}
