package com.etalk.crm.dao;

import com.etalk.crm.pojo.MessageReceivingPlatform;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jordan
 * @date 2018-04-26 17:17
 */
@Mapper
public interface MessageReceivingPlatformMapper {

	/**
	 * 查询消息接收平台
	 * @return List<MessageReceivingPlatform>
	 */
	List<MessageReceivingPlatform> selectMessageReceivingPlatformList();
}
