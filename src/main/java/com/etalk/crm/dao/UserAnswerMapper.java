package com.etalk.crm.dao;

import com.etalk.crm.pojo.QuestionUserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAnswerMapper {
    /**
     * 添加用户提交的答案
     * @param questionUserAnswer
     * @return
     */
   int  addUserAnswer(QuestionUserAnswer questionUserAnswer);

    /**
     * 查询用户选择的 问卷  答案
     * @param questionId
     * @param personId
     * @return
     */
   QuestionUserAnswer selectByQPId(@Param("questionId")Integer questionId, @Param("personId")Integer personId);

}
