package com.etalk.crm.dao;

import com.etalk.crm.pojo.QuestionnaireQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionnaireQuestionMapper {
    /**
     * 添加问卷问题
     * @param questionnaireQuestion
     * @return
     */
    int addQuestion(QuestionnaireQuestion questionnaireQuestion);

    /**
     * 根据问卷id 查询该问卷下 的问题
     * @param paperId
     * @return
     */
    List<QuestionnaireQuestion> selectByPaperId(@Param("paperId")Integer paperId);
}
