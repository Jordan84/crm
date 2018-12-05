package com.etalk.crm.dao;

import com.etalk.crm.pojo.ActivityGive;
import com.etalk.crm.pojo.ActivityGiveLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/26 10:48
 * @Version 1.0
 * @Description 活动赠送
 **/
@Mapper
public interface ActivityGiveMapper {
    /**
     * 获取活动赠送列表
     *
     * @return
     */
    List<ActivityGive> getActivityGiveList();

    /**
     * 检测用户，返回系统中有的
     *
     * @param names
     * @return
     */
    List<String> checkStudentExists(List<String> names);

    /**
     * 发布活动赠送
     *
     * @param activityGive
     * @return
     */
    Integer insertActivityGive(ActivityGive activityGive);

    /**
     * 获取赠送记录列表
     *
     * @param search
     * @return
     */
    List<ActivityGiveLog> getActivityGiveLogList(@Param("search") String search);

    /**
     * 添加活动赠送记录
     *
     * @param activityGiveLog
     * @return
     */
    Integer insertActivityGiveLog(ActivityGiveLog activityGiveLog);

    /**
     * 获取赠送名单
     *
     * @param agId
     * @return
     */
    List<Map> getActivityGiveUsers(Integer agId);
}
