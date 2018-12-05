package com.etalk.crm.serviceImpl;


import com.etalk.crm.dao.AchievementManagementMapper;
import com.etalk.crm.pojo.AchievementManagement;
import com.etalk.crm.pojo.GradeLevel;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.service.AchievementManagementService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Auther: James
 * @Date: 2018/10/8 16:43
 * @Description:
 */
@Service
public class AchievementManagementImpl implements AchievementManagementService {
    protected static final Logger logger = LogManager.getLogger(AchievementManagementService.class);

    @Resource
    private AchievementManagementMapper achievementManagementMapper;

    @Transactional()
    @Override
    public int addAchievement(AchievementManagement achievementManagement) {
        return achievementManagementMapper.addAchievement(achievementManagement);
    }

    @Override
    public List<GradeLevel> gradeLevelList() {
        return achievementManagementMapper.gradeLevelList();
    }

    @Override
    public int isAchievementExist(AchievementManagement achievementManagement) {
        return achievementManagementMapper.isAchievementExist(achievementManagement);
    }

    @Override
    public List<GradeLevel> gradeTypeList() {
        return achievementManagementMapper.gradeTypeList();
    }

    @Override
    public AchievementManagement selectAchievementCollectionParam(int wechatAchievementMsgId) {
        return achievementManagementMapper.selectAchievementCollectionParam(wechatAchievementMsgId);
    }

    @Override
    public PageInfo<AchievementManagement> achievementList(Integer page, Integer size, AchievementManagement achievementManagement) {
        Map paramMap = new HashMap();
        paramMap.put("page",page);
        paramMap.put("pageSize",size);
        paramMap.put("achievementManagement",achievementManagement);
        PageHelper.startPage(page, size);
        PageInfo<AchievementManagement> pageInfo = new PageInfo<AchievementManagement>(achievementManagementMapper.achievementList(paramMap));
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        page = pageInfo.getPageNum();
        size = pageInfo.getPageSize();
        return pageInfo;
    }
    @Transactional()
    @Override
    public int delAchievementManagement(int id) {
        return achievementManagementMapper.delAchievementManagement(id);
    }

    @Override
    public AchievementManagement selectById(int id) {
        return achievementManagementMapper.selectById(id);
    }

    @Override
    public List<Person> personList(String loginName) {
        return achievementManagementMapper.personList(loginName);
    }

    @Transactional()
    @Override
    public int ajaxachievementEdit(AchievementManagement achievementManagement) {
        return achievementManagementMapper.ajaxachievementEdit(achievementManagement);
    }

    @Transactional()
    @Override
    public int addWechatAchievementMsg(AchievementManagement achievementManagement) {
        return achievementManagementMapper.addWechatAchievementMsg(achievementManagement);
    }

    @Override
    public int selectWechatAchievementMsg(AchievementManagement achievementManagement) {
        return achievementManagementMapper.selectWechatAchievementMsg(achievementManagement);
    }

    @Override
    public PageInfo<AchievementManagement> wechatAchievementMsgList(Integer page, Integer size,String time) {
        PageHelper.startPage(page, size);
        PageInfo<AchievementManagement> pageInfo = new PageInfo<AchievementManagement>(achievementManagementMapper.wechatAchievementMsgList(time));
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        page = pageInfo.getPageNum();
        size = pageInfo.getPageSize();
        return pageInfo;
    }

    @Override
    public AchievementManagement selectWechatMsgById(int wechatAchievementMsgId) {
        return achievementManagementMapper.selectWechatMsgById(wechatAchievementMsgId);
    }

    @Transactional()
    @Override
    public int updateWechatMsgById(int wechatAchievementMsgId) {
        return achievementManagementMapper.updateWechatMsgById(wechatAchievementMsgId);
    }
}
