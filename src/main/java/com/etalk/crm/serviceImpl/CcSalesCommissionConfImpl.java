package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.CcSalesCommissionsConfMapper;
import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.CcSalesCommissionsConf;
import com.etalk.crm.pojo.SalesCommissionInfo;
import com.etalk.crm.service.CcSalesCommissionConfService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/10/26 14:08
 * @Description:
 */
@Service
public class CcSalesCommissionConfImpl implements CcSalesCommissionConfService {
    @Resource
    private CcSalesCommissionsConfMapper ccSalesCommissionsConfMapper;

    @Override
    public List<SalesCommissionInfo> salesCcommissionInfoList() {
        return ccSalesCommissionsConfMapper.salesCcommissionInfoList();
    }

    @Override
    public SalesCommissionInfo selectSCIById(int salesCcommissionId) {
        return ccSalesCommissionsConfMapper.selectSCIById(salesCcommissionId);
    }

    @Override
    public int selectConfigureById(int salesCcommissionId) {
        return ccSalesCommissionsConfMapper.selectConfigureById(salesCcommissionId);
    }

    @Override
    public List<CcSalesCommissionsConf> confgiureList(int salesCcommissionId) {
        return ccSalesCommissionsConfMapper.confgiureList(salesCcommissionId);
    }

    @Transactional()
    @Override
    public int eidtCommissionInfo(SalesCommissionInfo SalesCommissionInfo) {
        return ccSalesCommissionsConfMapper.eidtCommissionInfo(SalesCommissionInfo);
    }

    @Transactional()
    @Override
    public int submitccSalesCommissionConf(List<Map> paramList, SalesCommissionInfo salesCommissionInfo) {
         //判断 配置 表名字  是否存在
        if(salesCommissionInfo != null ){
            if(ccSalesCommissionsConfMapper.selectCommissionInfoByName(salesCommissionInfo)>0){
                return -2;
            }
        }
        int result = 0;
        List<CcSalesCommissionsConf> confgiureList = new ArrayList();
        for(int i = 0;i<paramList.size();i++){
            CcSalesCommissionsConf cscc = new CcSalesCommissionsConf();
            cscc.setSort(Integer.parseInt(paramList.get(i).get("sort").toString()));
            cscc.setIntervalMinValue(Integer.parseInt(paramList.get(i).get("intervalMinValue").toString()));
            cscc.setIntervalMinSign(Integer.parseInt(paramList.get(i).get("intervalMinSign").toString()));
            if("".equals(paramList.get(i).get("intervalMaxValue").toString())){
                cscc.setIntervalMaxValue(0);
            }else{
               cscc.setIntervalMaxValue(Integer.parseInt(paramList.get(i).get("intervalMaxValue").toString()));
            }
            if("".equals(paramList.get(i).get("intervalMaxSign").toString())){
                cscc.setIntervalMaxSign(0);
            }else{
                cscc.setIntervalMaxSign(Integer.parseInt(paramList.get(i).get("intervalMaxSign").toString()));
            }
            cscc.setPercentage(new BigDecimal(paramList.get(i).get("percentage").toString()));
            if(paramList.get(i).get("salesCcommissionId") != null && StringUtil.isNotEmpty(paramList.get(i).get("salesCcommissionId").toString())){
                 cscc.setSalesCcommissionId(Integer.parseInt(paramList.get(i).get("salesCcommissionId").toString()));
            }
            confgiureList.add(cscc);
        }
        //如果 baseSalaryInfo id 存在 说明 做 更新操作
        if(salesCommissionInfo.getId() == 0){
            result = ccSalesCommissionsConfMapper.addCommissionInfo(salesCommissionInfo);
            List<CcSalesCommissionsConf> cCSCList = new ArrayList<CcSalesCommissionsConf>();
            for(CcSalesCommissionsConf ccsc :confgiureList){
                ccsc.setSalesCcommissionId(salesCommissionInfo.getId());
                cCSCList.add(ccsc);
            }
            if(result > 0){
                result = ccSalesCommissionsConfMapper.addConfgiureList(cCSCList);
                return result;
            }
        }else{
            ccSalesCommissionsConfMapper.eidtCommissionInfo(salesCommissionInfo);
            result = ccSalesCommissionsConfMapper.selectConfigureById(salesCommissionInfo.getId());
            if(result >0){
                result = ccSalesCommissionsConfMapper.delconfgiureList(salesCommissionInfo.getId());
            }
            return ccSalesCommissionsConfMapper.addConfgiureList(confgiureList);
        }
        return 0;
    }

}
