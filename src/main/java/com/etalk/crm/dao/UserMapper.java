package com.etalk.crm.dao;

import com.etalk.crm.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface UserMapper {
    /**
     * 登录用户查询信息
     * @param loginNamePwdToken 登录名和密码md5加密字符串(登录名大写处理)
     * @return 登录人信息
     */
    User selectLoginInfoByLoginNamePwdToken(@Param("loginNamePwdToken") String loginNamePwdToken);

    /**
     * 查询ssc列表
     * @return
     */
    List<User> selectSSc();

    /**
     * 修改用户密码
     * @param userId 用户id
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 修改记录数
     */
    int updatePassword(@Param("userId")Integer userId,@Param("oldPwd")String oldPwd,@Param("newPwd")String newPwd);

    /**
     *
     * @param roleList  cc  ssc  各自的角色  列表
     * @return
     */
    List<User> ccSscList(@Param("roleList")List<Integer> roleList);
}
