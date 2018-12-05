package com.etalk.crm.dao;

import com.etalk.crm.pojo.Lessons;
import com.etalk.crm.pojo.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author Jordan
 */
@Mapper
public interface LessonsMapper {

	/**
	 * 查询公告界面排除老师中可选老师数据
	 * @param record 条件
	 * @return 老师列表
	 */
	List<String> selectTeacherListByMsgSend(Lessons record);

	/**
	 * 查询需要发送消息公告的所有学生
	 * @param record 条件
	 * @return
	 */
	List<Person> selectStudentListByMsgSend(Lessons record);

	/**
	 * 查询需要取消的课程列表
	 * @param record 条件
	 * @return 课程id列表
	 */
	List<Integer> selectLessonIdListByMsgSend(Lessons record);

	/**
	 * 取消接收消息的人的课程
	 * @param record 条件
	 * @return 记录数
	 */
	int updateStateByMsgSend(Lessons record);
	/**
	 * 取消接收消息的人的课程
	 * @param record 条件
	 * @return 记录数
	 */
	int updateLearnedClassByMsgSend(Lessons record);

	/**
	 * 关闭老师班表
	 * @param record 条件
	 * @return 删除记录数
	 */
	int deleteLessonByMsgSend(Lessons record);

	/**
	 * 查询需要发送的微信设备唯一id
	 * @param record 条件
	 * @return 设备id
	 */
	List<String> selectWXOpenIdListByMsgSend(Lessons record);

	/**
	 * 查询需要发送的APP设备唯一id
	 * @param record 条件
	 * @return 设备id
	 */
	List<String> selectAppDeviceIdListByMsgSend(Lessons record);
}
