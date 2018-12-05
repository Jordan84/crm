package com.etalk.crm.dao;

import java.util.List;
import java.util.Map;

import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionInputAnswer;
import com.etalk.crm.pojo.StudentPaper;
import com.etalk.crm.pojo.TestPaper;
import com.etalk.crm.pojo.TestPaperQBankRelation;
import com.etalk.crm.pojo.TestPaperType;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Terwer
 */
@Mapper
public interface TestPaperMapper {
    /**
     * 根据试卷标题查询试卷列表
     *
     * @return
     */
    List<TestPaper> selectByTestPaperName(@Param("testPaperName") String testPaperName);

    /**
     * 根据id查询试卷
     *
     * @return
     */
    TestPaper selectByTestPaperId(@Param("testPaperId") Integer testPaperId);

    /**
     * 查询试卷类型下列数据
     *
     * @return
     */
    List<TestPaperType> selectPaperTypeList();

    /**
     * 保存试卷信息
     *
     * @param testPaper
     * @return
     */
    int saveTestPaper(TestPaper testPaper);

    /**
     * 修改试卷信息
     *
     * @param testPaper
     * @return
     */
    int updateTestPaper(TestPaper testPaper);

    /**
     * 插入试题关联信息
     *
     * @return
     */
    int batchSavePaperQBankRelation(List<TestPaperQBankRelation> testPaperQBankRelationList);

    /**
     * 查询试卷中的大题标题
     *
     * @param testPaperId 试卷ID
     * @return
     */
    List<Map> selectQuestionTitleList(@Param("testPaperId") Integer testPaperId);

    /**
     * 查询大题下面的所有子题目的信息
     *
     * @param testPaperId   试卷ID
     * @param qbankParentid 大题题目ID
     * @return
     */
    List<QuestionBanks> selectQuestionBanksList(@Param("testPaperId") Integer testPaperId, @Param("qbankParentid") Integer qbankParentid);

    /**
     * 删除试卷
     *
     * @param testPaperId
     * @return
     */
    int deleteTestPaperByTestPaperId(@Param("testPaperId") Integer testPaperId);

    /**
     * 删除分数关联
     *
     * @param testPaperId
     * @return
     */
    int deletePaperScore(@Param("testPaperId") Integer testPaperId);

    /**
     * 删除做题记录关联
     *
     * @param testPaperId
     * @return
     */
    int deleteInputAnswer(@Param("testPaperId") Integer testPaperId);

    /**
     * 删除题目关联
     *
     * @param testPaperId
     * @return
     */
    int deleteQBankRelation(@Param("testPaperId") Integer testPaperId);

    /**
     * 获取学生以及对应的试卷信息
     *
     * @return
     */
    List<StudentPaper> selectStudentAndPaper(@Param("search") String search);

    /**
     * 检测用户本试卷是否可以测试
     *
     * @return
     */
    Integer checkTestState(@Param("paperId") Integer paperId, @Param("personId") Integer personId);

    /**
     * 发送试卷给学生
     *
     * @param paramMap
     */
    void saveTestPaperScores(Map paramMap);

    /**
     * 更新该学生对本试卷的总分数(这种方式返回主键必须返回void，从Map获取ID)
     *
     * @param scoreId
     * @param score
     * @return
     */
    Integer changeCountPaperScore(@Param("scoreId") Integer scoreId, @Param("score") Integer score);

    /**
     * 修改试卷状态
     *
     * @param scoreId
     * @param state
     * @return
     */
    Integer changePaperState(@Param("scoreId") Integer scoreId, @Param("state") Integer state);

    /**
     * 批量插入做题记录
     *
     * @param questionInputAnswerList
     * @return
     */
    Integer batchSaveInputAnswer(List<QuestionInputAnswer> questionInputAnswerList);

    /**
     * 查询试卷状态，用户本试卷是否可以再测试一次：0未做，可以，1不可以，2完成，可以再做
     *
     * @param scoreId
     * @return
     */
    Integer selectPaperStateByScoreId(Integer scoreId);

    /**
     * 修改当前正在做的题目的分数
     *
     * @param scoreId
     * @param qbankId
     * @return
     */
    Integer updateQuestionScore(@Param("scoreId") Integer scoreId, @Param("qbankId") Integer qbankId, @Param("score") Integer score);
}
