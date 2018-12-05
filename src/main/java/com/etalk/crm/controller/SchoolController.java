package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.SchoolCollectDate;
import com.etalk.crm.pojo.SchoolCollectInfo;
import com.etalk.crm.pojo.SchoolCollectTime;
import com.etalk.crm.pojo.SchoolCollections;
import com.etalk.crm.pojo.SchoolInfo;
import com.etalk.crm.service.SchoolService;
import com.etalk.crm.utils.Constants;
import com.etalk.crm.utils.RestResponseDTO;
import com.etalk.crm.utils.RestResponseStates;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Terewr
 * @date 2018/05/29
 */
@Controller
@RequestMapping("/school")
public class SchoolController {
    private static final Logger logger = LogManager.getLogger(SchoolController.class);


    @Resource
    private SchoolService schoolService;

    @RequestMapping("/page/edit")
    public String addSchoolCollectionInfo(Model model) {
        // 初始化页面数据
        initPage(model, 0);
        return "school/school_collect_edit";
    }

    @RequestMapping("/page/edit/{collectId}")
    public String editSchoolCollectionInfo(Model model, @PathVariable("collectId") Integer collectId) {
        initPage(model, collectId);
        return "school/school_collect_edit";
    }

    @RequestMapping("/page/list")
    public String listSchoolCollectionInfo(Model model, String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<SchoolCollectInfo> pageInfo = schoolService.getSchoolCollectInfoList(search, province, city, area, schoolId, effect, hasPolice, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        Map searchParam = new HashMap();
        searchParam.put("search", search);
        searchParam.put("province", province);
        searchParam.put("city", city);
        searchParam.put("area", area);
        searchParam.put("schoolId", schoolId);
        searchParam.put("effect", effect);
        searchParam.put("hasPolice", hasPolice);
        model.addAttribute("searchParam", searchParam);
        return "school/school_collect_list";
    }

    @RequestMapping(value = "/city", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getCityData() {
        List<Map<String, Object>> resultList = schoolService.getCityData();

        LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap<String>();
        for (Map<String, Object> m : resultList) {
            map.put(m.getOrDefault("areaCode", 0).toString(), m.getOrDefault("cnName", ""));
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/list/{areaCode}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO getSchoolInfo(@PathVariable("areaCode") Integer areaCode) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("查询学校信息，areaCode为：" + areaCode);
            List<SchoolInfo> schoolInfoList = schoolService.selectSchoolByAreaCode(areaCode);
            restResponseDTO.setData(schoolInfoList);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("题目信息查询异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 校验学校名称的唯一性
     *
     * @param schoolName
     * @return
     */
    @RequestMapping(value = "/checkSchoolName", produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO checkSchoolName(String schoolName) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("校验学校名称，schoolName为：" + schoolName);
            boolean isExist = schoolService.checkSchoolName(schoolName);
            Map resultMap = new HashMap();
            resultMap.put("result", isExist);
            restResponseDTO.setData(resultMap);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("题目信息查询异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 初始化学校采集信息
     *
     * @param formData
     * @return
     */
    @RequestMapping(value = "/collect/submit", produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO initSchoolCollectInfo(SchoolCollections formData) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("学校采集信息初始化，formData：" + JSON.toJSONString(formData));

            SchoolCollectInfo schoolCollectInfo = formData.getModels().get(0);
            //添加的时候需要校验学校名称的唯一性
            if (schoolCollectInfo.getId() == null || schoolCollectInfo.getId() == 0) {
                if (!schoolService.checkSchoolName(schoolCollectInfo.getSchoolInfo().getSchoolName())) {
                    restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                    restResponseDTO.setMsg("该学校名称已存在");
                    return restResponseDTO;
                }
            }

            boolean flag = schoolService.initSchoolColectInfo(schoolCollectInfo);
            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error("学校采集信息初始化异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 修改时初始化页面
     *
     * @param model
     * @param collectId
     */
    private void initPage(Model model, Integer collectId) {
        SchoolCollectInfo schoolCollectInfo = new SchoolCollectInfo();
        if (collectId != null && collectId > 0) {
            schoolCollectInfo = schoolService.selectByCollectId(collectId);
        } else {
            List<SchoolCollectTime> defaultCollectTime = new ArrayList<>();
            schoolCollectInfo.setCollectTime(defaultCollectTime);
            List<SchoolCollectDate> defaultCollectDate = new ArrayList<>();
            schoolCollectInfo.setCollectDate(defaultCollectDate);
        }

        //补全学校信息
        SchoolInfo schoolInfo = schoolService.selectSchoolInfoById(schoolCollectInfo.getSchoolId());
        if (null != schoolInfo) {
            schoolCollectInfo.setSchoolInfo(schoolInfo);
        }

        //补全空白
        int currentTimeSize = schoolCollectInfo.getCollectTime().size();
        for (int i = 0; i < 3 - currentTimeSize; i++) {
            SchoolCollectTime collectTime = new SchoolCollectTime();
            schoolCollectInfo.getCollectTime().add(collectTime);
        }

        int currentDateSize = schoolCollectInfo.getCollectDate().size();
        for (int j = 0; j < 5 - currentDateSize; j++) {
            SchoolCollectDate schoolCollectDate = new SchoolCollectDate();
            schoolCollectInfo.getCollectDate().add(schoolCollectDate);
        }

        model.addAttribute("collectId", collectId);
        model.addAttribute("collectInfo", schoolCollectInfo);
    }

    /**
     * 导出市场部已推广学校详情表
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportExcel")
    public @ResponseBody
    String exportExcel(HttpServletResponse response, String search, String province, String city, String area, Integer schoolId, Integer effect, Integer hasPolice) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场部已推广学校详情表");
        createTitle(workbook, sheet);

        List<SchoolCollectInfo> rows = schoolService.getSchoolCollectInfoListToExport(search, province, city, area, schoolId, effect, hasPolice);

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd hh:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (SchoolCollectInfo sc : rows) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(sc.getAreaName());
            row.createCell(1).setCellValue(sc.getSchoolName());
            //组装采集时间
            StringBuffer sb = new StringBuffer();
            List<SchoolCollectTime> timeList = sc.getCollectTime();
            for (int i = 0; i < timeList.size(); i++) {
                SchoolCollectTime time = timeList.get(i);
                if (!StringUtils.isEmpty(time)) {
                    sb.append(sdf.format(time.getCollectTime()));
                    if (i < timeList.size() - 1) {
                        sb.append("-");
                    }
                }
            }
            row.createCell(2).setCellValue(sb.toString());
            row.createCell(3).setCellValue(((null != sc.getHasPolice() && sc.getHasPolice() == 1) ? "有" : "无"));
            row.createCell(4).setCellValue((sc.getEffect() == 1 ? "好" : sc.getEffect() == 2 ? "一般" : "差"));
            row.createCell(5).setCellValue(sc.getSubway());
            row.createCell(6).setCellValue(sc.getSchoolDesc());
            SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
            HSSFCell cell7 = row.createCell(7);
            if (sc.getCollectDate().size() >= 1) {
                cell7.setCellValue(dateSdf.format(sc.getCollectDate().get(0).getCollectDate()));
            }
            HSSFCell cell8 = row.createCell(8);
            if (sc.getCollectDate().size() >= 2) {
                cell8.setCellValue(dateSdf.format(sc.getCollectDate().get(1).getCollectDate()));
            }
            HSSFCell cell9 = row.createCell(9);
            if (sc.getCollectDate().size() >= 3) {
                cell9.setCellValue(dateSdf.format(sc.getCollectDate().get(2).getCollectDate()));
            }
            HSSFCell cell10 = row.createCell(10);
            if (sc.getCollectDate().size() >= 4) {
                cell10.setCellValue(dateSdf.format(sc.getCollectDate().get(3).getCollectDate()));
            }
            HSSFCell cell11 = row.createCell(11);
            if (sc.getCollectDate().size() >= 5) {
                cell11.setCellValue(dateSdf.format(sc.getCollectDate().get(4).getCollectDate()));
            }
            rowNum++;
        }

        String fileName = "市场部已推广学校详情表.xls";

        //生成excel文件
        buildExcelFile(fileName, response, workbook);

        return "download excel";
    }

    /**
     * 生成excel文件
     *
     * @param filename
     * @param workbook
     * @throws Exception
     */
    protected void buildExcelFile(String filename, HttpServletResponse response, HSSFWorkbook workbook) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * 创建表头
     *
     * @param workbook
     * @param sheet
     */
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(3, 17 * 256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("地点");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("学校");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("采集时间");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("有无保安驱赶");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("效果");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("最近地铁站");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("学校情况备注");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("采集日期1");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("采集日期2");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("采集日期3");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("采集日期4");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("采集日期5");
        cell.setCellStyle(style);
    }

}
