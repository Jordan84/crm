package com.etalk.crm.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Auther: James
 * @Date: 2018/9/13 14:40
 * @Description: cc 销售目标
 */
@Mapper
public interface CcSalesTargetMapper {
    /**
     * 当前月份 cc 销售目标
     * @param ccId
     * @return
     */
    Integer thisMonthtSalesTarget(@Param("ccId") int ccId);
     /**
     * 当前月份 cc 销售额
     * @param ccId
     * @return
     */
    Integer thisMonthtSales(@Param("ccId") int ccId);
    /**
     * 当前月份 店铺 销售额
     * @param ccIdList
     * @return
     */
    Integer thisMonthStoresSales(@Param("ccIdList") List<Integer> ccIdList);

     /**
     * 当前月份 店铺 销售目标
     * @param storesId
     * @return
     */
    Integer thisMonthStoresSalesTarget(@Param("storesId") Integer storesId);

     /**
     * 根据店铺查询  该店铺的 cc
     * @param storesId
     * @return
     */
    List<Integer>selectCcByStoresId(@Param("storesId")Integer storesId);

    /**
     * 查询 当前登录账号  所属店铺
     * @param userId
     * @return
     */
    Integer selectStoresByUserId(@Param("userId") int userId);
}
