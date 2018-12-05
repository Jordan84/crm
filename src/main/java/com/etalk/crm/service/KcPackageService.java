package com.etalk.crm.service;


import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/8 18:23
 * @Version 1.0
 * @Description 套餐
 **/
public interface KcPackageService {
    /**
     * 获取促销套餐列表
     *
     * @param search
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Map> getPromationKcPackageList(String search, Integer pageNum, Integer pageSize);
}
