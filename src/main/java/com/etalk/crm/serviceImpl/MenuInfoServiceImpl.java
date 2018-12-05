package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.MenuInfoMapper;
import com.etalk.crm.pojo.MenuInfo;
import com.etalk.crm.service.MenuInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Jordan
 */
@Service
public class MenuInfoServiceImpl implements MenuInfoService {
    protected static final Logger logger=LogManager.getLogger(MenuInfoServiceImpl.class);

    @Resource
    private MenuInfoMapper menuInfoMapper;

    /**
     * 查询第一级菜单
     *
     * @param session HttpSession
     * @return 第一级菜单
     */
    @Override
    public List<MenuInfo> searchMenuFirstByUserId(HttpSession session) {
        return this.addSessionId(menuInfoMapper.selectMenuFirstByUserId(Integer.parseInt(session.getAttribute("userid").toString())),session);
    }

    /**
     * 查询子级菜单
     * @param jurisdictionId 权限id
     * @param parentId 父级菜单id
     * @return 子级菜单
     */
    @Override
    public List<MenuInfo> searchMenuNextByUserId(Integer jurisdictionId,Integer parentId,HttpSession session) {
        return this.addSessionId(menuInfoMapper.selectMenuNextByParentId(jurisdictionId,parentId),session);
    }

    /**
     * 菜单url添加sessionId
     * @param list 菜单列表
     * @param session HttpSession
     */
    private List<MenuInfo> addSessionId(List<MenuInfo> list, HttpSession session){
        if(list!=null && list.size()>0){
            for (int i=0;i<list.size();i++){
                if (!StringUtils.isEmpty(list.get(i).getMenuUrl()) && !StringUtils.isEmpty(list.get(i).getMenuUrl().trim())){
                    if (list.get(i).getMenuUrl().lastIndexOf(".action")>0){
                        if (list.get(i).getMenuUrl().lastIndexOf(".action?")>0){
                            list.get(i).setMenuUrl(list.get(i).getMenuUrl()+"&userToken="+session.getAttribute("userToken"));
                        }else {
                            list.get(i).setMenuUrl(list.get(i).getMenuUrl()+"?userToken="+session.getAttribute("userToken"));
                        }
                    }
                }
            }
        }
        return list;
    }
}
