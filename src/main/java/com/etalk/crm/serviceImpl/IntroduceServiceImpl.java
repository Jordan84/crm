package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.IntroduceGiftMapper;
import com.etalk.crm.pojo.IntroduceGift;
import com.etalk.crm.pojo.IntroduceGiftLog;
import com.etalk.crm.service.IntroduceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/28 15:45
 * @Version 1.0
 * @Description 转介绍
 **/
@Service
public class IntroduceServiceImpl implements IntroduceService {
    private static final Logger logger = LogManager.getLogger(IntroduceServiceImpl.class);

    @Resource
    private IntroduceGiftMapper introduceGiftMapper;

    @Override
    public IntroduceGift getIntroduceGiftById(Integer id) {
        return introduceGiftMapper.selectIntroduceGiftById(id);
    }

    @Override
    public PageInfo<IntroduceGift> getIntroduceGiftList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IntroduceGift> list = introduceGiftMapper.getIntroduceGiftList(search);
        // 分页信息
        PageInfo<IntroduceGift> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Transactional
    @Override
    public Integer insertIntroduceGift(IntroduceGift introduceGift) {
        Integer num = introduceGiftMapper.insertIntroduceGift(introduceGift);
        if (num > 0) {
            return introduceGift.getId();
        }
        return -1;
    }

    @Override
    public boolean updateIntroInfo(IntroduceGift introduceGift) {
        int num = introduceGiftMapper.updateIntroduceGiftById(introduceGift);
        if (num > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateIntroState(Integer state, Integer introId) {
        // 更改状态
        IntroduceGift introduceGift = new IntroduceGift();
        introduceGift.setId(introId);
        introduceGift.setState(state);
        introduceGiftMapper.updateIntroduceGiftById(introduceGift);
        if (state == 1) {
            // 其余重置为禁用
            introduceGiftMapper.updateIntroduceGiftExludeId(introduceGift.getId());
        }
        return true;
    }

    @Override
    public PageInfo<IntroduceGiftLog> getIntroduceGiftLogList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IntroduceGiftLog> list = introduceGiftMapper.getIntroduceGiftLogList(search);
        // 分页信息
        PageInfo<IntroduceGiftLog> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：search=" + search);
        return pageInfo;
    }

    @Override
    public PageInfo<Map> selectIntroduceNodeList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list = introduceGiftMapper.selectIntroduceNodeList(search);
        // 分页信息
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }
}
