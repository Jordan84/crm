package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.dao.QuestionMapper;
import com.etalk.crm.dao.TextBooksMapper;
import com.etalk.crm.pojo.KnowledgePointMistakes;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionBanksImg;
import com.etalk.crm.pojo.QuestionDifficultyDegree;
import com.etalk.crm.pojo.QuestionExplain;
import com.etalk.crm.pojo.QuestionGrade;
import com.etalk.crm.pojo.QuestionInputAnswer;
import com.etalk.crm.pojo.QuestionKnowledge;
import com.etalk.crm.pojo.QuestionMode;
import com.etalk.crm.pojo.QuestionOptionAnswer;
import com.etalk.crm.pojo.QuestionRightAnswer;
import com.etalk.crm.pojo.QuestionTag;
import com.etalk.crm.pojo.QuestionTerm;
import com.etalk.crm.pojo.QuestionTypes;
import com.etalk.crm.pojo.Textbooks;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.service.TextbooksService;
import com.etalk.crm.utils.ExcelImportUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Terwer
 * @since 2018/05/23
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    private static final Logger logger = LogManager.getLogger(QuestionServiceImpl.class);

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private TextBooksMapper textBooksMapper;
    @Resource
    private TextbooksService textbooksService;

    @Override
    public PageInfo<QuestionBanks> selectByTitleAndPage(String title, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBanks> list = questionMapper.selectByTitle(title);
        // 分页信息
        PageInfo<QuestionBanks> pageInfo = new PageInfo<QuestionBanks>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize
                + "，搜索关键字：title=" + title);
        return pageInfo;
    }

    @Override
    public PageInfo<QuestionBanks> selectByGradeAndKnowledge(Integer qgradeId, Integer qknowledgeId, Integer pageNum,
                                                             Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBanks> list = questionMapper.selectByGradeAndKnowledge(qgradeId, qknowledgeId);
        // 分页信息
        PageInfo<QuestionBanks> pageInfo = new PageInfo<QuestionBanks>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：qgradeId=" + qgradeId + "，qknowledgeId=" + qknowledgeId);
        return pageInfo;
    }

    @Override
    public QuestionBanks selectByQuestionId(Integer questionId) {
        // 当前题目
        QuestionBanks questionBanks = questionMapper.selectByQuestionId(questionId);
        // 设置属性及子题目
        QuestionBanks detailedQuestionBanks = initQuestionBanks(questionBanks, 0);
        return detailedQuestionBanks;
    }

    /**
     * 递归设置子题目
     *
     * @param questionBanks
     * @return
     */
    public QuestionBanks initQuestionBanks(QuestionBanks questionBanks, Integer scoreId) {
        if (null == questionBanks) {
            return questionBanks;
        }
        // 图片
        List<QuestionBanksImg> questionBanksImgs = questionMapper
                .selectQuestionBanksImgByQuestionId(questionBanks.getId());
        questionBanks.setListQuestionBanksImg(questionBanksImgs);
        // 正确答案
        List<QuestionRightAnswer> questionRightAnswerList = questionMapper
                .selectQuestionRightAnswerByQuestionId(questionBanks.getId());
        questionBanks.setListRightAnswer(questionRightAnswerList);
        // 选项
        List<QuestionOptionAnswer> questionOptionAnswerList = questionMapper
                .selectQuestionOptionAnswerByQuestionId(questionBanks.getId());
        questionBanks.setListOptionAnswer(questionOptionAnswerList);
        //学生填写的答案
        if (scoreId > 0) {
            List<QuestionInputAnswer> questionInputAnswerList = questionMapper.selectInputAnswerByScoreIdAndQbankId(scoreId, questionBanks.getId());
            questionBanks.setListInputAnswer(questionInputAnswerList);
        }
        // 标签
        List<QuestionTag> questionTagList = questionMapper.selectQuestionTagByQuestionId(questionBanks.getId());
        questionBanks.setListQuestionTag(questionTagList);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questionTagList.size(); i++) {
            QuestionTag tag = questionTagList.get(i);
            if (i == questionTagList.size() - 1) {
                sb.append(tag.getTagName());

            } else {
                sb.append(tag.getTagName() + ",");
            }
        }
        questionBanks.setQuestionTags(sb.toString());
        //解析
        List<QuestionExplain> questionExplainList = questionMapper.selectQuestionExplainByQuestionId(questionBanks.getId());
        if (questionExplainList.size() > 0) {
            questionBanks.setQuestionExplain(questionExplainList.get(0));
        }

        // 子题目（不包括图片选项答案）
        logger.info("父题目ID为：" + questionBanks.getId());
        List<QuestionBanks> subQuestions = questionMapper.selectChild(questionBanks.getId());
        // 子题目（添加图片选项答案）
        List<QuestionBanks> sub = null;
        // 如果有子题
        if (!CollectionUtils.isEmpty(subQuestions)) {
            sub = new ArrayList<>();
            logger.info("题目ID为：" + questionBanks.getId() + "的子题目个数：" + subQuestions.size());
            for (QuestionBanks item : subQuestions) {
                QuestionBanks subItem = initQuestionBanks(item, scoreId);
                sub.add(subItem);
            }
        }
        questionBanks.setListQuestionBanks(sub);
        return questionBanks;
    }

    @Override
    public List<QuestionGrade> selectQGradeList() {
        return questionMapper.selectQGradeList();
    }

    @Override
    public List<QuestionTypes> selectQTypesList() {
        return questionMapper.selectQTypesList();
    }

    @Override
    public List<QuestionMode> selectQModeList() {
        return questionMapper.selectQModeList();
    }

    @Override
    public List<QuestionDifficultyDegree> selectQDifficultyDegreeList() {
        return questionMapper.selectQDifficultyDegreeList();
    }

    @Transactional
    @Override
    public boolean deleteByQuestionId(Integer questionId) {
        int count = questionMapper.deleteByQuestionId(questionId);
        if (count > 0) {
            questionMapper.deleteQuestionBanksImg(questionId);
            questionMapper.deleteQuestionOptionAnswer(questionId);
            questionMapper.deleteQuestionRightAnswer(questionId);
            questionMapper.deleteByQuestionId(questionId);
            // 删除知识点关联
            questionMapper.deleteRelatedKnowledgeByQuestionId(questionId);
            // 删除小题
            questionMapper.deleteSubQuestionsByQuestionId(questionId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean saveQuestionBank(QuestionBanks questionBanks) {
        int count = questionMapper.saveQuestionBank(questionBanks);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public Integer batchSaveQuestion(List<QuestionBanks> questionBanksList, String recorder) {

        List<Integer> qids = new ArrayList<>();
        if (questionBanksList.get(0).getId() != null && questionBanksList.get(0).getId() > 0) {
            qids = questionMapper.selectIdsByParentId(questionBanksList.get(0).getId());
        }

        // 批量操作，id>0为修改，否则为新增
        for (QuestionBanks questionBanks : questionBanksList) {
            Integer questionId = questionBanks.getId();

            //设置错词本最后一级分类
            List<Integer> childMistakeIdList = questionBanks.getChildMistakeId();
            if (!CollectionUtils.isEmpty(childMistakeIdList)) {
                //最大的必然是最后一级的
                questionBanks.setMistakeId(Collections.max(childMistakeIdList));
                logger.debug("最后一级的错词本ID：questionBanks.getMistakeId()=" + questionBanks.getMistakeId());
            }

            // 不是第一个的话，设置父ID
            if (questionBanksList.get(0).getId() == null || questionBanksList.get(0).getId() == 0) {
                // 默认第一级题目的parentid为0
                questionBanks.setParentid(0);
            } else {
                logger.info("当前不是第一个");
                if (questionId != questionBanksList.get(0).getId()) {
                    questionBanks.setParentid(questionBanksList.get(0).getId());
                    logger.info("获取父ID：ID=" + questionBanksList.get(0).getId());

                    if (questionId != null && questionId > 0 && questionBanksList.get(0).getInputType() == QuestionBanks.INPIY_TYPE_SUB) {
                        if (qids.contains(questionId)) {
                            //排除已经有的
                            logger.info("排除已经有的：questionId=" + questionId);
                            qids.remove(questionId);
                        } else {
                            logger.info("需要添加：questionId=" + questionId);
                        }
                    }
                }
            }

            if (questionId != null && questionId > 0) {
                logger.info("修改题目：ID=" + questionId);
                questionMapper.updateQuestionBank(questionBanks);
            } else {
                logger.info("添加题目");
                questionMapper.saveQuestionBank(questionBanks);
                // 获取刚刚插入的题目ID
                questionId = questionBanks.getId();
                logger.info("刚刚插入成功新题目：ID=" + questionId);
            }

            //知识点处理（主题目才有）
            if (null != questionBanks.getRelatedKnowledge()) {
                //先删除所有的关联，然后再添加
                //删除所有的关联
                questionMapper.deleteRelatedKnowledgeByQuestionId(questionId);
                //再添加
                List<Map> knowledgeList = new ArrayList<>();
                List<Integer> knowledgeIds = questionBanks.getRelatedKnowledge();
                if (knowledgeIds.size() > 0) {
                    for (Integer knowledgeId : knowledgeIds) {
                        Map questionMap = new HashMap();
                        questionMap.put("qbankId", questionId);
                        questionMap.put("qknowledgeId", knowledgeId);
                        questionMap.put("recorder", recorder);
                        knowledgeList.add(questionMap);
                    }
                    questionMapper.batchSaveQuestionBanksKnowledge(knowledgeList);
                }
            }

            // 图片处理
            questionBanks.initQuestionImgs();
            questionMapper.deleteQuestionBanksImg(questionId);
            if (!CollectionUtils.isEmpty(questionBanks.getListQuestionBanksImg())) {
                questionMapper.batchSaveQuestionImg(questionBanks.getListQuestionBanksImg());
            }

            // 选项处理
            // 初始化选项及正确答案
            questionBanks.initListOptionAnswer();
            questionMapper.deleteQuestionOptionAnswer(questionId);
            if (!CollectionUtils.isEmpty(questionBanks.getListOptionAnswer())) {
                questionMapper.batchSaveQuestionOptionAnswer(questionBanks.getListOptionAnswer());
            }

            // 答案处理
            questionMapper.deleteQuestionRightAnswer(questionId);
            if (!CollectionUtils.isEmpty(questionBanks.getListRightAnswer())) {
                questionMapper.batchSaveQuestionRightAnswer(questionBanks.getListRightAnswer());
            }

            //标签处理
            questionBanks.initListQuestionTag();
            questionMapper.deleteQuestionTag(questionId);
            if (!CollectionUtils.isEmpty(questionBanks.getListQuestionTag())) {
                questionMapper.batchSaveQuestionTag(questionBanks.getListQuestionTag());
            }

            //解析处理
            questionMapper.deleteQuestionExplain(questionId);
            if (null != questionBanks.getQuestionExplain()) {
                List<QuestionExplain> questionExplainList = new ArrayList<>();
                questionBanks.getQuestionExplain().setQbankId(questionId);
                questionExplainList.add(questionBanks.getQuestionExplain());
                questionMapper.batchSaveQuestionExplain(questionExplainList);
            }
        }

        //剩下的就是之前选择了修改的时候删掉的
        for (Integer qid : qids) {
            //添加的时候选择了，修改的时候未勾选，需要删除
            logger.info("添加的时候选择了，修改的时候未勾选，需要删除：qid=" + qid);
            questionMapper.deleteByQuestionId(qid);
        }

        return questionBanksList.get(questionBanksList.size() - 1).getId();
    }

    @Transactional
    @Override
    public boolean updateQuestionBank(QuestionBanks questionBanks) {
        int count = questionMapper.updateQuestionBank(questionBanks);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionBanksImg> selectQuestionBanksImgByQuestionId(Integer qbankId) {
        List<QuestionBanksImg> questionBanksImgList = new ArrayList<>();
        try {
            questionBanksImgList = questionMapper.selectQuestionBanksImgByQuestionId(qbankId);
        } catch (Exception e) {
            logger.error("系统异常：" + e.getMessage());
        }
        return questionBanksImgList;
    }

    @Transactional
    @Override
    public boolean deleteQuestionBanksImg(Integer qbankId) {
        questionMapper.deleteQuestionBanksImg(qbankId);
        return true;
    }

    @Transactional
    @Override
    public boolean saveQuestionImg(QuestionBanksImg questionBanksImg) {
        int count = questionMapper.saveQuestionImg(questionBanksImg);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean batchSaveQuestionImg(List<QuestionBanksImg> questionBanksImgList) {
        int count = questionMapper.batchSaveQuestionImg(questionBanksImgList);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionOptionAnswer> selectQuestionOptionAnswerByQuestionId(Integer qbankId) {
        List<QuestionOptionAnswer> questionOptionAnswerList = new ArrayList<>();
        try {
            questionOptionAnswerList = questionMapper.selectQuestionOptionAnswerByQuestionId(qbankId);
        } catch (Exception e) {
            logger.error("系统异常" + e.getMessage());
        }
        return questionOptionAnswerList;
    }

    @Transactional
    @Override
    public boolean deleteQuestionOptionAnswer(Integer qbankId) {
        questionMapper.deleteQuestionOptionAnswer(qbankId);
        return true;
    }

    @Transactional
    @Override
    public boolean saveQuestionOptionAnswer(QuestionOptionAnswer questionOptionAnswer) {
        int count = questionMapper.saveQuestionOptionAnswer(questionOptionAnswer);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean batchSaveQuestionOptionAnswer(List<QuestionOptionAnswer> questionOptionAnswerList) {
        int count = questionMapper.batchSaveQuestionOptionAnswer(questionOptionAnswerList);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionRightAnswer> selectQuestionRightAnswerByQuestionId(Integer qbankId) {
        List<QuestionRightAnswer> questionRightAnswerList = new ArrayList<>();
        try {
            questionRightAnswerList = questionMapper.selectQuestionRightAnswerByQuestionId(qbankId);
        } catch (Exception e) {
            logger.error("系统异常" + e.getMessage());
        }
        return questionRightAnswerList;
    }

    @Transactional
    @Override
    public boolean deleteQuestionRightAnswer(Integer qbankId) {
        questionMapper.deleteQuestionRightAnswer(qbankId);
        return true;
    }

    @Transactional
    @Override
    public boolean saveQuestionRightAnswer(QuestionRightAnswer questionRightAnswer) {
        int count = questionMapper.saveQuestionRightAnswer(questionRightAnswer);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean batchSaveQuestionRightAnswer(List<QuestionRightAnswer> questionRightAnswerList) {
        int count = questionMapper.batchSaveQuestionRightAnswer(questionRightAnswerList);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> selectQuestionsByIds(String questionIds) {
        return questionMapper.selectQuestionsByIds(questionIds);
    }

    @Override
    public List<KnowledgePointMistakes> selectKnowledgePointMistakes() {
        List<KnowledgePointMistakes> resultList = questionMapper.selectKnowlwdgePointMistakes();

        List<KnowledgePointMistakes> detailedList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultList)) {
            for (KnowledgePointMistakes item : resultList) {
                if (null != item) {
                    KnowledgePointMistakes detailedItem = item;
                    detailedItem.setListChildKnowledgePointMistakes(questionMapper.selectChildKnowledgePointMistakes(item.getId()));
                    detailedList.add(detailedItem);
                }
            }
        }
        return detailedList;
    }

    @Override
    public List<KnowledgePointMistakes> selectChildKnowledgePointMistakes(Integer parentId) {
        return questionMapper.selectChildKnowledgePointMistakes(parentId);
    }

    @Override
    public Stack<Integer> getFirstKnnowledgePointMistakesId(Integer id, Stack<Integer> selectedIdsStack) {
        if (null == selectedIdsStack) {
            selectedIdsStack = new Stack<>();
        }
        Map<String, Integer> resultMap = questionMapper.selectParentKnowledgePointMistakes(id);
        Integer parentId = resultMap.get("parentId");
        Integer cid = resultMap.get("id");
        selectedIdsStack.push(cid);
        if (parentId == 0) {
            return selectedIdsStack;
        } else {
            return getFirstKnnowledgePointMistakesId(parentId, selectedIdsStack);
        }
    }

    @Override
    public List<QuestionTerm> selectQuestionTermByGradeId(Integer qgradeId) {
        List<QuestionTerm> questionTermList = new ArrayList<>();
        try {
            questionTermList = questionMapper.selectQuestionTermByGradeId(qgradeId);
        } catch (Exception e) {
            logger.error("系统异常" + e.getMessage());
        }
        return questionTermList;
    }

    @Override
    public List<QuestionTerm> selectQuestionTermByParentTermId(Integer parentTermId) {
        List<QuestionTerm> questionTermList = new ArrayList<>();
        try {
            questionTermList = questionMapper.selectQuestionTermByParentId(parentTermId);
        } catch (Exception e) {
            logger.error("系统异常" + e.getMessage());
        }
        return questionTermList;
    }

    @Override
    public PageInfo selectQKnowledgeList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionKnowledge> list = questionMapper.selectQKnowledgeList(search);
        // 分页信息
        PageInfo<QuestionKnowledge> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize
                + "，搜索关键字：search=" + search);
        return pageInfo;
    }

    @Override
    public QuestionKnowledge selectQKnowledgeById(Integer knowledgeId) {
        return questionMapper.selectQKnowledgeById(knowledgeId);
    }

    @Transactional
    @Override
    public boolean saveQKnowledge(QuestionKnowledge knowledge, String recorder) {
        int count = questionMapper.saveQKnowledge(knowledge);
        if (count > 0) {
            //添加知识点题目关联
            List<Map> questionList = new ArrayList<>();
            List<Integer> qbankIds = knowledge.getRelateQuestion();
            if (!CollectionUtils.isEmpty(qbankIds)) {
                for (Integer qbankId : qbankIds) {
                    Map questionMap = new HashMap();
                    questionMap.put("qbankId", qbankId);
                    questionMap.put("qknowledgeId", knowledge.getId());
                    questionMap.put("recorder", recorder);
                    questionList.add(questionMap);
                }
                questionMapper.batchSaveQuestionBanksKnowledge(questionList);
            }

            //添加知识点教材页码关联
            List<Map> textbooksList = new ArrayList<>();
            List<Integer> pageIds = knowledge.getPage();
            List<String> pageNames = knowledge.getPageName();
            if (!CollectionUtils.isEmpty(pageIds)) {
                for (int i = 0; i < pageIds.size(); i++) {
                    Integer pid = pageIds.get(i);
                    String pname = pageNames.get(i);
                    Map textbooksMap = new HashMap();
                    textbooksMap.put("qknowledgeId", knowledge.getId());
                    textbooksMap.put("textbooksId", knowledge.getRelatedTextbooks());
                    textbooksMap.put("textbooksPage", pid);
                    textbooksMap.put("textbooksPageName", pname);
                    textbooksMap.put("recorder", recorder);
                    textbooksList.add(textbooksMap);
                }
                textBooksMapper.batchSaveTextbooksKnowledge(textbooksList);
            }

            //添加知识点错词本关联
            if (!StringUtils.isEmpty(knowledge.getMistakeString())) {
                List<Map> mistakeList = new ArrayList<>();
                List<String> mids = Arrays.asList(knowledge.getMistakeString().split(","));
                if (!CollectionUtils.isEmpty(mids)) {
                    for (int i = 0; i < mids.size(); i++) {
                        Map mistakeMap = new HashMap();
                        mistakeMap.put("qknowledgeId", knowledge.getId());
                        mistakeMap.put("mistakeId", mids.get(i));
                        mistakeList.add(mistakeMap);
                    }
                    questionMapper.batchSaveQuestionKnowledgeMistake(mistakeList);
                }
            }
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean editQKnowledge(QuestionKnowledge knowledge, String recorder) {
        int count = questionMapper.editQKnowledge(knowledge);
        if (count > 0) {
            //修改知识点题目关联
            //先删除所有的关联
            questionMapper.deleteRelatedQuestionByKnowledgeId(knowledge.getId());
            //再添加
            List<Map> questionList = new ArrayList<>();
            List<Integer> qbankIds = knowledge.getRelateQuestion();
            if (!CollectionUtils.isEmpty(qbankIds)) {
                for (int i = 0; i < qbankIds.size(); i++) {
                    Integer qbankId = qbankIds.get(i);
                    Map questionMap = new HashMap();
                    questionMap.put("qbankId", qbankId);
                    questionMap.put("qknowledgeId", knowledge.getId());
                    questionMap.put("recorder", recorder);
                    questionList.add(questionMap);
                }
                questionMapper.batchSaveQuestionBanksKnowledge(questionList);
            }

            //修改知识点教材页码关联
            textBooksMapper.deleteRelatedTextbooksByKnowledgeId(knowledge.getId());
            List<Map> textbooksList = new ArrayList<>();
            List<Integer> pageIds = knowledge.getPage();
            List<String> pageNames = knowledge.getPageName();
            if (!CollectionUtils.isEmpty(pageIds)) {
                for (int i = 0; i < pageIds.size(); i++) {
                    Integer pid = pageIds.get(i);
                    String pname = pageNames.get(i);
                    Map textbooksMap = new HashMap();
                    textbooksMap.put("qknowledgeId", knowledge.getId());
                    textbooksMap.put("textbooksId", knowledge.getRelatedTextbooks());
                    textbooksMap.put("textbooksPage", pid);
                    textbooksMap.put("textbooksPageName", pname);
                    textbooksMap.put("recorder", recorder);
                    textbooksList.add(textbooksMap);
                }
                textBooksMapper.batchSaveTextbooksKnowledge(textbooksList);
            }

            //修改知识点错词本关联
            questionMapper.deleteRelatedMistakeByKnowledgeId(knowledge.getId());
            if (!StringUtils.isEmpty(knowledge.getMistakeString())) {
                List<Map> mistakeList = new ArrayList<>();
                List<String> mids = Arrays.asList(knowledge.getMistakeString().split(","));
                if (!CollectionUtils.isEmpty(mids)) {
                    for (int i = 0; i < mids.size(); i++) {
                        Map mistakeMap = new HashMap();
                        mistakeMap.put("qknowledgeId", knowledge.getId());
                        mistakeMap.put("mistakeId", mids.get(i));
                        mistakeList.add(mistakeMap);
                    }
                    questionMapper.batchSaveQuestionKnowledgeMistake(mistakeList);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkKnowledgeExist(String cnName) {
        int count = questionMapper.checkKnowledgeExist(cnName);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkOtherKnowledgeExist(String oldCnName, String newCnName) {
        int count = questionMapper.checkOtherKnowledgeExist(oldCnName, newCnName);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Override
    public PageInfo<QuestionBanks> getQuestionListByKnowledgeId(Integer knowledgeId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBanks> list = questionMapper.getQuestionListByKnowledgeId(knowledgeId);
        // 分页信息
        PageInfo<QuestionBanks> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo<QuestionBanks> getQuestionListByQuestionIdList(List<String> questionIdList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBanks> list = questionMapper.getQuestionListByQuestionIdList(questionIdList);
        // 分页信息
        PageInfo<QuestionBanks> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo getKnowledgeListByQuestionId(Integer qbankId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionKnowledge> list = questionMapper.getKnowledgeListByQuestionId(qbankId);
        // 分页信息
        PageInfo<QuestionKnowledge> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo getKnowledgeListByKnowledgeIdList(List<String> knowledgeList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionKnowledge> list = questionMapper.getKnowledgeListByKnowledgeIdList(knowledgeList);
        // 分页信息
        PageInfo<QuestionKnowledge> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public List<QuestionKnowledge> getKnowledgeListByMistakeId(Integer mistakeId) {
        return questionMapper.getKnowledgeListByMistakeId(mistakeId);
    }

    @Transactional
    @Override
    public Map batchImport(String fileName, MultipartFile mfile) {
        Map resultMap = new HashMap();
        List<Map> dataList = new ArrayList<>();

        InputStream is = null;
        try {
            is = mfile.getInputStream();

            Workbook workbook = null;
            //根据版本选择创建Workbook的方式
            //根据文件名判断文件是2003版本还是2007版本
            Sheet sheet = null;
            if (ExcelImportUtils.isExcel2007(fileName)) {
                // 针对2007 Excel 文件
                workbook = new XSSFWorkbook(is);
                sheet = workbook.getSheetAt(0);
            } else {
                // 针对 2003 Excel 文件
                workbook = new HSSFWorkbook(new POIFSFileSystem(is));
                sheet = workbook.getSheetAt(0);
            }
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 0; j < physicalNumberOfRows; j++) {
                if (j == 0 || j == 1) {
                    continue;//标题行
                }
                Row row = sheet.getRow(j);

                // 第3列是知识点名称
                if (row.getCell(2) != null && row.getCell(3) != null && row.getCell(4) != null) {
                    row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                    Cell cell2 = row.getCell(3);
                    String pages = cell2.getStringCellValue();

                    row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                    Cell cell = row.getCell(2);
                    String knowledgeName = cell.getStringCellValue();
                    if (null != knowledgeName && !StringUtils.isEmpty(knowledgeName.trim())) {
                        // 插入知识点
                        Integer qkId = 0;
                        if (checkKnowledgeExist(knowledgeName)) {
                            QuestionKnowledge questionKnowledge = new QuestionKnowledge();
                            questionKnowledge.setCnName(knowledgeName);
                            questionMapper.saveQKnowledge(questionKnowledge);
                            qkId = questionKnowledge.getId();
                        } else {
                            // 存在知识点查出ID
                            qkId = questionMapper.selectIdByKnowledgeName(knowledgeName);
                        }

                        if (null != pages && !StringUtils.isEmpty(pages.trim())) {
                            String[] pageArray = pages.split(",");
                            for (int i = 0; i < pageArray.length; i++) {
                                Map itemMap = new HashMap();

                                // 返回主键ID
                                itemMap.put("qknowledgeId", qkId);

                                row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                                Cell cell3 = row.getCell(4);
                                String textbooksId = cell3.getStringCellValue();
                                if (null != textbooksId && !StringUtils.isEmpty(textbooksId.trim())) {
                                    itemMap.put("textbooksId", textbooksId);
                                }

                                int page = Integer.parseInt(pageArray[i]);
                                String pageName = "p" + page;

                                // 查找页码配置文件
                                Map map = new HashMap();
                                map.put("id", textbooksId);
                                List<Textbooks> list = textBooksMapper.selectTextbooks(map);
                                if (!CollectionUtils.isEmpty(list)) {
                                    String address = list.get(0).getAddress();
                                    if (address.lastIndexOf("/") == address.length() - 1) {
                                        address += "cfg.json";
                                    } else {
                                        address += "/cfg.json";
                                    }
                                    // 查找页码索引
                                    int pageIdx = textbooksService.getTextbooksIdxByPage(address, pageName);

                                    itemMap.put("textbooksPage", pageIdx);
                                    itemMap.put("textbooksPageName", "p" + page);

                                    dataList.add(itemMap);
                                }
                            }
                        }
                    }
                }
            }

            // 批量插入知识点教材页码管理
            textBooksMapper.batchSaveTextbooksKnowledge(dataList);

            logger.debug("data:" + JSON.toJSONString(dataList));
            resultMap.put("status", 1);
            resultMap.put("msg", "导入数据成功！");
        } catch (Exception e) {
            // 异常手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("status", 0);
            resultMap.put("msg", "导入出错！请检查数据格式！" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        resultMap.put("data", dataList);
        return resultMap;
    }
}
