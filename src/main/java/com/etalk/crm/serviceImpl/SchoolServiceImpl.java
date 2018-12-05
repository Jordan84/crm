package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.dao.SchoolMapper;
import com.etalk.crm.pojo.SchoolCollectDate;
import com.etalk.crm.pojo.SchoolCollectInfo;
import com.etalk.crm.pojo.SchoolCollectTime;
import com.etalk.crm.pojo.SchoolInfo;
import com.etalk.crm.service.SchoolService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SchoolServiceImpl implements SchoolService {
    private static final Logger logger = LogManager.getLogger(SchoolServiceImpl.class);

    @Resource
    private SchoolMapper schoolMapper;

    @Override
    public List<Map<String, Object>> getCityData() {
        List<Map<String, Object>> resultList = this.getChildCityData(null);
        return resultList;
    }

    public List<Map<String, Object>> getChildCityData(Integer parentId) {
        List<Map<String, Object>> child = schoolMapper.selectAreaByParentId(parentId);
        return child;
    }

    @Override
    public List<SchoolInfo> selectSchoolByAreaCode(Integer areaCode) {
        return schoolMapper.selectSchoolByAreaCode(areaCode);
    }

    @Override
    public boolean checkSchoolName(String schoolName) {
        Integer count = schoolMapper.checkSchoolName(schoolName);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public boolean initSchoolColectInfo(SchoolCollectInfo schoolCollectInfo) {

        logger.info("提交采集信息:SchoolCollectInfo=" + JSON.toJSONString(schoolCollectInfo));
        //新增
        if (schoolCollectInfo.getId() == null || schoolCollectInfo.getId() == 0) {
            logger.info("添加采集信息");
            SchoolInfo schoolInfo = schoolCollectInfo.getSchoolInfo();
            schoolInfo.setAreaCode(schoolCollectInfo.getAreaCode());
            //保存学校信息
            schoolMapper.saveSchoolInfo(schoolInfo);
            //保存采集基本信息
            schoolCollectInfo.setSchoolId(schoolInfo.getId());
            schoolMapper.saveSchoolCollectInfo(schoolCollectInfo);

        } else {//修改
            logger.info("修改采集信息");
            SchoolInfo schoolInfo = schoolCollectInfo.getSchoolInfo();
            schoolInfo.setId(schoolCollectInfo.getSchoolId());
            schoolInfo.setAreaCode(schoolCollectInfo.getAreaCode());
            //修改学校信息
            schoolMapper.updateSchoolInfo(schoolInfo);
            //修改采集基本信息
            schoolMapper.updateSchoolCollectInfo(schoolCollectInfo);
        }

        //先清空采集时间
        schoolMapper.deleteSchoolCollectTime(schoolCollectInfo.getSchoolId());
        //保存采集时间
        List<SchoolCollectTime> schoolCollectTimeList = new ArrayList<>();
        for (SchoolCollectTime collectTime : schoolCollectInfo.getCollectTime()) {
            if (!StringUtils.isEmpty(collectTime.getCollectTime())) {
                collectTime.setSchoolId(schoolCollectInfo.getSchoolId());
                schoolCollectTimeList.add(collectTime);
            }
        }
        if (schoolCollectTimeList.size() > 0) {
            schoolMapper.batchSaveSchoolCollectTime(schoolCollectTimeList);
        }

        //先清空最近5个采集日期
        //schoolMapper.deleteSchoolCollectDate(schoolCollectInfo.getSchoolId());
        //保存采集日期
        List<SchoolCollectDate> schoolCollectDateList = new ArrayList<>();
        for (SchoolCollectDate collectDate : schoolCollectInfo.getCollectDate()) {
            //如果不存在采集日期才去更新
            if (!StringUtils.isEmpty(collectDate.getCollectDate()) && schoolMapper.checkCollectDateExist(schoolCollectInfo.getSchoolId(), collectDate.getCollectDate()) == 0) {
                collectDate.setSchoolId(schoolCollectInfo.getSchoolId());
                schoolCollectDateList.add(collectDate);
            }
        }
        if (schoolCollectDateList.size() > 0) {
            schoolMapper.batchSaveSchoolCollectDate(schoolCollectDateList);
        }
        return true;
    }

    @Override
    public PageInfo<SchoolCollectInfo> getSchoolCollectInfoList(String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SchoolCollectInfo> list = schoolMapper.selectSchoolCollectInfoList(search, province, city, area, schoolId, effect, hasPolice);
        //搜索采集时间以及采集日期
        for (SchoolCollectInfo collectInfo : list) {
            List<SchoolCollectTime> schoolCollectTimeList = schoolMapper.selectSchoolCollectTime(collectInfo.getSchoolId());
            List<SchoolCollectDate> schoolCollectDateList = schoolMapper.selectScchoolCollectDate(collectInfo.getSchoolId());
            collectInfo.setCollectTime(schoolCollectTimeList);
            collectInfo.setCollectDate(schoolCollectDateList);
        }
        // 分页信息
        PageInfo<SchoolCollectInfo> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：search=" + search);
        return pageInfo;
    }

    @Override
    public List<SchoolCollectInfo> getSchoolCollectInfoListToExport(String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice) {
        List<SchoolCollectInfo> list = schoolMapper.selectSchoolCollectInfoList(search, province, city, area, schoolId, effect, hasPolice);
        //搜索采集时间以及采集日期
        for (SchoolCollectInfo collectInfo : list) {
            List<SchoolCollectTime> schoolCollectTimeList = schoolMapper.selectSchoolCollectTime(collectInfo.getSchoolId());
            List<SchoolCollectDate> schoolCollectDateList = schoolMapper.selectScchoolCollectDate(collectInfo.getSchoolId());
            collectInfo.setCollectTime(schoolCollectTimeList);
            collectInfo.setCollectDate(schoolCollectDateList);
        }
        return list;
    }

    @Override
    public SchoolCollectInfo selectByCollectId(Integer collectId) {
        SchoolCollectInfo schoolCollectInfo = schoolMapper.selectByCollectId(collectId);
        List<SchoolCollectTime> schoolCollectTimeList = schoolMapper.selectSchoolCollectTime(schoolCollectInfo.getSchoolId());
        List<SchoolCollectDate> schoolCollectDateList = schoolMapper.selectScchoolCollectDate(schoolCollectInfo.getSchoolId());
        schoolCollectInfo.setCollectTime(schoolCollectTimeList);
        schoolCollectInfo.setCollectDate(schoolCollectDateList);
        return schoolCollectInfo;
    }

    @Override
    public SchoolInfo selectSchoolInfoById(Integer schoolId) {
        return schoolMapper.selectSchoolInfoById(schoolId);
    }
}
