package com.etalk.crm.dao;

import com.etalk.crm.pojo.Papers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PapersMapper {
    /**
     * 添加问卷
     * @param papers
     * @return
     */
    int addPaper (Papers papers);

    /**
     * 查询问卷
     * @return
     */
    Papers selectByState ();

    /**
     * 根据主键查询问卷
     * @return
     */
    Papers selectById(@Param("paperId")Integer paperId);

    /**
     * 查询所有的 问卷
     * @return
     */
    List<Papers> paperList();

    /**
     * 更改问卷的状态
     * @param paperId
     * @return
     */
    int updatePaper(@Param("paperId")Integer paperId);
    /**
     * 更改问卷的状态
     * @param paperId
     * @return
     */
    int updatePaper2(@Param("paperId")Integer paperId);
}
