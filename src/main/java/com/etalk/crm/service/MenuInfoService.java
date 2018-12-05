package com.etalk.crm.service;

import com.etalk.crm.pojo.MenuInfo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Jordan
 */
public interface MenuInfoService {
    /**
     * 查询第一级菜单
     * @param session HttpSession
     * @return 第一级菜单
     */
    List<MenuInfo> searchMenuFirstByUserId(HttpSession session);
    /**
     * 查询子级菜单
     * @param jurisdictionId 权限id
     * @param parentId 父级菜单id
     * @return 子级菜单
     */
    List<MenuInfo> searchMenuNextByUserId(Integer jurisdictionId,Integer parentId,HttpSession session);
}
