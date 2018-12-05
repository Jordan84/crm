package com.etalk.crm.service;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/9/17 19:10
 * @Description:
 */
public interface CcSalesPerformanceService {
    /**
     * 当前月份销售目标
     * @param ccId
     * @return
     */
    Integer thisMonthtSalesTarget(int ccId);
     /**
     * 当前月份  销售额
     * @param ccId
     * @return
     */
    Integer thisMonthtSales(int ccId);


     /**
     * 当前月份 店铺 销售额
     * @param storesId
     * @return
     */
    Integer thisMonthStoresSales(Integer storesId);

     /**
     * 当前月份 店铺 销售目标
     * @param storesId
     * @return
     */
    Integer thisMonthStoresSalesTarget(Integer storesId);

     /**
     * 查询 当前登录账号  所属店铺
     * @param userId
     * @return
     */
    Integer selectStoresByUserId(int userId);

}
