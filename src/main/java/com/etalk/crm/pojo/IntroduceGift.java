package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/9/28 11:52
 * @Version 1.0
 * @Description 转介绍
 **/
@Getter
@Setter
public class IntroduceGift {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 配置名称
     */
    private String name;
    /**
     * 节点名称
     */
    private String node;
    /**
     * 节点
     */
    private Integer node_primary_key;
    /**
     * 赠礼类型
     */
    private Integer giftType;
    /**
     * 礼物名称
     */
    private String gift;
    /**
     * 礼物
     */
    private Integer gift_primary_key;
    /**
     * 礼品描述
     */
    private String giftDesc;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 学员类型
     */
    private Integer studentType;
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
