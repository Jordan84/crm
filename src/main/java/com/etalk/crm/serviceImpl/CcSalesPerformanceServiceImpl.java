package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.CcSalesTargetMapper;
import com.etalk.crm.service.CcSalesPerformanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/9/17 19:11
 * @Description:
 */
@Service
public class CcSalesPerformanceServiceImpl implements CcSalesPerformanceService {
    @Resource
    private CcSalesTargetMapper ccSalesTargetMapper;

    @Override
    public Integer thisMonthtSalesTarget(int ccId) {
        return ccSalesTargetMapper.thisMonthtSalesTarget(ccId);
    }

    @Override
    public Integer thisMonthtSales(int ccId) {
        return ccSalesTargetMapper.thisMonthtSales(ccId);
    }

    @Override
    public Integer thisMonthStoresSales(Integer storesId) {
        List<Integer> ccIdList = ccSalesTargetMapper.selectCcByStoresId(storesId);
        if(ccIdList != null && ccIdList.size()>0){
             return ccSalesTargetMapper.thisMonthStoresSales(ccIdList);
        }else{
            return 0;
        }
    }

    @Override
    public Integer thisMonthStoresSalesTarget(Integer storesId) {
        return ccSalesTargetMapper.thisMonthStoresSalesTarget(storesId);
    }

    @Override
    public Integer selectStoresByUserId(int userId) {
        return ccSalesTargetMapper.selectStoresByUserId(userId);
    }
}
