package com.etalk.crm.dao;

import com.etalk.crm.pojo.ClassWays;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface ClassWaysMapper {

	/**
	 * 查询可用的上课方式
	 * @return List<ClassWays>
	 */
	List<ClassWays> selectClassWaysList();
}
