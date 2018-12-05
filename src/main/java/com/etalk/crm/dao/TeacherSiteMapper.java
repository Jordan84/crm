package com.etalk.crm.dao;

import com.etalk.crm.pojo.TeacherSite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface TeacherSiteMapper {

	/**
	 * 查询教学基地列表
	 * @return List<TeacherSite>
	 */
	List<TeacherSite> selectTeacherSiteList();
}
