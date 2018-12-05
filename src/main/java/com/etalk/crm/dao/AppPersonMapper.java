package com.etalk.crm.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface AppPersonMapper {

	/**
	 * 查询所有的app设备id
	 * @return
	 */
	List<String> selectDeviceIdList();
}
