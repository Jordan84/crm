package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Terwer
 * @Date 2018/11/12 19:07
 * @Version 1.0
 * @Description 正在上的课程
 **/
@Getter
@Setter
public class KcOrdersMaterial {
    /**
     * 订单关联教材表id
     */
    private String orderBookId;
    /**
     *教材中文名
     */
    private String materialCnName;
    /**
     * 教材英文名
     */
    private String materialEnName;
    /**
     * 教材已上当前页
     */
    private int materialPage;
}
