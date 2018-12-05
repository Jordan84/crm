package com.etalk.crm.dao;

import com.etalk.crm.pojo.KcOrders;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/11/13 11:39
 * @Version 1.0
 * @Description 订单
 **/
@Mapper
public interface KcOrdersMapper {
    /**
     * 根据订单ID查询订单
     *
     * @param orderId
     * @return
     */
    KcOrders selectByOrderId(String orderId);

    /**
     * 添加订单
     *
     * @param record
     * @return
     */
    int insertOrder(KcOrders record);

    /**
     * 获取学员有效订单最后一个过期日期
     *
     * @param personId
     * @return
     */
    Date getLastOutdate(Integer personId);
}
