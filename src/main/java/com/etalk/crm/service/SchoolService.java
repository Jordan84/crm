package com.etalk.crm.service;

import com.etalk.crm.pojo.SchoolCollectInfo;
import com.etalk.crm.pojo.SchoolInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 * @date 2018/06/12
 */
public interface SchoolService {
    /**
     * 获取城市下拉数据
     *
     * @return
     */
    List<Map<String, Object>> getCityData();

    /**
     * 根据区域查询学校
     *
     * @param areaCode
     * @return
     */
    List<SchoolInfo> selectSchoolByAreaCode(Integer areaCode);

    /**
     * 校验学校名称的唯一性
     *
     * @param schoolName
     * @return
     */
    boolean checkSchoolName(String schoolName);

    /**
     * 后台初始化学校及采集信息
     *
     * @param schoolCollectInfo
     * @return
     */
    boolean initSchoolColectInfo(SchoolCollectInfo schoolCollectInfo);

    /**
     * 获取采集信息列表
     *
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<SchoolCollectInfo> getSchoolCollectInfoList(String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice, Integer pageNum, Integer pageSize);

    /**
     * 查询数据用于导出Excel
     *
     * @param search
     * @param province
     * @param city
     * @param area
     * @param schoolName
     * @param effect
     * @param hasPolice
     * @return
     */
    List<SchoolCollectInfo> getSchoolCollectInfoListToExport(String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice);

    /**
     * 根据ID查询学校采集信息
     *
     * @param collectId
     * @return
     */
    SchoolCollectInfo selectByCollectId(Integer collectId);

    /**
     * 根据学校ID查询学校信息
     *
     * @param schoolId
     * @return
     */
    SchoolInfo selectSchoolInfoById(Integer schoolId);
}
