package com.etalk.crm.service;

import com.etalk.crm.pojo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Jordan
 */
public interface MessageSendRecordService {

	/**
	 * 分页查询消息记录
	 * @param record 参数
	 * @param pageNum 页码
	 * @param pageSize 总记录数
	 * @return 结果
	 */
	PageInfo<MessageSendRecord> searchSendRecordByPage(MessageSendRecord record,int pageNum, int pageSize);

	/**
	 * 查询自定义的消息模板id和名称
	 * @return List<MessageTemplate>
	 */
	Map<String, Object> searchMessageTemplate();

	/**
	 * 查询微信模板格式和预定义的内容
	 * @param templateId 预定义的消息模板id
	 * @return Map<String,Object>
	 */
	MessageTemplate searchMessageTemplateById(Integer templateId);

	/**
	 * 手动关闭消息提醒
	 * @param messageId 消息id
	 * @return 结果
	 */
	int closeMessageShow(Integer messageId);
	/**
	 * 保存发送的消息内容
	 * @param record 消息对象
	 * @return 记录数
	 */
	int saveMessageSendRecord(SubmitTemplateInfo record);

	/**
	 * 关闭空闲班表
	 * @param record 参数
	 * @return 记录数
	 */
	int closeTeacherFreeClass(Lessons record);

	/**
	 * 查询公告界面排除老师中可选老师数据
	 * @param record 查询条件
	 * @return 结果
	 */
	List<String> searchTeacherLoginNameListByMsgSend(Lessons record);

	/**
	 * 查询公告界面筛选后的学员
	 * @param record 查询条件
	 * @return 结果
	 */
	List<Person> searchStudentIdListByMsgSend(Lessons record);
}
