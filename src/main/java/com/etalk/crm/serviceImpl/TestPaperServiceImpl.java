package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.dao.PersonMapper;
import com.etalk.crm.dao.QuestionMapper;
import com.etalk.crm.dao.TestPaperMapper;
import com.etalk.crm.pojo.PaperInputAnswer;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionItem;
import com.etalk.crm.pojo.StudentPaper;
import com.etalk.crm.pojo.TestPaper;
import com.etalk.crm.pojo.TestPaperQBankRelation;
import com.etalk.crm.pojo.TestPaperType;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.service.TestPaperService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    private static final Logger logger = LogManager.getLogger(MenuInfoServiceImpl.class);
    /**
     * 试卷未做状态0
     */
    private static final int PAPER_STATE_UNDO = 0;
    /**
     * 试卷待批改状态1
     */
    private static final int PAPER_STATE_CORRECT = 1;
    /**
     * 试卷完成状态2
     */
    private static final int PAPER_STATE_FINISH = 2;

    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private PersonMapper personMapper;

    @Override
    public PageInfo<TestPaper> selectByTestPaperNameAndPage(String testPaperName, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TestPaper> list = testPaperMapper.selectByTestPaperName(testPaperName);
        //分页信息
        PageInfo<TestPaper> pageInfo = new PageInfo<TestPaper>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：testPaperName=" + testPaperName);
        return pageInfo;
    }

    @Override
    public TestPaper selectByTestPaperId(Integer testPaperId) {
        //查询不包含正确答案的试卷信息
        TestPaper testPaper = this.selectByTestPaperIdAndScoreId(testPaperId, 0);
        return testPaper;
    }

    @Override
    public TestPaper selectByTestPaperIdAndScoreId(Integer testPaperId, Integer scoreId) {
        //试卷基本信息
        TestPaper testPaper = testPaperMapper.selectByTestPaperId(testPaperId);
        if (null == testPaper) {
            logger.error("查询试卷信息失败：试卷ID=" + testPaperId);
            return null;
        }

        //查询试卷状态
        Integer paperState = testPaperMapper.selectPaperStateByScoreId(scoreId);
        logger.info("查询试卷状态:scoreId=" + scoreId + " paperState=" + paperState);
        testPaper.setState(paperState);

        //题目信息
        //查询大题标题
        List<Map> questionTitleList = testPaperMapper.selectQuestionTitleList(testPaperId);
        //查询小题信息
        List<QuestionItem> listQuestionItems = new ArrayList<>();
        for (Map questionMap : questionTitleList) {
            String questionTitle = (String) questionMap.getOrDefault("title", "");
            Integer questionId = (Integer) questionMap.getOrDefault("id", 0);

            QuestionItem questionItem = new QuestionItem();
            questionItem.setQuestionTitle(questionTitle);
            //查询大题下面的所有子题目的信息
            List<QuestionBanks> listQuestionBanks = testPaperMapper.selectQuestionBanksList(testPaperId, questionId);

            List<QuestionBanks> detailedListQuestionBanks = new ArrayList<>(listQuestionBanks.size());
            //初始化子题目
            for (QuestionBanks questionBanks : listQuestionBanks) {
                //设置属性及子题目
                QuestionBanks detailedQuestionBanks = questionService.initQuestionBanks(questionBanks, scoreId);
                detailedListQuestionBanks.add(detailedQuestionBanks);
            }
            questionItem.setListQuestionBanks(detailedListQuestionBanks);
            listQuestionItems.add(questionItem);
        }
        testPaper.setListQuestionItems(listQuestionItems);
        return testPaper;
    }

    @Override
    public List<TestPaperType> selectPaperTypeList() {
        return testPaperMapper.selectPaperTypeList();
    }

    @Transactional
    @Override
    public boolean saveTestPaper(TestPaper testPaper) {
        if (testPaper.getId() != null && testPaper.getId() > 0) {
            logger.info("开始修改试卷信息：入参=" + JSON.toJSONString(testPaper));
            int count = testPaperMapper.updateTestPaper(testPaper);
            if (count > 0) {
                logger.info("试卷修改成功：testPaperId=" + testPaper.getId());
                return true;
            }
        } else {
            logger.info("开始生成试卷：入参=" + JSON.toJSONString(testPaper));
            int count = testPaperMapper.saveTestPaper(testPaper);
            if (count > 0) {
                logger.debug("试卷ID：" + testPaper.getId());
                List<TestPaperQBankRelation> testPaperQBankRelationList = new ArrayList<>();
                for (QuestionItem questionItem : testPaper.getListQuestionItems()) {
                    //临时解决传空值问题，根本方案是改进vue前端删除逻辑
                    if (null == questionItem.getQuestionTitle()) {
                        continue;
                    }

                    //大题标题
                    QuestionBanks qbank = new QuestionBanks();
                    qbank.setParentid(-1);
                    qbank.setTitle(questionItem.getQuestionTitle());
                    qbank.setQtitle("");
                    qbank.setQgradeId(0);
                    qbank.setMistakeId(0);
                    qbank.setQtypeId(0);
                    questionMapper.saveQuestionBank(qbank);

                    TestPaperQBankRelation testPaperQBankRelation = new TestPaperQBankRelation();
                    testPaperQBankRelation.setTestPaperId(testPaper.getId());
                    testPaperQBankRelation.setQbankId(qbank.getId());
                    testPaperQBankRelation.setQbankParentid(0);
                    testPaperQBankRelationList.add(testPaperQBankRelation);

                    //小题
                    for (QuestionBanks questionBanks : questionItem.getListQuestionBanks()) {
                        testPaperQBankRelation = new TestPaperQBankRelation();
                        testPaperQBankRelation.setTestPaperId(testPaper.getId());
                        testPaperQBankRelation.setQbankId(questionBanks.getId());
                        testPaperQBankRelation.setQbankParentid(qbank.getId());
                        testPaperQBankRelationList.add(testPaperQBankRelation);
                    }
                }

                int result = testPaperMapper.batchSavePaperQBankRelation(testPaperQBankRelationList);
                if (result > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteTestPaper(Integer testPaperId) {
        //删除大题标题
        questionMapper.deleteQuestionTitleByTestPaperId(testPaperId);
        //删除试卷题目关联
        testPaperMapper.deleteQBankRelation(testPaperId);
        //删除试卷信息
        testPaperMapper.deleteTestPaperByTestPaperId(testPaperId);
        //删除分数关联
        testPaperMapper.deletePaperScore(testPaperId);
        //删除做题记录关联
        testPaperMapper.deleteInputAnswer(testPaperId);
        return true;
    }

    @Override
    public PageInfo<Person> getStudentList(String studentName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Person> list = personMapper.selectStudentListByPaperSend(studentName);
        // 分页信息
        PageInfo<Person> pageInfo = new PageInfo<Person>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：studentName=" + studentName);
        return pageInfo;
    }

    @Override
    public PageInfo<StudentPaper> selectStudentAndPaper(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentPaper> list = testPaperMapper.selectStudentAndPaper(search);
        // 分页信息
        PageInfo<StudentPaper> pageInfo = new PageInfo<StudentPaper>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：search=" + search);
        return pageInfo;
    }

    @Override
    public boolean checkTestState(Integer paperId, Integer personId) {
        Integer state = testPaperMapper.checkTestState(paperId, personId);
        //最近一次的试卷未完成，不允许再次做题
        if (null != state && PAPER_STATE_FINISH != state) {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public Integer sendPaperToStudent(Integer paperId, Integer personId) {
        Map paramMap = new HashMap();
        paramMap.put("paperId", paperId);
        paramMap.put("personId", personId);
        testPaperMapper.saveTestPaperScores(paramMap);
        Integer scoreId = (Integer) paramMap.getOrDefault("scoreId", 0);
        return scoreId;
    }

    @Override
    public boolean checkPaperCanHandin(Integer scoreId) {
        //检测试卷状态，只有未做才允许提交
        Integer state = testPaperMapper.selectPaperStateByScoreId(scoreId);
        //最近一次的试卷未完成，不允许再次做题
        if (PAPER_STATE_UNDO != state) {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public boolean handinPaper(PaperInputAnswer inputAnswer) {
        //更新该学生对本试卷的总分数
        testPaperMapper.changeCountPaperScore(inputAnswer.getScoreId(), inputAnswer.getScore());

        //插入做题数据，按照小题保存
        Integer result = testPaperMapper.batchSaveInputAnswer(inputAnswer.getListQuestionInputAnswer());
        if (result > 0) {
            //做题结果保存成功后，修改试卷状态为批改中
            testPaperMapper.changePaperState(inputAnswer.getScoreId(), PAPER_STATE_CORRECT);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean correctPaper(Integer scoreId, Integer qbankId, Integer score) {
        Integer count = testPaperMapper.updateQuestionScore(scoreId, qbankId, score);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
