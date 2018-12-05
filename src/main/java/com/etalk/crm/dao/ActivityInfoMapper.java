package com.etalk.crm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/8 14:02
 * @Version 1.0
 * @Description 活动
 **/
@Mapper
public interface ActivityInfoMapper {
    /**
     * 获取活动列表
     *
     * @param search
     * @return
     */
    List<Map> getActivityList(@Param("search") String search);
}
