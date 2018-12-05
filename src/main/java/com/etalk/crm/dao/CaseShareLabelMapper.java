package com.etalk.crm.dao;

import com.etalk.crm.pojo.CaseShareLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 16:10
 * @Description: CaseShareLabelMapper
 */
@Mapper
public interface CaseShareLabelMapper {
    /**
     * casesharetype 列表
     * @return
     */
    List<CaseShareLabel> selectLabelList();

    /**
     * 根据  多个 id  查询标签列表
     * @param list
     * @return
     */
    List<CaseShareLabel> selectByIds (List<Integer> list);
}
