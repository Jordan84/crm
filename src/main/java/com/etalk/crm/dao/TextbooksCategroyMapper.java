package com.etalk.crm.dao;

import com.etalk.crm.pojo.TextbookesCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/10/24 16:50
 * @Version 1.0
 * @Description 教材分类
 **/
@Mapper
public interface TextbooksCategroyMapper {
    /**
     * 根据根级分类，父级分类及等级查询教材分类id及名称
     *
     * @param map
     * @return List<TextbookesCategory>
     */
    List<TextbookesCategory> searchTextbooksCategroyBySearch(Map<?, ?> map);
}
