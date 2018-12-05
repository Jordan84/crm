package com.etalk.crm.dao;

import com.etalk.crm.pojo.WeixinMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Jordan
 */
@Mapper
public interface WeixinMessageMapper {

	/**
	 * 查询微信模板信息
	 * @param id 模板id
	 * @return WeixinMessage
	 */
	WeixinMessage selectWeixinTemplateById(@Param("id")String id);
}
