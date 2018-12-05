package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.dao.*;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.QuestionnaireOcService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QuestionnaireOcServiceImpl implements QuestionnaireOcService {
     protected static final Logger logger = LogManager.getLogger(QuestionnaireOcServiceImpl.class);
   @Resource
   private QuestionnaireMapper questionnaireMapper;

   @Resource
   private QuestionnaireQuestionMapper questionnaireQuestionMapper;

    @Resource
    private QuestionaireOptionMapper questionaireOptionMapper;

    @Resource
    private PapersMapper papersMapper;

     @Resource
    private UserAnswerMapper userAnswerMapper;

     @Resource
     private StudentDynamicsMapper studentDynamicsMapper;

     @Resource
     private PersonMapper personMapper;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addQoc(HttpSession session, @RequestBody JSONObject jsonObject) {
        int result = 0;
        QuestionnaireOc qoc = new QuestionnaireOc();
        StudentDynamics studentDynamics = new StudentDynamics();
        Integer personId = null;
        Integer lessonId = null;
        if(!StringUtils.isEmpty(jsonObject.get("personId").toString())){
            personId = Integer.parseInt(jsonObject.get("personId").toString());
            qoc.setPersonId(personId);
            studentDynamics.setPersonId(personId);
        }
        if(questionnaireMapper.selectByPersonId(personId) == 0){
             Integer loginId = (Integer) session.getAttribute("userid");
            //oc课程id
            if(!StringUtils.isEmpty(jsonObject.get("lessonId").toString())){
                lessonId = Integer.parseInt(jsonObject.get("lessonId").toString());
                qoc.setLessonId(lessonId);
            }
            //oc课 上课时间
            String releaseTime = null;
            if(!StringUtils.isEmpty(jsonObject.get("addUserId").toString())){
                Integer addUserId = Integer.parseInt(jsonObject.get("addUserId").toString());
                qoc.setAddUserId(addUserId);
                studentDynamics.setAddUserId(addUserId);
            }
            //oc 课上课老师
            String teacher = "";
            if(!StringUtils.isEmpty(jsonObject.get("teacher").toString())){
                teacher = jsonObject.get("teacher").toString();
                qoc.setTeacher(teacher);
            }
            //学员登录名
            if(!StringUtils.isEmpty(jsonObject.get("studentLogin").toString())){
               String studentLogin = jsonObject.get("studentLogin").toString();
               qoc.setStudentLogin(studentLogin);
                studentDynamics.setStudentLogin(studentLogin);
            }

            //学员中英文名
            if(!StringUtils.isEmpty(jsonObject.get("loginName").toString())){
               String name = jsonObject.get("loginName").toString();
                qoc.setPersonName(name);
            }
            //添加人姓名
            if(!StringUtils.isEmpty(jsonObject.get("addUserName").toString())){
                String addUserName = jsonObject.get("addUserName").toString();
                qoc.setAddUserName(addUserName);
                studentDynamics.setAddUser(addUserName);
            }
            //ssc 登录名
            if(!StringUtils.isEmpty(jsonObject.get("sscName").toString())){
                qoc.setSscName(jsonObject.get("sscName").toString());
            }
            //插入学员动态记录
            studentDynamics.setAddTime(new Date());
            Integer paperId = null;
            if(!StringUtils.isEmpty(jsonObject.get("paperId").toString())){
               paperId = Integer.parseInt(jsonObject.get("paperId").toString());
                qoc.setPaperId(paperId);
            }
            Map map = new HashMap();
            if(!StringUtils.isEmpty(jsonObject.get("releaseTime").toString())){
                releaseTime = jsonObject.get("releaseTime").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    qoc.setReleaseTime(sdf.parse(releaseTime));
                } catch (ParseException e) {
                    logger.error(e.getMessage(),e);
                }
                qoc.setCreateTime(new Date());
                studentDynamics.setContent("完成oc课回访（"+lessonId+"）调查，上课时间："+releaseTime+",老师："+teacher);
            }
            QuestionUserAnswer questionUserAnswer = new QuestionUserAnswer();
            qoc.setState(1);

            List<Map> list = new ArrayList();
            //插入用户操作的答案，方便后期展示
            if(!StringUtils.isEmpty(jsonObject.get("userAnswer").toString())){
                list = (List)(jsonObject.get("userAnswer"));
                //计算平均分
                 if(!StringUtils.isEmpty(jsonObject.get("score").toString())){
                     BigDecimal score = new BigDecimal(String.valueOf(jsonObject.get("score").toString())).divide(new BigDecimal(String.valueOf(list.size())), 2, BigDecimal.ROUND_HALF_UP);
                     qoc.setGrade(score);
                 }

                result =  questionnaireMapper.addQoc(qoc);
                for(int i = 0; i<list.size();i++){
                    QuestionUserAnswer qua = new QuestionUserAnswer();
                    qua.setQuestionId(Integer.parseInt(list.get(i).get("questionId").toString()));
                    qua.setOptionId(Integer.parseInt(list.get(i).get("optionId").toString()));
                    qua.setPersonId(personId);
                    qua.setPaperId(paperId);
                    if(StringUtils.isEmpty(list.get(i).get("optionSummary").toString())){
                        qua.setOptionSummary("");
                    }else{
                         qua.setOptionSummary(list.get(i).get("optionSummary").toString());
                    }
                    userAnswerMapper.addUserAnswer(qua);
                }
            }
            studentDynamics.setQuestionnaireId(qoc.getId());
            studentDynamicsMapper.addStuDynamic(studentDynamics);
        }else{
            result = -1;
        }
        return result;
    }

    /**
     * 添加问卷
     *
     * @param papers
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addPaper(Papers papers) {
        return papersMapper.addPaper(papers);
    }

    /**
     * 查询用户选择的 问卷  答案
     *
     * @param questionId
     * @param personId
     * @return
     */
    @Override
    public QuestionUserAnswer selectByQPId(Integer questionId, Integer personId) {
        return userAnswerMapper.selectByQPId(questionId,personId);
    }

    /**
     * 根据paperId  查询问卷
     *
     * @param paperId
     * @return
     */
    @Override
    public Papers selectPapersByPaperId(Integer paperId) {
        return papersMapper.selectById(paperId);
    }

    @Override
    public List<QuestionnaireQuestion> selectByPaperId(Integer paperId) {
        return questionnaireQuestionMapper.selectByPaperId(paperId);
    }

    /**
     * 根据问卷题目查找选项
     *
     * @return
     */
    @Override
    public List<QuestionaireOption> selectByQuestionId(Integer questionId) {
        return questionaireOptionMapper.selectByQuestionId(questionId);
    }

    /**
     * 添加问题
     *
     * @param questionnaireQuestion
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addQuestion(QuestionnaireQuestion questionnaireQuestion) {
        return questionnaireQuestionMapper.addQuestion(questionnaireQuestion);
    }

    /**
     * 添加问卷选项
     *
     * @param QuestionaireOption
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addOption(QuestionaireOption QuestionaireOption) {
        return questionaireOptionMapper.addOption(QuestionaireOption);
    }


    /**
     * 查询可用的 问卷
     *
     * @return
     */
    @Override
    public Papers selectByState() {
        return papersMapper.selectByState();
    }

    /**
     * 存贮用户问卷答案
     *
     * @param questionUserAnswer
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addUserAnser(QuestionUserAnswer questionUserAnswer) {
        return userAnswerMapper.addUserAnswer(questionUserAnswer);
    }

    /**
     * 添加学生动态
     *
     * @param studentDynamics
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addStuDynamic(StudentDynamics studentDynamics) {
        return studentDynamicsMapper.addStuDynamic(studentDynamics);
    }

    /**
     *查询oc 问卷调查回访记录
     * @param startTime
     * @param endTime
     * @param teacher
     * @return
     */
    @Override
    public PageInfo<QuestionnaireOc> questionnaireOcList(String startTime, String endTime, String teacher,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionnaireOc> list = questionnaireMapper.questionnaireOcList(startTime,endTime,teacher);
        //分页信息
        PageInfo<QuestionnaireOc> pageInfo = new PageInfo<QuestionnaireOc>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    /**
     * 查询所有的 问卷
     *
     * @return
     */
    @Override
    public PageInfo<Papers> paperList(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Papers> list = papersMapper.paperList();
        //分页信息
        PageInfo<Papers> pageInfo = new PageInfo<Papers>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    /**
     * 更改问卷的状态
     *
     * @param paperId
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public int updatePaper(Integer paperId) {
        int result  = papersMapper.updatePaper(paperId);
        int result2 = papersMapper.updatePaper2(paperId);
        return result+result2;
    }

    /**
     * 查询所有的 问卷
     *
     * @return
     */
    @Override
    public List<QuestionnaireOc> SelectquestionnaireAll() {
        return questionnaireMapper.questionnaireOcList(null,null,null);
    }

    /**
     * @param questionnaireId
     * @return
     */
    @Override
    public QuestionnaireOc selectByQCId(Integer questionnaireId) {
        return questionnaireMapper.selectByQCId(questionnaireId);
    }

    @Override
    public List<Person> selectSSCTeacher() {
        return personMapper.selectSSCTeacher();
    }

    /**
     * 根据多个id查询列表
     *
     * @param ids
     * @return
     */
    @Override
    public List<QuestionnaireOc> selectQcByIds(Integer[] ids) {
        return questionnaireMapper.selectQcByIds(ids);
    }

    /**
     * 导出  oc 问卷数据
     *
     * @param startTime
     * @param endTime
     * @param teacher
     * @return
     */
    @Override
    public List<QuestionnaireOc> exportData(String startTime, String endTime, String teacher) {
        return questionnaireMapper.questionnaireOcList(startTime,endTime,teacher);
    }
}
