package com.etalk.crm.controller;

import com.etalk.crm.pojo.ActivityGive;
import com.etalk.crm.pojo.ActivityGiveLog;
import com.etalk.crm.service.ActivityGiveService;
import com.etalk.crm.service.KcPackageService;
import com.etalk.crm.utils.Constants;
import com.etalk.crm.utils.ExcelImportUtils;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/26 11:13
 * @Version 1.0
 * @Description 活动赠送
 **/
@Controller
@RequestMapping("/activity")
public class ActivityGiveController {
    private static final Logger logger = LogManager.getLogger(ActivityGiveController.class);

    @Resource
    private ActivityGiveService activityGiveService;
    @Resource
    private KcPackageService kcPackageService;

    @RequestMapping("/give/page/list")
    public String listActivityGive(Model model, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<ActivityGive> pageInfo = activityGiveService.getActivityGiveList(pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "activityGive/activity_list";
    }

    @RequestMapping("/give/page/student/import")
    public String importActivityStudent(HttpSession session) {
        session.removeAttribute("existPersons");
        return "activityGive/activity_student_import";
    }

    @RequestMapping("/give/checkFile")
    public String checkFile(Model model, @RequestParam(value = "filename") MultipartFile file, HttpSession session) {
        // 判断文件是否为空
        if (file == null) {
            model.addAttribute("msg", "文件不能为空！");
            return "activityGive/activity_student_import";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.debug("文件名：" + fileName);
        if (StringUtils.isEmpty(fileName)) {
            model.addAttribute("msg", "文件不能为空！");
            return "activityGive/activity_student_import";
        }

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            model.addAttribute("msg", "文件必须是excel格式！");
            return "activityGive/activity_student_import";
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            model.addAttribute("msg", "文件不能为空！");
            return "redirect:toUserKnowledgeImport";
        }

        //批量导入
        Map resultMap = activityGiveService.batchImport(fileName, file);
        logger.debug("msg:", resultMap.get("msg"));
        List<String> names = (List<String>) resultMap.get("data");
        //检测名字是否存在
        List<String> existPersons = activityGiveService.checkStudentExists(names);
        //获取不存在的用户
        List<String> notExistPersons = new ArrayList<>(names);
        notExistPersons.removeAll(existPersons);
        //供前台展示
        model.addAttribute("notExistPersons", notExistPersons);
        model.addAttribute("existPersons", existPersons);
        model.addAttribute("count", names.size());
        model.addAttribute("errorCount", names.size() - existPersons.size());
        if (names.size() == existPersons.size()) {//待导入的用户全部合法
            session.setAttribute("existPersons", existPersons);
            model.addAttribute("tips", "检测成功，请重新选择excel文件并点击【下一步】开始上传");
        } else {//导图数据有误，清空缓存
            session.removeAttribute("existPersons");
        }
        return "activityGive/activity_student_import";
    }

    @RequestMapping("/give/importStudent")
    public String importStudent(HttpSession session) {
        //检测待导入用户是否合法
        List<String> existPersons = (List<String>) session.getAttribute("existPersons");
        if (CollectionUtils.isEmpty(existPersons)) {
            return "activityGive/activity_student_import";
        }
        return "activityGive/activity_choose_gift";
    }

    @RequestMapping("/give/page/student/list")
    public String listActivityStudent(Model model, Integer agId, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<Map> pageInfo = activityGiveService.getActivityGiveUsers(agId, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "activityGive/activity_student_list";
    }

    @RequestMapping("/give/page/choose/gift")
    public String chooseGift() {
        return "activityGive/activity_choose_gift";
    }

    @RequestMapping("/give/page/notice/send")
    public String sendNotice(Model model, Integer activity_primary_key, Integer gift_primary_key, Integer isGift, String giftDesc, Integer packageType) {
        model.addAttribute("activityId", activity_primary_key);
        model.addAttribute("gift", gift_primary_key);
        model.addAttribute("isGift", isGift);
        model.addAttribute("giftDesc", giftDesc);
        model.addAttribute("packageType", packageType);
        return "activityGive/activity_notice_send";
    }

    @RequestMapping("give/page/notice/show/{id}")
    public String showNotice(Model model, @PathVariable Integer id) {
        initPage(model, id);
        return "activityGive/activity_notice_show";
    }

    @RequestMapping("/log/page/list")
    public String listActivityLog(Model model, String search, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<ActivityGiveLog> pageInfo = activityGiveService.getActivityGiveLogList(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "activityGiveLog/activity_log_list";
    }

    private void initPage(Model model, Integer id) {
        model.addAttribute("id", id);
    }

    @RequestMapping("give/submit")
    @ResponseBody
    public RestResponseDTO submit(ActivityGive activityGive, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("导入学员添加活动赠礼并发送通知");
            if(activityGive.getGift_primary_key()==null && activityGive.getIsGift() ==null){
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("必须选择课时或者礼品");
                return restResponseDTO;
            }
            boolean flag = false;
            //发布活动记录
            List<String> names = (List<String>) session.getAttribute("existPersons");
            flag = activityGiveService.publishActivityGive(activityGive, names, session);
            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error("活动赠送发布异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    @RequestMapping("give/selectActivity")
    @ResponseBody
    public Map selectActivity(HttpServletRequest request, Integer page_num, Integer per_page) {
        List resultList = new ArrayList();
        if (page_num == null) {
            page_num = Constants.DEFAULT_PAGE_NUM;
        }
        if (per_page == null) {
            per_page = Constants.DEFAULT_PAGE_SIZE;
        }
        String q_word = request.getParameter("q_word[]");
        PageInfo<Map> pageInfo = activityGiveService.getActivityList(q_word, page_num, per_page);
        if (pageInfo != null) {
            resultList = pageInfo.getList();
        }

        Map resultMap = new HashMap();
        resultMap.put("result", resultList);
        resultMap.put("cnt_whole", pageInfo.getTotal());
        return resultMap;
    }

    @RequestMapping("give/selectKcPackege")
    @ResponseBody
    public Map selectKcPackege(HttpServletRequest request, Integer page_num, Integer per_page) {
        List resultList = new ArrayList();
        if (page_num == null) {
            page_num = Constants.DEFAULT_PAGE_NUM;
        }
        if (per_page == null) {
            per_page = Constants.DEFAULT_PAGE_SIZE;
        }
        String q_word = request.getParameter("q_word[]");
        PageInfo<Map> pageInfo = kcPackageService.getPromationKcPackageList(q_word, page_num, per_page);
        if (pageInfo != null) {
            resultList = pageInfo.getList();
        }

        Map resultMap = new HashMap();
        resultMap.put("result", resultList);
        resultMap.put("cnt_whole", pageInfo.getTotal());
        return resultMap;
    }

    /**
     * 导出赠送学员到Excel
     *
     * @param model
     * @param agId
     * @return
     */
    @RequestMapping(value = "/student/exportExcel")
    @ResponseBody
    public String exportActivityStudent(Model model, Integer agId, HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("赠送学员名单");
        createTitle(workbook, sheet);

        List<Map> rows = activityGiveService.getActivityGiveUsersForExport(agId);

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd hh:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (Map sc : rows) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(sc.get("loginName").toString());
            rowNum++;
        }

        String fileName = "赠送学员名单.xls";

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
        cell.setCellValue("学员登录名");
        cell.setCellStyle(style);
    }
}
