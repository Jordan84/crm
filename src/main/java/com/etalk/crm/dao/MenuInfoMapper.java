package com.etalk.crm.dao;

import com.etalk.crm.pojo.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jordan
 */
@Mapper
public interface MenuInfoMapper {
    /**
     * 查询第一级菜单
     * @param userId 用户id
     * @return 第一级菜单
     */
    List<MenuInfo> selectMenuFirstByUserId(@Param("userId")Integer userId);

    /**
     * 查询子级菜单
     * @param jurisdictionId 权限id
     * @param parentId 父级菜单id
     * @return 子级菜单
     */
    List<MenuInfo> selectMenuNextByParentId(@Param("jurisdictionId")Integer jurisdictionId, @Param("parentId")Integer parentId);

    /**
     * 查询用户所有授权菜单列表(兼容老版权限控制)
     * @param userId 后台用户id
     * @return 菜单数据
     */
    List<MenuInfo> searchMenuInfoByUsersId(@Param("userId") Integer userId);
}
