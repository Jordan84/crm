package com.etalk.crm.dao;

import com.etalk.crm.pojo.OrderBooks;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Terwer
 * @Date 2018/11/13 13:47
 * @Version 1.0
 * @Description 订单教材信息
 **/
@Mapper
public interface OrderBooksMapper {
    /**
     * 添加订单教材信息
     *
     * @param orderBooks
     * @return
     */
    int insertOrderBooks(OrderBooks orderBooks);
}
