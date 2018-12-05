package com.etalk.crm.dao;

import com.etalk.crm.pojo.IntroduceGift;
import com.etalk.crm.pojo.IntroduceGiftLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/28 11:53
 * @Version 1.0
 * @Description 转介绍
 **/
@Mapper
public interface IntroduceGiftMapper {

    /**
     * 根据ID查询转介绍礼品
     *
     * @param id
     * @return
     */
    IntroduceGift selectIntroduceGiftById(Integer id);

    /**
     * 获取转介绍配置列表
     *
     * @return
     */
    List<IntroduceGift> getIntroduceGiftList(String search);

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
    Integer updateIntroduceGiftById(IntroduceGift introduceGift);

    /**
     * 更强其他为禁用
     *
     * @param introId
     * @return
     */
    Integer updateIntroduceGiftExludeId(Integer introId);

    /**
     * 获取赠送记录列表
     *
     * @param search
     * @return
     */
    List<IntroduceGiftLog> getIntroduceGiftLogList(@Param("search") String search);

    /**
     * 转介绍赠送记录
     *
     * @param introduceGiftLog
     * @return
     */
    Integer insertIntroduceGiftLog(IntroduceGiftLog introduceGiftLog);

    /**
     * 获取转介绍节点列表
     *
     * @return
     */
    List<Map> selectIntroduceNodeList(@Param("search") String search);
}
