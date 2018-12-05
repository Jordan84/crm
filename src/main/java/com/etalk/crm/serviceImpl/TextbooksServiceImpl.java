package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.dao.TextBooksMapper;
import com.etalk.crm.dao.TextbooksCategroyMapper;
import com.etalk.crm.pojo.TextbookesCategory;
import com.etalk.crm.pojo.Textbooks;
import com.etalk.crm.service.TextbooksService;
import com.etalk.crm.utils.OssGetObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/10/24 15:13
 * @Version 1.0
 * @Description 教材
 **/
@Service
public class TextbooksServiceImpl implements TextbooksService {
    private static final Logger logger = LogManager.getLogger(TextbooksServiceImpl.class);

    @Resource
    private TextBooksMapper textBooksMapper;
    @Resource
    private TextbooksCategroyMapper textbooksCategroyMapper;

    @Override
    public PageInfo selectTextbooks(Map<String, Object> map, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Textbooks> list = textBooksMapper.selectTextbooks(map);
        // 分页信息
        PageInfo<Textbooks> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo selectTextbooksByThirdId(Integer thirdId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Textbooks> list = textBooksMapper.selectTextbooksByThirdId(thirdId);
        // 分页信息
        PageInfo<Textbooks> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public List<TextbookesCategory> searchTextbooksCategroyBySearch(Map<?, ?> map) {
        return textbooksCategroyMapper.searchTextbooksCategroyBySearch(map);
    }

    @Override
    public PageInfo getTextbooksListByKnowledgeId(Integer knowledgeId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Textbooks> list = textBooksMapper.getTextbooksListByKnowledgeId(knowledgeId);
        // 分页信息
        PageInfo<Textbooks> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public List<Map> showTextbooksPageList(String strUrl) {
        // 教材页码数据
        List<Map> resultList = new ArrayList<>();
        String jsonContent = OssGetObject.getCfgJson(strUrl);
        if (StringUtil.isNotEmpty(jsonContent)) {
            JSONArray jsona = JSON.parseArray(jsonContent);
            for (int i = 0; i < jsona.size(); i++) {
                String str = ((JSONObject) jsona.get(i)).getString("t");
                if ("f".equals(str)) {
                    str = ((JSONObject) jsona.get(i)).getString("n");
                    String page = str.substring(0, str.indexOf("."));
                    // 组装结果
                    Map pageMap = new HashMap();
                    pageMap.put("id", i);
                    pageMap.put("name", page);
                    resultList.add(pageMap);
                }
            }
        }
        return resultList;
    }

    @Override
    public int getTextbooksIdxByPage(String strUrl, String pageName) {
        int idx = -1;
        // 教材页码数据
        String jsonContent = OssGetObject.getCfgJson(strUrl);
        if (StringUtil.isNotEmpty(jsonContent)) {
            JSONArray jsona = JSON.parseArray(jsonContent);
            for (int i = 0; i < jsona.size(); i++) {
                String str = ((JSONObject) jsona.get(i)).getString("t");
                if ("f".equals(str)) {
                    str = ((JSONObject) jsona.get(i)).getString("n");
                    String page = str.substring(0, str.indexOf("."));
                    // 检测索引
                    Map pageMap = new HashMap();
                    if(pageName.equals(page)){
                        idx =i;
                        break;
                    }
                }
            }
        }
        return idx;
    }
}
