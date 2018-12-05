package com.etalk.crm.dao;

import com.etalk.crm.pojo.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface MessageTemplateMapper {

	/**
	 * 查询模板信息
	 * @param id 模板id
	 * @return 模板对象MessageTemplate
	 */
	MessageTemplate selectMessageTemplateById(@Param("id")Integer id);

	/**
	 * 查询所有有效的模板id和名称，用于模板选择
	 * @return 列表
	 */
	List<MessageTemplate> selectIdAndNameList();
}
