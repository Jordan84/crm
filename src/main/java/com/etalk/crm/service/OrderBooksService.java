package com.etalk.crm.service;

import com.etalk.crm.pojo.OrderBooks;

/**
 * @Author Terwer
 * @Date 2018/11/13 13:45
 * @Version 1.0
 * @Description 订单教材信息
 **/
public interface OrderBooksService {
    /**
     * 订单教材信息
     *
     * @param ob
     * @return
     */
    boolean addOrderBooks(OrderBooks ob);
}
