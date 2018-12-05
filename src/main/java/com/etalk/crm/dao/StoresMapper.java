package com.etalk.crm.dao;

import com.etalk.crm.pojo.Stores;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Jordan
 */
@Mapper
public interface StoresMapper {

    /**
     * 查询机构信息
     * @param id 机构id
     * @return 机构信息
     */
    Stores selectStoreInfoById(@Param("id") Integer id);
}
