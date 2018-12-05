package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.MobileQuestionMapper;
import com.etalk.crm.pojo.MobileQuestion;
import com.etalk.crm.pojo.QuestionInfo;
import com.etalk.crm.pojo.QuestionResultRecord;
import com.etalk.crm.service.MobileQuestionService;
import com.etalk.crm.service.QuestionService;
import javafx.collections.transformation.SortedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/6 15:51
 * @Version 1.0
 * @Description 移动端试题
 **/
@Service
public class MobileQuestionServiceImpl implements MobileQuestionService {
    private static final Logger logger = LogManager.getLogger(MobileQuestionServiceImpl.class);

    @Resource
    private MobileQuestionMapper mobileQuestionMapper;
    @Resource
    private QuestionService questionService;

    @Override
    public List<Map> getLearingLessonList(Integer personId) {
        return mobileQuestionMapper.getLearingLessonListByPersonId(personId);
    }

    @Override
    public Map getQuestionInfo(Integer personId, Integer lessonId, Integer knowledgeId, Integer unitId) {
        Map resultMap = null;
        List<QuestionInfo> levelInfoList = mobileQuestionMapper.getQuestionInfo(personId, lessonId, knowledgeId);
        if (!CollectionUtils.isEmpty(levelInfoList)) {
            // 按照level级别从小到大排序
            Collections.sort(levelInfoList, new Comparator<QuestionInfo>() {
                @Override
                public int compare(QuestionInfo o1, QuestionInfo o2) {
                    return o1.getQlevel().compareTo(o2.getQlevel());
                }
            });
            // 获取最大的版本号
            QuestionInfo maxQuestionInfo = Collections.max(levelInfoList, new Comparator<QuestionInfo>() {
                @Override
                public int compare(QuestionInfo o1, QuestionInfo o2) {
                    return o1.getVersion().compareTo(o2.getVersion());
                }
            });
            resultMap = new HashMap();
            resultMap.put("version", maxQuestionInfo.getVersion());
            resultMap.put("info", levelInfoList);
        }
        return resultMap;
    }

    @Override
    public MobileQuestion getCurrentQuestion(Integer personId, Integer lessonId, Integer knowledgeId, Integer unitId, Integer level, Integer version) {
        MobileQuestion mobileQuestion = null;
        // 当前级别本次没做过的题目flag=1
        List<MobileQuestion> undoMobileQuestionList = mobileQuestionMapper.getCurrentQuestionList(personId, lessonId, knowledgeId, level, version, 1);
        logger.info("当前级别本次没做过的题目flag=1，数目：" + undoMobileQuestionList.size());

        // 当前级别做过的错题flag=2
        List<MobileQuestion> errorQuestionList = mobileQuestionMapper.getCurrentQuestionList(personId, lessonId, knowledgeId, level, version, 2);
        logger.info("当前级别做过的错题flag=2，数目：" + errorQuestionList.size());

        // 优先取错题
        logger.info("错题筛选之前，数目：" + undoMobileQuestionList.size());
        List<MobileQuestion> currentErrorQuestionList = new ArrayList<>();
        for (int i = 0; i < errorQuestionList.size(); i++) {
            MobileQuestion item = errorQuestionList.get(i);
            if (undoMobileQuestionList.contains(item)) {
                currentErrorQuestionList.add(item);
                undoMobileQuestionList.remove(item);
            }
        }

        logger.info("本次未做的题目中以前做错的题目数目：" + currentErrorQuestionList.size());
        logger.info("本次未做的题目中错题筛选之后的数目：" + undoMobileQuestionList.size());

        // 随机取题目
        logger.info("随机取一道题目");
        // 先从之前做过的错题在本次出现的题目中取
        if (!CollectionUtils.isEmpty(currentErrorQuestionList)) {
            int cIdx = (int) (Math.random() * currentErrorQuestionList.size());
            mobileQuestion = currentErrorQuestionList.get(cIdx);
        } else {// 没有错题，取当前未做过的
            if (!CollectionUtils.isEmpty(undoMobileQuestionList)) {
                int uIdx = (int) (Math.random() * undoMobileQuestionList.size());
                mobileQuestion = undoMobileQuestionList.get(uIdx);
            }
        }

        // 设置题目
        if (null != mobileQuestion) {
            mobileQuestion.setQuestionBanks(questionService.selectByQuestionId(mobileQuestion.getQrId()));
        }
        return mobileQuestion;
    }

    @Transactional
    @Override
    public boolean submitAnswer(Integer personId, Integer lessonId, Integer qknowledgeId, Integer unitId, Integer qrId, String qrResult, Integer isTrue, Integer version) {
        QuestionResultRecord questionResultRecord = new QuestionResultRecord();
        questionResultRecord.setPersonId(personId);
        questionResultRecord.setLessonId(lessonId);
        questionResultRecord.setQknowledgeId(qknowledgeId);
        questionResultRecord.setUnitId(unitId);
        questionResultRecord.setQbankId(qrId);
        questionResultRecord.setQuestionResult(qrResult);
        questionResultRecord.setIsTrue(isTrue);
        questionResultRecord.setVersion(version);

        int count = 0;
        if (checkQuestionDone(questionResultRecord)) {
            logger.info("更新做题记录");
            count = mobileQuestionMapper.updateQuestionRecord(questionResultRecord);
        } else { // 第一次需插入
            logger.info("插入做题记录");
            count = mobileQuestionMapper.addQuestionRecord(questionResultRecord);
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    private boolean checkQuestionDone(QuestionResultRecord questionResultRecord) {
        int count = mobileQuestionMapper.checkQuestionDone(questionResultRecord);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
