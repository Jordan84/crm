package com.etalk.crm.service;

import com.etalk.crm.pojo.KcPackage;

import java.util.List;

/**
 * @Author Terwer
 * @Date 2018/11/12 18:24
 * @Version 1.0
 * @Description 订单
 **/
public interface KcOrdersService {
    /**
     * 活动赠送赠送课时添加订单
     *
     * @param names        待赠送的用户列表
     * @param packageType  1即时生效,2正式套餐或促销后生效
     * @param kcPackage 套餐
     * @param activityId   活动id
     * @param activityName 活动或者礼品名称
     * @param isGift       是否赠送礼品
     * @return
     */
    boolean addKcOrdersFromActivity(List<String> names, int packageType, KcPackage kcPackage, int activityId, String activityName, int isGift);
}
