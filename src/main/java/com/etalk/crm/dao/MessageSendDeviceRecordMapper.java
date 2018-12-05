package com.etalk.crm.dao;

import com.etalk.crm.pojo.Lessons;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jordan
 */
@Mapper
public interface MessageSendDeviceRecordMapper {

	/**
	 * 复制APP学员接收消息的设备信息
	 * @param record 条件
	 * @return 记录数
	 */
	int insertMessageSendPlatformRecordByApp(Lessons record);

	/**
	 * 复制微信学员接收消息的设备信息
	 * @param record 条件
	 * @return 记录数
	 */
	int insertMessageSendPlatformRecordByWeixin(Lessons record);
}
