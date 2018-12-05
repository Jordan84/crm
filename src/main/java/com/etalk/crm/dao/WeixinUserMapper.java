package com.etalk.crm.dao;

import com.etalk.crm.pojo.WeixinUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface WeixinUserMapper {
	/**
	 * 查询所有微信用户发送消息
	 * @return 微信唯一id
	 */
	List<String> selectOpenIdList();

	/**
	 * 查询用户id和微信唯一id
	 * @return 对象
	 */
	List<WeixinUser> selectOpenIdAndPersonIdList();

	/**
	 * 查询用户绑定的微信
	 * @param personId 学员id
	 * @return 微信唯一id
	 */
	List<String> selectOpenIdByPersonId(@Param("personId") Integer personId);
}
