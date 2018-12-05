package com.etalk.crm.dao;

import com.etalk.crm.pojo.Lessons;
import com.etalk.crm.pojo.MessageSendPlatformRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface MessageSendPlatformRecordMapper {

	/**
	 * 保存发送消息的平台记录
	 * @param list 要保存的数据列表
	 * @return 记录数
	 */
	int insertMessageSendPlatformRecord(@Param("list") List<MessageSendPlatformRecord> list);


}
