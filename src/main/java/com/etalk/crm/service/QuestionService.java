package com.etalk.crm.service;

import com.etalk.crm.pojo.KnowledgePointMistakes;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionBanksImg;
import com.etalk.crm.pojo.QuestionDifficultyDegree;
import com.etalk.crm.pojo.QuestionGrade;
import com.etalk.crm.pojo.QuestionKnowledge;
import com.etalk.crm.pojo.QuestionMode;
import com.etalk.crm.pojo.QuestionOptionAnswer;
import com.etalk.crm.pojo.QuestionRightAnswer;
import com.etalk.crm.pojo.QuestionTerm;
import com.etalk.crm.pojo.QuestionTypes;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Terwer
 * @since 2018/05/23
 */
public interface QuestionService {
    /**
     * 根据题目搜索数据并分页
     *
     * @param title    题目标题
     * @param pageNum  页号
     * @param pageSize 每页显示记录数
     * @return
     */
    PageInfo<QuestionBanks> selectByTitleAndPage(String title, Integer pageNum, Integer pageSize);

    /**
     * 根据年级和知识点来获取试题列表
     *
     * @param qgradeId
     * @param qknowledgeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<QuestionBanks> selectByGradeAndKnowledge(Integer qgradeId, Integer qknowledgeId, Integer pageNum,
                                                      Integer pageSize);

    /**
     * 根据ID查询题目信息
     *
     * @param questionId 题目ID
     * @return
     */
    QuestionBanks selectByQuestionId(Integer questionId);

    /**
     * 递归设置子题目（包含学生做过的题目）
     *
     * @param questionBanks
     * @return
     */
    QuestionBanks initQuestionBanks(QuestionBanks questionBanks, Integer scoreId);

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
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo selectQKnowledgeList(String search, Integer pageNum, Integer pageSize);

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
     * @param recorder
     * @return
     */
    boolean saveQKnowledge(QuestionKnowledge knowledge, String recorder);

    /**
     * 修改知识点
     *
     * @param knowledge
     * @param recorder
     * @return
     */
    boolean editQKnowledge(QuestionKnowledge knowledge, String recorder);

    /**
     * 检测知识点是否存在
     *
     * @param cnName
     * @return
     */
    boolean checkKnowledgeExist(String cnName);

    /**
     * 检测知识点是否存在
     *
     * @param oldCnName
     * @param newCnName
     * @return
     */
    boolean checkOtherKnowledgeExist(String oldCnName, String newCnName);

    /**
     * 根据题目ID集合查询知识点关联的题目
     *
     * @param questionIdList
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<QuestionBanks> getQuestionListByQuestionIdList(List<String> questionIdList, Integer pageNum, Integer pageSize);

    /**
     * 查询知识点关联的题目
     *
     * @param knowledgeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<QuestionBanks> getQuestionListByKnowledgeId(Integer knowledgeId, Integer pageNum, Integer pageSize);

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
     * 根据题目ID删除题目
     *
     * @param questionId 题目ID
     * @return
     */
    boolean deleteByQuestionId(Integer questionId);

    /**
     * 添加题目
     *
     * @param questionBanks
     * @return
     */
    boolean saveQuestionBank(QuestionBanks questionBanks);

    /**
     * 批量添加题目
     *
     * @param questionBanksList
     * @param recorder
     * @return 返回最后一条记录的题目主键ID
     */
    Integer batchSaveQuestion(List<QuestionBanks> questionBanksList, String recorder);

    /**
     * 修改题目
     *
     * @param questionBanks
     * @return
     */
    boolean updateQuestionBank(QuestionBanks questionBanks);

    /**
     * 查询某个题目对应的所有图片
     *
     * @param qbankId
     * @return
     */
    List<QuestionBanksImg> selectQuestionBanksImgByQuestionId(Integer qbankId);

    /**
     * 删除图片
     *
     * @param qbankId 题目ID
     * @return
     */
    boolean deleteQuestionBanksImg(Integer qbankId);

    /**
     * 保存图片
     *
     * @param questionBanksImg
     * @return
     */
    boolean saveQuestionImg(QuestionBanksImg questionBanksImg);

    /**
     * 批量保存图片
     *
     * @param questionBanksImgList
     * @return
     */
    boolean batchSaveQuestionImg(List<QuestionBanksImg> questionBanksImgList);

    /**
     * 查询某个题目对应的所有选项
     *
     * @param qbankId
     * @return
     */
    List<QuestionOptionAnswer> selectQuestionOptionAnswerByQuestionId(Integer qbankId);

    /**
     * 删除选项
     *
     * @param qbankId
     * @return
     */
    boolean deleteQuestionOptionAnswer(Integer qbankId);

    /**
     * 保存选项
     *
     * @param questionOptionAnswer
     * @return
     */
    boolean saveQuestionOptionAnswer(QuestionOptionAnswer questionOptionAnswer);

    /**
     * 批量保存选项
     *
     * @param questionOptionAnswerList
     * @return
     */
    boolean batchSaveQuestionOptionAnswer(List<QuestionOptionAnswer> questionOptionAnswerList);

    /**
     * 查询某个题目对应的所有选项
     *
     * @param qbankId
     * @return
     */
    List<QuestionRightAnswer> selectQuestionRightAnswerByQuestionId(Integer qbankId);

    /**
     * 删除正确答案
     *
     * @param qbankId
     * @return
     */
    boolean deleteQuestionRightAnswer(Integer qbankId);

    /**
     * 保存正确答案
     *
     * @param questionRightAnswer
     * @return
     */
    boolean saveQuestionRightAnswer(QuestionRightAnswer questionRightAnswer);

    /**
     * 批量保存正确答案
     *
     * @param questionRightAnswerList
     * @return
     */
    boolean batchSaveQuestionRightAnswer(List<QuestionRightAnswer> questionRightAnswerList);

    /**
     * 根据题目ID集合字符串查询题目标题
     *
     * @param questionIds
     * @return
     */
    List<Map<String, Object>> selectQuestionsByIds(String questionIds);

    /**
     * 查看错词本分类
     */
    List<KnowledgePointMistakes> selectKnowledgePointMistakes();

    /**
     * 查询子错词本分类
     *
     * @param parentId
     * @return
     */
    List<KnowledgePointMistakes> selectChildKnowledgePointMistakes(Integer parentId);

    /**
     * 查询所有已选中分类的id
     *
     * @param id
     * @return
     */
    Stack<Integer> getFirstKnnowledgePointMistakesId(Integer id, Stack<Integer> selectedIdsStack);

    /**
     * 根据年级查询学期
     *
     * @param qgradeId
     * @return
     */
    List<QuestionTerm> selectQuestionTermByGradeId(Integer qgradeId);

    /**
     * 根据学期查询单元或者课时
     *
     * @param parentTermId
     * @return
     */
    List<QuestionTerm> selectQuestionTermByParentTermId(Integer parentTermId);

    /**
     * 查询题目关联的知识点
     *
     * @param qbankId
     * @return
     */
    PageInfo getKnowledgeListByQuestionId(Integer qbankId, Integer pageNum, Integer pageSize);

    /**
     * 根据知识点ID集合查询题目关联的知识点
     *
     * @param knowledgeList
     * @return
     */
    PageInfo getKnowledgeListByKnowledgeIdList(@Param("knowledgeList") List<String> knowledgeList, Integer pageNum, Integer pageSize);

    /**
     * 根据错词本ID查询关联的知识点
     *
     * @param mistakeId
     * @return
     */
    List<QuestionKnowledge> getKnowledgeListByMistakeId(Integer mistakeId);

    /**
     * 上传excel文件到临时目录后并开始解析
     *
     * @param fileName
     * @param mfile
     * @return
     */
    Map batchImport(String fileName, MultipartFile mfile);
}
