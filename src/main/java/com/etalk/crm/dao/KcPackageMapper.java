package com.etalk.crm.dao;

import com.etalk.crm.pojo.KcPackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/8 14:01
 * @Version 1.0
 * @Description 套餐
 **/
@Mapper
public interface KcPackageMapper {
    /**
     * 获取促销套餐列表
     *
     * @param search
     * @return
     */
    List<Map> getPromationKcPackageList(@Param("search") String search);

    /**
     * 根据套餐id查询套餐信息
     *
     * @param id 套餐id
     * @return 套餐信息
     */
    KcPackage getPackegeInfoById(int id);
}
