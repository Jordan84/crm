package com.etalk.crm.service;

import com.etalk.crm.pojo.MobileQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 * @since 2018/11/6
 */
public interface MobileQuestionService {

    /**
     * 获取当前所学课程列表
     *
     * @param personId 用户ID
     * @return
     */
    List<Map> getLearingLessonList(Integer personId);

    /**
     * 查询版本号以及所有级别的题目数目
     *
     * @param personId    用户ID
     * @param lessonId    课程ID
     * @param knowledgeId 知识点ID
     * @param unitId      单元ID
     * @return
     */
    Map getQuestionInfo(Integer personId, Integer lessonId, Integer knowledgeId, Integer unitId);

    /**
     * 获取题目
     *
     * @param personId    用户ID
     * @param lessonId    课程ID
     * @param knowledgeId 知识点ID
     * @param unitId      单元ID
     * @param level       难度级别
     * @param version     版本号
     * @return
     */
    MobileQuestion getCurrentQuestion(Integer personId, Integer lessonId, Integer knowledgeId, Integer unitId, Integer level, Integer version);

    /**
     * 提交答案
     *
     * @param personId     用户ID
     * @param lessonId     课程ID
     * @param qknowledgeId 知识点ID
     * @param unitId       单元ID
     * @param qrId         题目ID
     * @param qrResult     用户选的答案
     * @param isTrue       是否答对
     * @param version      版本号
     * @return
     */
    boolean submitAnswer(Integer personId, Integer lessonId, Integer qknowledgeId, Integer unitId, Integer qrId, String qrResult, Integer isTrue, Integer version);
}
