package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.dao.MenuInfoMapper;
import com.etalk.crm.dao.UserMapper;
import com.etalk.crm.pojo.MenuInfo;
import com.etalk.crm.pojo.User;
import com.etalk.crm.service.UserService;
import com.etalk.crm.utils.EncryptAndDecrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Jordan
 */
@Service
public class UserServiceImpl implements UserService {
    protected static final Logger logger= LogManager.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private  MenuInfoMapper menuInfoMapper;

    /**
     * 查询登录人信息
     * @param loginName 登录名称
     * @param loginPwd 登录密码
     * @return 登录是否成功
     */
    @Override
    public boolean login(String loginName,String loginPwd, HttpSession session) {
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(loginPwd)){
            return false;
        }
        String loginNamePwdToken= EncryptAndDecrypt.MD5(loginName.toUpperCase()+loginPwd);
        User user=userMapper.selectLoginInfoByLoginNamePwdToken(loginNamePwdToken);
        if (!StringUtils.isEmpty(user) && user.getId()>0){
            session.setAttribute("userid", user.getId());
            session.setAttribute("loginname", user.getLoginName());
            session.setAttribute("roleId", user.getRoleId());
            session.setAttribute("storesId", user.getStoresId());
            session.setAttribute("username", user.getCnName());
            session.setAttribute("robCase", user.getRobCase());
            session.setAttribute("accountId", user.getAccountId());

            /*Stores stores=storesMapper.selectStoreInfoById(user.getStoresId());
            session.setAttribute("storesName", stores.getName());
            session.setAttribute("storesEnName", stores.getEnName());
            session.setAttribute("hflag", stores.getHomeFlag());
            session.setAttribute("roleflag", stores.getRoleFlag());
            session.setAttribute("shareAreaId", stores.getShareAreaId());
            session.setAttribute("menus", menuInfoMapper.searchMenuInfoByUsersId(user.getId()));*/
            //菜单权限
	        List<MenuInfo> menuList = menuInfoMapper.searchMenuInfoByUsersId(user.getId());
            Map<Integer,String>menuMap = new HashMap<>(menuList.size());
            for(MenuInfo menu : menuList){
                menuMap.put(menu.getId(),menu.getMenuName());
            }
            session.setAttribute("menuMap",menuMap);
            Map<String,Object> map=new HashMap<>(4);
            map.put("loginId",user.getId());
            map.put("loginName",user.getLoginName());
            map.put("roleId",user.getRoleId());
            map.put("storesId",user.getStoresId());
            session.setAttribute("userToken",EncryptAndDecrypt.encrypt(JSON.toJSONString(map)));
	        logger.info("URLEncoder :  "+ EncryptAndDecrypt.encrypt(JSON.toJSONString(map)));

          /*  try {
                String userToken=EncryptAndDecrypt.encrypt(JSON.toJSONString(map));
                logger.info("userToken:  "+ userToken);
                userToken= URLEncoder.encode(userToken,"UTF-8");
                logger.info("URLEncoder :  "+ userToken);
                logger.info(userToken+"  :  "+EncryptAndDecrypt.decrypt(URLDecoder.decode(userToken, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            return true;
        }
        return false;
    }

    /**
     * ssc 列表
     * @return
     */
    @Override
    public List<User> sscList() {
        return userMapper.selectSSc();
    }

    /**
     * 修改密码
     * @param userId 用户id
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 修改记录数
     */
    @Override
    public int modifyPassword(Integer userId,String oldPwd,String newPwd){
        return userMapper.updatePassword(userId,oldPwd,newPwd);
    }

    @Override
    public List<User> ccSscList(List<Integer> roleList) {
        return userMapper.ccSscList(roleList);
    }
}
