package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/9/28 15:08
 * @Version 1.0
 * @Description 赠送记录
 **/
@Getter
@Setter
public class IntroduceGiftLog {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 转介绍ID
     */
    private Integer introduceId;
    /**
     * 推荐人ID
     */
    private Integer referrerId;
    /**
     * 推荐人名称
     */
    private String referrerName;
    /**
     * 学员登录名
     */
    private String loginName;
    /**
     * 事件
     */
    private String event;
    /**
     * 添加人
     */
    private String recorder;
    /**
     * 添加时间
     */
    private Date recordTime;
}
