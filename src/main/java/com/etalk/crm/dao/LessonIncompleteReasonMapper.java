package com.etalk.crm.dao;

import com.etalk.crm.pojo.LessonIncompleteReason;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface LessonIncompleteReasonMapper {

	/**
	 * 批量插入取消课程原因
	 * @param list
	 * @return
	 */
	int insertLessonIncompleteReason(@Param("list") List<LessonIncompleteReason> list);
}
