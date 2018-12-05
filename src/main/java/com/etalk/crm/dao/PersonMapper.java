package com.etalk.crm.dao;

import com.etalk.crm.pojo.Person;
import com.etalk.crm.pojo.RecommendUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface PersonMapper {
	/**
	 * 查询所有有效的老师
	 *
	 * @param teaSiteId 教学基地
	 * @return 老师列表
	 */
	List<String> selectTeacherListByMsgSend(@Param("teaSiteId") List<Integer> teaSiteId);

	/**
	 * 查询有绑定app或微信公众号的学员
	 *
	 * @return
	 */
	List<Person> selectStudentListByMsgSend();

	/**
	 * 查询SSCteacher
	 *
	 * @return
	 * @date 2018-05-18
	 */
	List<Person> selectSSCTeacher();

	/**
	 * 查询所有有效的学生
	 *
	 * @param studentName
	 * @return 学生列表
	 */
	List<Person> selectStudentListByPaperSend(@Param("studentName") String studentName);

	/**
	 * 根据sscId  查询学员列表
	 *
	 * @param ccId
	 * @return
	 */
	List<Person> personList(@Param("ccId") Integer ccId,@Param("loginName")String loginName);

	/**
	 * 根据personId查询学员信息
	 *
	 * @param personId
	 * @return
	 */
	Person selectByPersonId(@Param("personId") int personId);

	/**
	 * 根据登录名查询学员信息，用于excel导入
	 *
	 * @param loginName
	 * @return
	 */
	Person selectPersonByLoginName(@Param("loginName") String loginName);

	/**
	 * 根据登录名集合查询用户
	 *
	 * @param loginNameList
	 * @return
	 */
	List<RecommendUsers> selectPersonByLoginNameList(@Param("loginNameList") List loginNameList);
}
