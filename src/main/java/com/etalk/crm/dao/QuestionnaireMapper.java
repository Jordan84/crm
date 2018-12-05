package com.etalk.crm.dao;

import com.etalk.crm.pojo.QuestionnaireOc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * oc课后问卷调查
 */
@Mapper
public interface QuestionnaireMapper {


    /**
    *oc课问卷调查插入操作
    * @param questionnaireOc
    * @return
    */
    int addQoc(QuestionnaireOc questionnaireOc);

    /**
    *查询oc 问卷调查回访记录
    * @param startTime
    * @param endTime
    * @param teacher
    * @return
    */
    List<QuestionnaireOc> questionnaireOcList(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("teacher")String teacher);

    /**
    *
    * @param questionnaireId
    * @return
    */
    QuestionnaireOc selectByQCId(@Param("questionnaireId")Integer questionnaireId);


    /**
    * 根据多个id查询列表
    * @param ids
    * @return
    */
    List<QuestionnaireOc> selectQcByIds(Integer [] ids);

    /**
     * 查询记录是否存在
     * @param personId
     * @return
     */
    int selectByPersonId(@Param("personId")Integer personId);
}
