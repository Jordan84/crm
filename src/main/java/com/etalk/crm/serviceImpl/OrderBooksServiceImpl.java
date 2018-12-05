package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.OrderBooksMapper;
import com.etalk.crm.pojo.OrderBooks;
import com.etalk.crm.service.OrderBooksService;
import com.etalk.crm.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Terwer
 * @Date 2018/11/13 13:46
 * @Version 1.0
 * @Description 订单教材信息
 **/
@Service
public class OrderBooksServiceImpl implements OrderBooksService {
    @Resource
    private OrderBooksMapper orderBooksMapper;

    @Override
    public boolean addOrderBooks(OrderBooks ob) {
        ob.setRegistTime(DateUtil.getCurrentDatetime());
        int num = orderBooksMapper.insertOrderBooks(ob);
        if (num > 0) {
            return true;
        }
        return false;
    }
}
