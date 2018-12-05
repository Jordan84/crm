package com.etalk.crm.dao;

import com.etalk.crm.pojo.MobileQuestion;
import com.etalk.crm.pojo.QuestionInfo;
import com.etalk.crm.pojo.QuestionResultRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/6 15:55
 * @Version 1.0
 * @Description 移动端试题
 **/
@Mapper
public interface MobileQuestionMapper {
    /**
     * 获取当前所学课程列表
     *
     * @param personId
     * @return
     */
    List<Map> getLearingLessonListByPersonId(Integer personId);

    /**
     * 查询版本号以及所有级别的题目数目
     *
     * @param personId    用户ID
     * @param lessonId    课程ID
     * @param knowledgeId 知识点ID
     * @return
     */
    List<QuestionInfo> getQuestionInfo(@Param("personId") Integer personId, @Param("lessonId") Integer lessonId, @Param("qknowledgeId") Integer knowledgeId);

    /**
     * 获取题目
     *
     * @param personId    用户ID
     * @param lessonId    课程ID
     * @param knowledgeId 课程ID
     * @param level       难度级别
     * @param version     版本号
     * @return
     */
    List<MobileQuestion> getCurrentQuestionList(@Param("personId") Integer personId, @Param("lessonId") Integer lessonId, @Param("qknowledgeId") Integer knowledgeId, @Param("level") Integer level, @Param("version") Integer version, @Param("flag") Integer flag);

    /**
     * 检测是否做过题目
     *
     * @param questionResultRecord
     * @return
     */
    int checkQuestionDone(QuestionResultRecord questionResultRecord);

    /**
     * 提交答案
     *
     * @param questionResultRecord
     * @return
     */
    int addQuestionRecord(QuestionResultRecord questionResultRecord);

    /**
     * 更新做题结果
     *
     * @param questionResultRecord
     * @return
     */
    int updateQuestionRecord(QuestionResultRecord questionResultRecord);
}
