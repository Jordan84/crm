package com.etalk.crm.dao;

import com.etalk.crm.pojo.MessageSendRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface MessageSendRecordMapper {

	/**
	 * 查询消息记录分页
	 * @return
	 */
	List<MessageSendRecord> selectMessageSendRecordList(MessageSendRecord record);

	/**
	 * 保存消息发送记录
	 * @param record
	 * @return
	 */
	int insertMessageSendRecord(MessageSendRecord record);

	/**
	 * 修改消息不再提示
	 * @param messageId 消息id
	 * @return 修改记录数
	 */
	int updateMessageStateById(@Param("id")Integer messageId);

}
