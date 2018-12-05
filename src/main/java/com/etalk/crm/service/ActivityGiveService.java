package com.etalk.crm.service;

import com.etalk.crm.pojo.ActivityGive;
import com.etalk.crm.pojo.ActivityGiveLog;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/28 18:02
 * @Version 1.0
 * @Description 活动赠送
 **/
public interface ActivityGiveService {
    /**
     * 获取活动赠送列表
     *
     * @return
     */
    PageInfo<ActivityGive> getActivityGiveList(Integer pageNum, Integer pageSize);

    /**
     * 上传excel文件到临时目录后并开始解析
     *
     * @param fileName
     * @param mfile
     * @return
     */
    Map batchImport(String fileName, MultipartFile mfile);

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
     * @param names
     * @param session
     * @return
     */
    boolean publishActivityGive(ActivityGive activityGive, List<String> names, HttpSession session);

    /**
     * 获取活动列表
     *
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Map> getActivityList(String search, Integer pageNum, Integer pageSize);

    /**
     * 获取活动赠礼记录列表
     *
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ActivityGiveLog> getActivityGiveLogList(String search, Integer pageNum, Integer pageSize);

    /**
     * 获取赠送名单
     *
     * @param agId     活动ID
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Map> getActivityGiveUsers(Integer agId, Integer pageNum, Integer pageSize);

    /**
     * 导出赠送名单
     *
     * @param agId
     * @return
     */
    List<Map> getActivityGiveUsersForExport(Integer agId);
}
