package com.etalk.crm.dao;

import com.etalk.crm.pojo.SchoolCollectDate;
import com.etalk.crm.pojo.SchoolCollectInfo;
import com.etalk.crm.pojo.SchoolCollectTime;
import com.etalk.crm.pojo.SchoolInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SchoolMapper {
    /**
     * 查询区域
     *
     * @param parentId
     * @return
     */
    List<Map<String, Object>> selectAreaByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据区域查询学校
     *
     * @param areaCode
     * @return
     */
    List<SchoolInfo> selectSchoolByAreaCode(@Param("areaCode") Integer areaCode);

    /**
     * 校验学校名称的唯一性
     *
     * @param schoolName
     * @return
     */
    Integer checkSchoolName(@Param("schoolName") String schoolName);

    /**
     * 保存学校信息
     *
     * @param schoolInfo
     * @return
     */
    Integer saveSchoolInfo(SchoolInfo schoolInfo);

    /**
     * 保存采集基本信息
     *
     * @param schoolCollectInfo
     * @return
     */
    Integer saveSchoolCollectInfo(SchoolCollectInfo schoolCollectInfo);

    /**
     * 批量保存采集时间
     *
     * @param schoolCollectTimeList
     * @return
     */
    Integer batchSaveSchoolCollectTime(List<SchoolCollectTime> schoolCollectTimeList);

    /**
     * 批量保存采集日期
     *
     * @param schoolCollectDateList
     * @return
     */
    Integer batchSaveSchoolCollectDate(List<SchoolCollectDate> schoolCollectDateList);

    /**
     * 获取学校采集信息列表
     */
    List<SchoolCollectInfo> selectSchoolCollectInfoList(@Param("search") String search, @Param("province") String province, @Param("city") String city, @Param("area") String area, @Param("schoolId") Integer schoolId, @Param("effect") Integer effect, @Param("hasPolice") Integer hasPolice);

    /**
     * 根据ID查询学校采集信息
     *
     * @param collectId
     * @return
     */
    SchoolCollectInfo selectByCollectId(@Param("collectId") Integer collectId);

    /**
     * 根据学校ID查询学校信息
     *
     * @param schoolId
     * @return
     */
    SchoolInfo selectSchoolInfoById(@Param("schoolId") Integer schoolId);

    /**
     * 查询具体学校的采集时间
     *
     * @param schoolId
     * @return
     */
    List<SchoolCollectTime> selectSchoolCollectTime(@Param("schoolId") Integer schoolId);

    /**
     * 查询具体学校的采集日期
     *
     * @param schoolId
     * @return
     */
    List<SchoolCollectDate> selectScchoolCollectDate(@Param("schoolId") Integer schoolId);

    /**
     * 清空采集时间
     *
     * @param schoolId
     * @return
     */
    Integer deleteSchoolCollectTime(@Param("schoolId") Integer schoolId);

    /**
     * 清空采集日期
     *
     * @param schoolId
     * @return
     */
    Integer deleteSchoolCollectDate(@Param("schoolId") Integer schoolId);

    /**
     * 更新学校信息
     *
     * @param schoolInfo
     * @return
     */
    Integer updateSchoolInfo(SchoolInfo schoolInfo);

    /**
     * 更新采集基本信息
     *
     * @param schoolCollectInfo
     * @return
     */
    Integer updateSchoolCollectInfo(SchoolCollectInfo schoolCollectInfo);

    Integer checkCollectDateExist(@Param("schoolId") Integer schoolId, @Param("collectDate") Date collectDate);
}
