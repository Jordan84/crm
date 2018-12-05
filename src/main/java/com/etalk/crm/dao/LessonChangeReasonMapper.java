package com.etalk.crm.dao;

import com.etalk.crm.pojo.LessonChangeReason;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface LessonChangeReasonMapper {
	/**
	 * 查询所有void原因
	 * @return 对象
	 */
	List<LessonChangeReason> selectVoidReasonList();
}
