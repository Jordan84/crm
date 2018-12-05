package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.BaseSalaryConfigureMapper;
import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.BaseSalaryInfo;
import com.etalk.crm.service.BaseSalaryConfigureService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/10/23 10:59
 * @Description:
 */
@Service
public class BaseSalaryConfigureImpl implements BaseSalaryConfigureService {
    @Resource
    private BaseSalaryConfigureMapper baseSalaryConfigureMapper;

    @Override
    public List<BaseSalaryInfo> salaryInfoList() {
        List<BaseSalaryInfo> salaryInfoList = baseSalaryConfigureMapper.salaryInfoList();

        return  salaryInfoList;
    }

    @Override
    public List<BaseSalaryConfigure> confgiureList(int baseSalaryId) {
        return baseSalaryConfigureMapper.confgiureList(baseSalaryId);
    }

    @Override
    public BaseSalaryInfo selectsalaryInfoById(int baseSalaryId) {
        return baseSalaryConfigureMapper.selectsalaryInfoById(baseSalaryId);
    }

    @Transactional()
    @Override
    public int submitsalaryConfigure(List<Map> paramList, BaseSalaryInfo baseSalaryInfo) {
        //判断 配置 表名字  是否存在
        if(baseSalaryInfo != null){
            if(baseSalaryConfigureMapper.selectbaseSalaryInfoByName(baseSalaryInfo) > 0){
                return  -2;
            }
        }
        int result = 0;
        List<BaseSalaryConfigure> confgiureList = new ArrayList();
        for(int i = 0;i<paramList.size();i++){
            BaseSalaryConfigure bsc = new BaseSalaryConfigure();
            bsc.setSort(Integer.parseInt(paramList.get(i).get("sort").toString()));
            bsc.setIntervalMinValue(Integer.parseInt(paramList.get(i).get("intervalMinValue").toString()));
            bsc.setIntervalMinSign(Integer.parseInt(paramList.get(i).get("intervalMinSign").toString()));
            if("".equals(paramList.get(i).get("intervalMaxValue").toString())){
                bsc.setIntervalMaxValue(0);
            }else{
                bsc.setIntervalMaxValue(Integer.parseInt(paramList.get(i).get("intervalMaxValue").toString()));
            }
            if("".equals(paramList.get(i).get("intervalMaxSign").toString())){
                bsc.setIntervalMaxSign(0);
            }else{
                bsc.setIntervalMaxSign(Integer.parseInt(paramList.get(i).get("intervalMaxSign").toString()));
            }
            bsc.setBaseSalary(Integer.parseInt(paramList.get(i).get("baseSalary").toString()));
            if(paramList.get(i).get("baseSalaryId") != null && StringUtil.isNotEmpty(paramList.get(i).get("baseSalaryId").toString())){
                 bsc.setBaseSalaryId(Integer.parseInt(paramList.get(i).get("baseSalaryId").toString()));
            }
            confgiureList.add(bsc);
        }
        //如果 baseSalaryInfo id 存在 说明 做 更新操作
        if(baseSalaryInfo.getId() == 0){
            result = baseSalaryConfigureMapper.addSalaryInfo(baseSalaryInfo);
            List<BaseSalaryConfigure> bsaList = new ArrayList<BaseSalaryConfigure>();
            for(BaseSalaryConfigure bsa :confgiureList){
                bsa.setBaseSalaryId(baseSalaryInfo.getId());
                bsaList.add(bsa);
            }
            if(result > 0){
                return baseSalaryConfigureMapper.addConfgiureList(bsaList);
            }
        }else{
            baseSalaryConfigureMapper.eidtSalaryInfo(baseSalaryInfo);
            result = baseSalaryConfigureMapper.selectConfigureById(baseSalaryInfo.getId());
            if(result >0){
                result = baseSalaryConfigureMapper.delconfgiureList(baseSalaryInfo.getId());
            }
            return baseSalaryConfigureMapper.addConfgiureList(confgiureList);
        }
        return 0;
    }

    @Transactional()
    @Override
    public int eidtSalaryInfo(BaseSalaryInfo baseSalaryInfo) {
        return baseSalaryConfigureMapper.eidtSalaryInfo(baseSalaryInfo);
    }
}
