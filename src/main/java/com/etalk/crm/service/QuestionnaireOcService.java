package com.etalk.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface QuestionnaireOcService {
    int addQoc (HttpSession session, @RequestBody JSONObject jsonObject);

    /**
     *添加问卷
     * @param papers
     * @return
     */
    int addPaper(Papers papers);

    /**
     * 添加问题
     * @param questionnaireQuestion
     * @return
     */
    int addQuestion(QuestionnaireQuestion questionnaireQuestion);

    /**
     * 添加问卷选项
     * @param QuestionaireOption
     * @return
     */
    int addOption(QuestionaireOption QuestionaireOption);

    /**
     * 查询可用的 问卷
     * @return
     */
    Papers selectByState();

    /**
     * 根据问题paperId 查询问题列表
     * @param paperId
     * @return
     */
    List<QuestionnaireQuestion> selectByPaperId(Integer paperId);

  /**
     * 根据问卷题目查找选项
     * @return
     */
    List<QuestionaireOption> selectByQuestionId(Integer questionId);

    /**
     * 存贮用户问卷答案
     * @param questionUserAnswer
     * @return
     */
    int addUserAnser(QuestionUserAnswer questionUserAnswer);

    /**
     * 添加学生动态
     * @param studentDynamics
     * @return
     */
    int addStuDynamic(StudentDynamics studentDynamics);

    /**
     * 根据paperId  查询问卷
      * @param paperId
     * @return
     */
    Papers selectPapersByPaperId(Integer paperId);

     /**
     * 查询用户选择的 问卷  答案
     * @param questionId
     * @param personId
     * @return
     */
   QuestionUserAnswer selectByQPId(Integer questionId,Integer personId);

   /**
     *查询oc 问卷调查回访记录
     * @param startTime
     * @param endTime
     * @param teacher
     * @return
     */
   PageInfo<QuestionnaireOc> questionnaireOcList(String startTime, String endTime, String teacher,int pageNum, int pageSize);

   /**
     * 查询所有的 问卷
     * @return
     */
    PageInfo<Papers> paperList(Integer pageNum,Integer pageSize);

    /**
     * 查询所有的 问卷管理记录
     * @return
     */
    List<QuestionnaireOc> SelectquestionnaireAll();

    /**
     * 更改问卷的状态
     * @param paperId
     * @return
     */
    int updatePaper(@Param("paperId")Integer paperId);

    /**
     *
     * @param questionnaireId
     * @return
     */
   QuestionnaireOc selectByQCId(Integer questionnaireId);

    /**
     * 查询SSCteacher
     * @return
     */
   List<Person> selectSSCTeacher();


  /**
     * 根据多个id查询列表
     * @param ids
     * @return
     */
   List<QuestionnaireOc> selectQcByIds(Integer [] ids);

    /**
     * 导出  oc 问卷数据
     * @return
     */
   List<QuestionnaireOc> exportData(String startTime,String endTime,String teacher);

}
