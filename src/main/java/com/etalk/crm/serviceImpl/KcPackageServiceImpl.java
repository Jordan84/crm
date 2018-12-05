package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.KcPackageMapper;
import com.etalk.crm.service.KcPackageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/8 18:25
 * @Version 1.0
 * @Description 套餐
 **/
@Service
public class KcPackageServiceImpl implements KcPackageService {
    private static final Logger logger = LogManager.getLogger(KcPackageServiceImpl.class);

    @Resource
    private KcPackageMapper kcPackageMapper;

    @Override
    public PageInfo<Map> getPromationKcPackageList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list = kcPackageMapper.getPromationKcPackageList(search);
        // 分页信息
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;    }
}
