package com.etalk.crm.service;

import com.etalk.crm.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Jordan
 */
public interface UserService {

    /**
     * 查询登录人信息
     * @param loginName 登录名称
     * @param loginPwd 登录密码
     * @param session HttpSession
     * @return 登录是否成功
     */
    boolean login(String loginName, String loginPwd, HttpSession session);

    /**
     * ssc列表
     * @return
     */
    List<User> sscList();

	/**
	 * 修改密码
	 * @param userId 用户id
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @return 修改记录数
	 */
	int modifyPassword(Integer userId, String oldPwd, String newPwd);
	
	
	/*
	 * @Author James
	 * @Description 查询  cc  ssc  列表
	 * @Date 11:10 2018/11/1
	 * @Param 
	 * @return 
	 **/
	List<User> ccSscList(List<Integer> roleList);
}
