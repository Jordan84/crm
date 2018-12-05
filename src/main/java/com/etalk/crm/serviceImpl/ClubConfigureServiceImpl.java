package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.ClubScopeConfigureMapper;
import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.ClubConfigureInfo;
import com.etalk.crm.pojo.ClubScopeConfigure;
import com.etalk.crm.pojo.GradeLevel;
import com.etalk.crm.service.ClubConfigureService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/11/7 16:13
 * @Description:
 */
@Service
public class ClubConfigureServiceImpl implements ClubConfigureService {
    @Resource
    private ClubScopeConfigureMapper clubScopeConfigureMapper;

    @Override
    public List<ClubConfigureInfo> clubConfigureInfoList() {
        return clubScopeConfigureMapper.clubConfigureInfoList();
    }

    @Override
    public ClubConfigureInfo clubConfigureInfoById(int clubConfigurInfoId) {
        return clubScopeConfigureMapper.clubConfigureInfoById(clubConfigurInfoId);
    }

    @Override
    public int selectConfigureById(int clubConfigureId) {
        return clubScopeConfigureMapper.selectConfigureById(clubConfigureId);
    }

    @Override
    public List<ClubScopeConfigure> clubConfgiureScopeList(int clubConfigureId) {
        return clubScopeConfigureMapper.clubConfgiureScopeList(clubConfigureId);
    }

    @Transactional()
    @Override
    public int delconfgiureList(int clubConfigureId) {
        return clubScopeConfigureMapper.delconfgiureList(clubConfigureId);
    }

    @Transactional()
    @Override
    public int addConfgiureList(List<ClubScopeConfigure> list) {
        return clubScopeConfigureMapper.addConfgiureList(list);
    }

    @Transactional()
    @Override
    public int addClubConfigureInfo(ClubConfigureInfo clubConfigureInfo) {
        return clubScopeConfigureMapper.addClubConfigureInfo(clubConfigureInfo);
    }

    @Transactional()
    @Override
    public int eidtClubConfigureInfo(ClubConfigureInfo clubConfigureInfo) {
        return clubScopeConfigureMapper.eidtClubConfigureInfo(clubConfigureInfo);
    }

    @Transactional()
    @Override
    public int subclubScopeConfigure(List<Map> paramList, ClubConfigureInfo clubConfigureInfo) {
        //判断 配置 表名字  是否存在
        if(clubConfigureInfo !=null){
            if(clubScopeConfigureMapper.selectClubInfoByName(clubConfigureInfo)>0){
                return -2;
            }
        }
        int result = 0;
        List<ClubScopeConfigure> confgiureList = new ArrayList();
        for(int i = 0;i<paramList.size();i++){
            ClubScopeConfigure csc = new ClubScopeConfigure();
            csc.setSort(Integer.parseInt(paramList.get(i).get("sort").toString()));
            csc.setIntervalMinValue(Integer.parseInt(paramList.get(i).get("intervalMinValue").toString()));
            csc.setIntervalMinSign(Integer.parseInt(paramList.get(i).get("intervalMinSign").toString()));
            csc.setSalesClubType(Integer.parseInt(paramList.get(i).get("salesClubType").toString()));
             if("".equals(paramList.get(i).get("intervalMaxValue").toString())){
                csc.setIntervalMaxValue(0);
            }else{
               csc.setIntervalMaxValue(Integer.parseInt(paramList.get(i).get("intervalMaxValue").toString()));
            }
            if("".equals(paramList.get(i).get("intervalMaxSign").toString())){
                csc.setIntervalMaxSign(0);
            }else{
                csc.setIntervalMaxSign(Integer.parseInt(paramList.get(i).get("intervalMaxSign").toString()));
            }
            if(paramList.get(i).get("clubConfigureId") != null && StringUtil.isNotEmpty(paramList.get(i).get("clubConfigureId").toString())){
                 csc.setClubConfigureId(Integer.parseInt(paramList.get(i).get("clubConfigureId").toString()));
            }
            confgiureList.add(csc);
        }
        //如果 clubConfigureInfo id 存在 说明 做 更新操作
        if(clubConfigureInfo.getId() == 0){
            result = clubScopeConfigureMapper.addClubConfigureInfo(clubConfigureInfo);
            List<ClubScopeConfigure> cscList = new ArrayList<ClubScopeConfigure>();
            for(ClubScopeConfigure csc :confgiureList){
                csc.setClubConfigureId(clubConfigureInfo.getId());
                cscList.add(csc);
            }
            if(result > 0){
                return clubScopeConfigureMapper.addConfgiureList(cscList);
            }
        }else{
            clubScopeConfigureMapper.eidtClubConfigureInfo(clubConfigureInfo);
            result = clubScopeConfigureMapper.selectConfigureById(clubConfigureInfo.getId());
            if(result >0){
                clubScopeConfigureMapper.delconfgiureList(clubConfigureInfo.getId());
            }
            return  clubScopeConfigureMapper.addConfgiureList(confgiureList);
        }
        return 0;
    }

    @Override
    public List<GradeLevel> selectClubType() {
        return clubScopeConfigureMapper.selectClubType();
    }

    @Override
    public GradeLevel selectClubById(int clubType) {
        return clubScopeConfigureMapper.selectClubById(clubType);
    }
}
