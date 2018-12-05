package com.etalk.crm.dao;

import com.etalk.crm.pojo.CaseShareType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 16:10
 * @Description:CaseShareTypeMapper
 */
@Mapper
public interface CaseShareTypeMapper {
    /**
     * casesharetype 列表
     * @return
     */
    List<CaseShareType> selectSourceTypeList();

}
