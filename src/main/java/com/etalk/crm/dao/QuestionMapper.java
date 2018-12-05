package com.etalk.crm.dao;

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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 */
@Mapper
public interface QuestionMapper {
    /**
     * 根据题目搜索数据
     *
     * @param title 题目标题
     * @return
     */
    List<QuestionBanks> selectByTitle(@Param("title") String title);

    /**
     * 根据题目搜索数据
     *
     * @param qgradeId     年级
     * @param qknowledgeId 知识点
     * @return
     */
    List<QuestionBanks> selectByGradeAndKnowledge(@Param("qgradeId") Integer qgradeId, @Param("qknowledgeId") Integer qknowledgeId);

    /**
     * 题目ID
     *
     * @param questionId 题目ID
     * @return
     */
    QuestionBanks selectByQuestionId(@Param("questionId") Integer questionId);

    /**
     * 查询所有子题目
     *
     * @param parentId
     * @return
     */
    List<QuestionBanks> selectChild(@Param("parentId") Integer parentId);

    /**
     * 年级列表
     *
     * @return
     */
    List<QuestionGrade> selectQGradeList();

    /**
     * 获取知识点列表
     *
     * @param search
     * @return
     */
    List<QuestionKnowledge> selectQKnowledgeList(@Param("search") String search);

    /**
     * 根据ID查询知识点
     *
     * @param knowledgeId
     * @return
     */
    QuestionKnowledge selectQKnowledgeById(Integer knowledgeId);

    /**
     * 添加知识点
     *
     * @param knowledge
     * @return
     */
    int saveQKnowledge(QuestionKnowledge knowledge);

    /**
     * 修改知识点
     *
     * @param knowledge
     * @return
     */
    int editQKnowledge(QuestionKnowledge knowledge);

    /**
     * 检测知识点是否存在
     *
     * @param cnName
     * @return
     */
    int checkKnowledgeExist(String cnName);

    /**
     * 检测知识点是否存在
     *
     * @param oldCnName
     * @param newCnName
     * @return
     */
    int checkOtherKnowledgeExist(@Param("oldCnName") String oldCnName, @Param("newCnName") String newCnName);

    /**
     * 删除某个知识点与题目的关联
     *
     * @param knowledgeId
     * @return
     */
    int deleteRelatedQuestionByKnowledgeId(Integer knowledgeId);

    /**
     * 批量添加知识点与题目的关联
     *
     * @param questionList
     * @return
     */
    int batchSaveQuestionBanksKnowledge(List<Map> questionList);

    /**
     * 查询知识点关联的题目
     *
     * @param knowledgeId
     * @return
     */
    List<QuestionBanks> getQuestionListByKnowledgeId(Integer knowledgeId);

    /**
     * 根据题目ID集合查询知识点关联的题目
     *
     * @param questionList
     * @return
     */
    List<QuestionBanks> getQuestionListByQuestionIdList(@Param("questionList") List<String> questionList);

    /**
     * 题类列表
     *
     * @return
     */
    List<QuestionTypes> selectQTypesList();

    /**
     * 题型列表
     *
     * @return
     */
    List<QuestionMode> selectQModeList();

    /**
     * 难度系数列表
     *
     * @return
     */
    List<QuestionDifficultyDegree> selectQDifficultyDegreeList();

    /**
     * 删除题目
     *
     * @param questionId 题目ID
     * @return
     */
    int deleteByQuestionId(@Param("id") Integer questionId);

    /**
     * 删除子题目
     *
     * @param parentid 父题目ID
     * @return
     */
    int deleteSubQuestionsByQuestionId(@Param("parentid") Integer parentid);

    /**
     * 添加题目
     *
     * @param questionBanks
     * @return
     */
    int saveQuestionBank(QuestionBanks questionBanks);

    /**
     * 修改题目
     *
     * @param questionBanks
     * @return
     */
    int updateQuestionBank(QuestionBanks questionBanks);

    /**
     * 查询某个题目对应的所有图片
     *
     * @param qbankId
     * @return
     */
    List<QuestionBanksImg> selectQuestionBanksImgByQuestionId(@Param("qbankId") Integer qbankId);

    /**
     * 删除图片
     *
     * @param qbankId
     * @return
     */
    int deleteQuestionBanksImg(@Param("qbankId") Integer qbankId);

    /**
     * 保存图片
     *
     * @param questionBanksImg
     * @return
     */
    int saveQuestionImg(QuestionBanksImg questionBanksImg);

    /**
     * 批量保存图片
     *
     * @param questionBanksImgList
     * @return
     */
    int batchSaveQuestionImg(List<QuestionBanksImg> questionBanksImgList);

    /**
     * 查询某个题目对应的所有选项
     *
     * @param qbankId
     * @return
     */
    List<QuestionOptionAnswer> selectQuestionOptionAnswerByQuestionId(@Param("qbankId") Integer qbankId);

    /**
     * 删除选项
     *
     * @param qbankId
     * @return
     */
    int deleteQuestionOptionAnswer(@Param("qbankId") Integer qbankId);

    /**
     * 保存选项
     *
     * @param questionOptionAnswer
     * @return
     */
    int saveQuestionOptionAnswer(QuestionOptionAnswer questionOptionAnswer);

    /**
     * 批量保存选项
     *
     * @param questionOptionAnswerList
     * @return
     */
    int batchSaveQuestionOptionAnswer(List<QuestionOptionAnswer> questionOptionAnswerList);

    /**
     * 查询某个题目对应的所有选项
     *
     * @param qbankId
     * @return
     */
    List<QuestionRightAnswer> selectQuestionRightAnswerByQuestionId(@Param("qbankId") Integer qbankId);

    /**
     * 删除正确答案
     *
     * @param qbankId
     * @return
     */
    int deleteQuestionRightAnswer(@Param("qbankId") Integer qbankId);

    /**
     * 删除标签
     *
     * @param qbankId
     * @return
     */
    int deleteQuestionTag(@Param("qbankId") Integer qbankId);

    /**
     * 保存正确答案
     *
     * @param questionRightAnswer
     * @return
     */
    int saveQuestionRightAnswer(QuestionRightAnswer questionRightAnswer);

    /**
     * 批量保存正确答案
     *
     * @param questionRightAnswerList
     * @return
     */
    int batchSaveQuestionRightAnswer(List<QuestionRightAnswer> questionRightAnswerList);

    /**
     * 批量保存标签
     *
     * @param questionTagList
     * @return
     */
    int batchSaveQuestionTag(List<QuestionTag> questionTagList);

    /**
     * 根据题目ID集合字符串查询题目标题
     *
     * @param questionIds
     * @return
     */
    List<Map<String, Object>> selectQuestionsByIds(@Param("questionIds") String questionIds);

    /**
     * 根据试卷ID是删除大题标题
     *
     * @param testPaperID
     * @return
     */
    int deleteQuestionTitleByTestPaperId(Integer testPaperID);

    /**
     * 查询第一级错题分类
     *
     * @return
     */
    List<KnowledgePointMistakes> selectKnowlwdgePointMistakes();

    /**
     * 查询子错题分类
     *
     * @param parentId 父ID
     * @return
     */
    List<KnowledgePointMistakes> selectChildKnowledgePointMistakes(Integer parentId);

    /**
     * 查询父题目ID
     *
     * @param parentId
     * @return
     */
    Map<String, Integer> selectParentKnowledgePointMistakes(Integer parentId);

    /**
     * 根据分数ID以及题目ID查询学生做的答案
     *
     * @param scoreId
     * @param qbankId
     * @return
     */
    List<QuestionInputAnswer> selectInputAnswerByScoreIdAndQbankId(@Param("scoreId") Integer scoreId, @Param("qbankId") Integer qbankId);

    /**
     * 附带小题类型查询所有题目的ID集合
     *
     * @param parentId
     * @return
     */
    List<Integer> selectIdsByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据年级查询学期
     *
     * @param qgradeId
     * @return
     */
    List<QuestionTerm> selectQuestionTermByGradeId(Integer qgradeId);

    /**
     * 根据父ID查询学期
     *
     * @param parentId
     * @return
     */
    List<QuestionTerm> selectQuestionTermByParentId(Integer parentId);

    /**
     * 查询某个题目对应的标签
     *
     * @param qbankId
     * @return
     */
    List<QuestionTag> selectQuestionTagByQuestionId(@Param("qbankId") Integer qbankId);

    /**
     * 查询题目关联的知识点
     *
     * @param qbankId
     * @return
     */
    List<QuestionKnowledge> getKnowledgeListByQuestionId(Integer qbankId);

    /**
     * 根据知识点ID集合查询题目关联的知识点
     *
     * @param knowledgeList
     * @return
     */
    List<QuestionKnowledge> getKnowledgeListByKnowledgeIdList(@Param("knowledgeList") List<String> knowledgeList);

    /**
     * 删除题目与知识点的关联
     *
     * @param qbankId
     * @return
     */
    int deleteRelatedKnowledgeByQuestionId(Integer qbankId);

    /**
     * 删除知识点与错词本的关联
     *
     * @param qknowledgeId
     * @return
     */
    int deleteRelatedMistakeByKnowledgeId(Integer qknowledgeId);

    /**
     * 批量插入知识点错词关联
     *
     * @param mistakeList
     * @return
     */
    int batchSaveQuestionKnowledgeMistake(List<Map> mistakeList);

    /**
     * 根据错词本ID查询关联的知识点
     *
     * @param mistakeId
     * @return
     */
    List<QuestionKnowledge> getKnowledgeListByMistakeId(Integer mistakeId);

    /**
     * 删除解析
     *
     * @param qbankId
     * @return
     */
    int deleteQuestionExplain(@Param("qbankId") Integer qbankId);

    /**
     * 批量保存解析
     *
     * @param questionExplainList
     * @return
     */
    int batchSaveQuestionExplain(List<QuestionExplain> questionExplainList);

    /**
     * 查询某个题目对应的解析
     *
     * @param qbankId
     * @return
     */
    List<QuestionExplain> selectQuestionExplainByQuestionId(@Param("qbankId") Integer qbankId);

    /**
     * 根据名称查询知识点ID
     *
     * @param knowledgeName
     * @return
     */
    Integer selectIdByKnowledgeName(@Param("knowledgeName") String knowledgeName);
}