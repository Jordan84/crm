package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.client.InvokingService;
import com.etalk.crm.dao.ActivityGiveMapper;
import com.etalk.crm.dao.ActivityInfoMapper;
import com.etalk.crm.dao.KcPackageMapper;
import com.etalk.crm.dao.PersonMapper;
import com.etalk.crm.pojo.ActivityGive;
import com.etalk.crm.pojo.ActivityGiveLog;
import com.etalk.crm.pojo.KcPackage;
import com.etalk.crm.pojo.RecommendUsers;
import com.etalk.crm.service.ActivityGiveService;
import com.etalk.crm.service.KcOrdersService;
import com.etalk.crm.utils.ExcelImportUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/28 18:03
 * @Version 1.0
 * @Description 活动赠送
 **/
@Service
public class ActivityGiveServiceImpl implements ActivityGiveService {
    private static final Logger logger = LogManager.getLogger(ActivityGiveServiceImpl.class);

    @Resource
    private ActivityGiveMapper activityGiveMapper;
    @Resource
    private ActivityInfoMapper activityInfoMapper;
    @Resource
    private KcOrdersService kcOrdersService;
    @Resource
    private InvokingService invokingService;
    @Resource
    private PersonMapper personMapper;
    @Resource
    private KcPackageMapper kcPackageMapper;

    @Override
    public PageInfo<ActivityGive> getActivityGiveList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityGive> list = activityGiveMapper.getActivityGiveList();
        // 分页信息
        PageInfo<ActivityGive> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public Map batchImport(String fileName, MultipartFile mfile) {
        Map resultMap = new HashMap();
        List<String> names = new ArrayList<>();

        InputStream is = null;
        try {
            is = mfile.getInputStream();

            Workbook workbook = null;
            //根据版本选择创建Workbook的方式
            //根据文件名判断文件是2003版本还是2007版本
            Sheet sheet = null;
            if (ExcelImportUtils.isExcel2007(fileName)) {
                // 针对2007 Excel 文件
                workbook = new XSSFWorkbook(is);
                sheet = workbook.getSheetAt(0);
            } else {
                // 针对 2003 Excel 文件
                workbook = new HSSFWorkbook(new POIFSFileSystem(is));
                sheet = workbook.getSheetAt(0);
            }
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 0; j < physicalNumberOfRows; j++) {
                if (j == 0) {
                    continue;//标题行
                }
                Row row = sheet.getRow(j);
                if (row.getCell(0) != null) {
                    row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    Cell cell = row.getCell(0);
                    String name = cell.getStringCellValue();
                    if (null != name && !StringUtils.isEmpty(name.trim()) && !names.contains(name.trim())) {
                        names.add(name.trim());
                    }
                }
            }

            logger.debug("data:" + JSON.toJSONString(names));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    logger.error(e.getMessage(), e);
                }
            }
        }

        resultMap.put("msg", "导入出错！请检查数据格式！");
        resultMap.put("data", names);
        return resultMap;
    }

    @Override
    public List<String> checkStudentExists(List<String> names) {
        return activityGiveMapper.checkStudentExists(names);
    }

    @Transactional
    @Override
    public boolean publishActivityGive(ActivityGive activityGive, List<String> names, HttpSession session) {
        boolean flag = false;
        try {
            int userId = (int) session.getAttribute("userid");
            // 添加活动记录
            int count = activityGiveMapper.insertActivityGive(activityGive);

            if (count > 0) {
                // 赠送课时并发送通知
                int isGift = (activityGive.getIsGift() == null) ? 0 : activityGive.getIsGift();
                Integer kcPackageId = activityGive.getGift_primary_key();
                KcPackage kcPackage = kcPackageMapper.getPackegeInfoById(kcPackageId);
                // 获取课时
                int lessonCount = 0;
                // 有课时的情况
                if (kcPackageId != null) {
                    lessonCount = kcPackage.getLessonCount() + (null == kcPackage.getGift() ? 0 : kcPackage.getGift());
                    kcOrdersService.addKcOrdersFromActivity(names, activityGive.getPackageType(), kcPackage, activityGive.getId(), activityGive.getActivity(), isGift);
                }
                // 批量查询推荐人
                List<RecommendUsers> uidList = personMapper.selectPersonByLoginNameList(names);
                // 调用市场运营接口
                logger.info("uidList:" + JSON.toJSONString(uidList) + ",activityId:" + activityGive.getActivity_primary_key() + ",hours:" + lessonCount + ",isGift:" + isGift);
                invokingService.privilegeAndHours(JSON.toJSONString(uidList), activityGive.getActivity_primary_key(), lessonCount, isGift);

                // 批量添加学员赠送记录
                for (String name : names) {
                    // 添加赠送记录
                    ActivityGiveLog activityGiveLog = new ActivityGiveLog();
                    activityGiveLog.setAgId(activityGive.getId());
                    activityGiveLog.setActivityId(activityGive.getActivity_primary_key());
                    activityGiveLog.setReferrerId(userId);
                    activityGiveLog.setLoginName(name);
                    activityGiveMapper.insertActivityGiveLog(activityGiveLog);
                }
            }
            flag = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 异常手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return flag;
    }

    @Override
    public PageInfo<Map> getActivityList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list = activityInfoMapper.getActivityList(search);
        // 分页信息
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo<ActivityGiveLog> getActivityGiveLogList(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityGiveLog> list = activityGiveMapper.getActivityGiveLogList(search);
        // 分页信息
        PageInfo<ActivityGiveLog> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，搜索关键字：search=" + search);
        return pageInfo;
    }

    @Override
    public PageInfo<Map> getActivityGiveUsers(Integer agId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list = activityGiveMapper.getActivityGiveUsers(agId);
        // 分页信息
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize + "，agId=" + agId);
        return pageInfo;
    }

    @Override
    public List<Map> getActivityGiveUsersForExport(Integer agId) {
        List<Map> list = activityGiveMapper.getActivityGiveUsers(agId);
        return list;
    }
}
