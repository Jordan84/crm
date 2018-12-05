package com.etalk.crm.service;

import com.etalk.crm.pojo.IntroduceGift;
import com.etalk.crm.pojo.IntroduceGiftLog;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/28 15:45
 * @Version 1.0
 * @Description 转介绍
 **/
public interface IntroduceService {

    /**
     * 根据ID查询转介绍礼品
     *
     * @param id
     * @return
     */
    IntroduceGift getIntroduceGiftById(Integer id);

    /**
     * 获取转介绍配置列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<IntroduceGift> getIntroduceGiftList(String search, Integer pageNum, Integer pageSize);

    /**
     * 添加转介绍配置
     *
     * @param introduceGift
     * @return
     */
    Integer insertIntroduceGift(IntroduceGift introduceGift);

    /**
     * 修改转介绍配置
     *
     * @param introduceGift
     * @return
     */
    boolean updateIntroInfo(IntroduceGift introduceGift);

    /**
     * 修改转介绍配置
     *
     * @param state
     * @param introId
     * @return
     */
    boolean updateIntroState(Integer state, Integer introId);

    /**
     * 获取赠礼记录列表
     *
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<IntroduceGiftLog> getIntroduceGiftLogList(String search, Integer pageNum, Integer pageSize);

    /**
     * 获取转介绍节点列表
     *
     * @return
     */
    PageInfo<Map> selectIntroduceNodeList(String search, Integer pageNum, Integer pageSize);
}
