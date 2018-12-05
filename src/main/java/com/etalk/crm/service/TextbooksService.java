package com.etalk.crm.service;

import com.etalk.crm.pojo.TextbookesCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 */
public interface TextbooksService {
    /**
     * 分页查询教材信息
     *
     * @param map 查询条件
     * @return List<Textbooks>
     */
    PageInfo selectTextbooks(Map<String, Object> map, Integer pageNum, Integer pageSize);

    /**
     * 根据三级分类查询教材
     *
     * @param thirdId
     * @return
     */
    PageInfo selectTextbooksByThirdId(Integer thirdId, Integer pageNum, Integer pageSize);

    /**
     * 根据根级分类，父级分类及等级查询教材分类id及名称
     *
     * @param map
     * @return List<TextbookesCategory>
     */
    List<TextbookesCategory> searchTextbooksCategroyBySearch(Map<?, ?> map);

    /**
     * 查询知识点关联的教材
     *
     * @param knowledgeId
     * @return
     */
    PageInfo getTextbooksListByKnowledgeId(Integer knowledgeId, Integer pageNum, Integer pageSize);

    /**
     * 获取教材配置文件，取教材实际页码
     *
     * @param strUrl
     * @return
     */
    List<Map> showTextbooksPageList(String strUrl);

    /**
     * 根据页码取索引
     *
     * @param strUrl
     * @param pageName
     * @return
     */
    int getTextbooksIdxByPage(String strUrl, String pageName);
}
