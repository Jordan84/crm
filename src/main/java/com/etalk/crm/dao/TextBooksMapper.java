package com.etalk.crm.dao;

import com.etalk.crm.pojo.Textbooks;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/10/24 15:13
 * @Version 1.0
 * @Description 教材
 **/
@Mapper
public interface TextBooksMapper {
    /**
     * 根据ID查询教材
     *
     * @param id
     * @return
     */
    Textbooks selectByTextbooksId(Integer id);

    /**
     * 分页查询教材信息
     *
     * @param map 查询条件
     * @return List<Textbooks>
     */
    List<Textbooks> selectTextbooks(Map<String, Object> map);

    /**
     * 根据三级分类查询教材
     *
     * @param thirdId
     * @return
     */
    List<Textbooks> selectTextbooksByThirdId(Integer thirdId);

    /**
     * 查询知识点关联的教材
     *
     * @param knowledgeId
     * @return
     */
    List<Textbooks> getTextbooksListByKnowledgeId(Integer knowledgeId);

    /**
     * 删除某个知识点与教材的关联
     *
     * @param knowledgeId
     * @return
     */
    int deleteRelatedTextbooksByKnowledgeId(Integer knowledgeId);

    /**
     * 批量添加知识点与教材的关联
     *
     * @param textbooksList
     * @return
     */
    int batchSaveTextbooksKnowledge(List<Map> textbooksList);
}
