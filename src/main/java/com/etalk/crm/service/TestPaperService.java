package com.etalk.crm.service;

import com.etalk.crm.pojo.PaperInputAnswer;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.pojo.StudentPaper;
import com.etalk.crm.pojo.TestPaper;
import com.etalk.crm.pojo.TestPaperType;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Terwer
 * @date 2018/06/05
 */
public interface TestPaperService {

    /**
     * 根据试卷标题和页码查询试卷列表
     *
     * @param testPaperName 试卷标题
     * @param pageSize      页码
     * @param pageNum       页数
     * @return
     */
    PageInfo<TestPaper> selectByTestPaperNameAndPage(String testPaperName, Integer pageSize, Integer pageNum);

    /**
     * 根据试卷ID查询试卷信息
     *
     * @param testPaperId 试卷ID
     * @return
     */
    TestPaper selectByTestPaperId(Integer testPaperId) throws Exception;

    /**
     * 根据试卷ID和学生ID查询试卷信息
     *
     * @param testPaperId 试卷ID
     * @param scoreId     学生ID
     * @return
     */
    TestPaper selectByTestPaperIdAndScoreId(Integer testPaperId, Integer scoreId);

    /**
     * 查询试卷类型下列数据
     *
     * @return
     */
    List<TestPaperType> selectPaperTypeList();

    /**
     * 生成一份新试卷
     *
     * @param testPaper
     * @return
     */
    boolean saveTestPaper(TestPaper testPaper);

    /**
     * 删除试卷
     *
     * @param testPaperId 试卷ID
     * @return
     */
    boolean deleteTestPaper(Integer testPaperId);

    /**
     * 获取学生列表
     *
     * @return
     */
    PageInfo<Person> getStudentList(String studentName, Integer pageNum, Integer pageSize);

    /**
     * 获取学生以及对应的试卷信息
     *
     * @return
     */
    PageInfo<StudentPaper> selectStudentAndPaper(String search, Integer pageNum, Integer pageSize);

    /**
     * 检测用户本试卷是否可以测试
     *
     * @return
     */
    boolean checkTestState(Integer paperId, Integer personId);

    /**
     * 发送试卷给学生
     *
     * @param paperId
     * @param personId
     * @return
     */
    Integer sendPaperToStudent(Integer paperId, Integer personId);

    /**
     * 检测试卷是否可以提交
     *
     * @param scoreId
     * @return
     */
    boolean checkPaperCanHandin(Integer scoreId);

    /**
     * 学生提交做题信息给老师
     *
     * @param inputAnswer
     * @return
     */
    boolean handinPaper(PaperInputAnswer inputAnswer);

    /**
     * 修改当前正在做的题目的分数
     *
     * @param scoreId
     * @param qbankId
     * @param score
     * @return
     */
    boolean correctPaper(Integer scoreId, Integer qbankId, Integer score);
}
