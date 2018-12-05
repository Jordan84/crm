package com.etalk.crm.dao;

import com.etalk.crm.pojo.QuestionaireOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionaireOptionMapper {
    /**
     * 添加选项
     * @param questionaireOption
     * @return
     */
    int addOption(QuestionaireOption questionaireOption);

    /**
     * 根据问卷题目查找选项
     * @return
     */
    List<QuestionaireOption> selectByQuestionId(@Param("questionId")Integer questionId);
}
