package com.etalk.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.QuestionnaireOcService;
import com.etalk.crm.utils.RestResponseDTO;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jordan
 */
@RequestMapping(value = "/questionnaireOc")
@Controller
public class QuestionnaireOcController {
    private static final Logger logger= LogManager.getLogger(QuestionnaireOcController.class);

    @Autowired
    private QuestionnaireOcService questionnaireOcService;

    @Value("${web.crm.old.url}")
    String oldCrmUrl;

    /**
     * 进入oc课课后回访调查
     * @param model
     * @param strStartDate oc课上课开始时间
     * @param strEndDate oc 结束时间
     * @param teacher  上课老师
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/toQuestionnaireOc", method = {RequestMethod.GET, RequestMethod.POST})
    public String toQuestionnaireOc(Model model,String strStartDate, String strEndDate,String teacher,Integer pageNum, Integer pageSize,ModelMap modelMap){
        logger.info(strStartDate);
        logger.info(strEndDate);
        logger.info(teacher);
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageInfo<QuestionnaireOc> pageInfo = questionnaireOcService.questionnaireOcList(strStartDate, strEndDate, teacher,pageNum,pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        List<Person> sscList = questionnaireOcService.selectSSCTeacher();
        if (sscList != null && sscList.size()>0) {
            modelMap.addAttribute("sscList", sscList);
        }
        if(!StringUtils.isEmpty(teacher)){
          modelMap.put("teacher",teacher);
        }
        if(!StringUtils.isEmpty(strStartDate)){
          modelMap.put("strStartDate",strStartDate);
        }
        if(!StringUtils.isEmpty(strEndDate)){
          modelMap.put("strEndDate",strEndDate);
        }
        return  "questionnaire/questionnaire";
    }


    /**
     * 创建问卷
     * @param jsonObject 接收前端数据
     * @param session
     * @return
     * @author James
     */
    @RequestMapping(value = "/createPaper",method = RequestMethod.POST)
    @ResponseBody
    public Object  createPaper(@RequestBody JSONObject jsonObject,HttpSession session){
        //参数打印
        logger.info(jsonObject);
        String paperTitle = "";
        String paperSummary = "";
        if(!StringUtils.isEmpty(jsonObject.get("paperTitle").toString())){
            paperTitle = jsonObject.get("paperTitle").toString();
        }
        if(!StringUtils.isEmpty(jsonObject.get("paperSummary").toString())){
            paperSummary = jsonObject.get("paperSummary").toString();
        }
        Papers papers = new Papers();
        papers.setCreateDate(new Date());
        papers.setPaperCount(0);
        papers.setPaperStatus(0);
        papers.setPaperTitle(paperTitle);
        papers.setPaperSummary(paperSummary);
        //获取当前创建人
        if(session != null){
            Integer loginId = (Integer) session.getAttribute("userid");
            String userName = session.getAttribute("username").toString();
            papers.setAddUserId(loginId);
            papers.setAddUserName(userName);
        }
        Map map = new HashMap();
        String msg = "";
        Integer code = null;
        int result = 0;
        try{
            //创建paper记录
           questionnaireOcService.addPaper(papers);
           //mybatis 从bean获取 paperId
           Integer paperId = papers.getPaperId();
           Integer questionId = null;

           //在问卷表以及选项表插入记录
            if(!StringUtils.isEmpty(jsonObject.get("questionAndOption"))){
                List<QuestionAndOption> questList =(List<QuestionAndOption>) jsonObject.get("questionAndOption");
                List<Map> list =(List) jsonObject.get("questionAndOption");
                for(int i = 0; i<list.size(); i++){
                    QuestionnaireQuestion qq = new QuestionnaireQuestion();
                    qq.setQuestionName(list.get(i).get("questionName").toString());
                    qq.setEndnotes(list.get(i).get("endnotes").toString());
                    qq.setCaption(list.get(i).get("caption").toString());
                    qq.setPaperId(paperId);
                    qq.setState(Integer.parseInt(list.get(i).get("state").toString()));
                    questionnaireOcService.addQuestion(qq);
                    questionId = qq.getQuestionId();
                    List<Map> optionList =(List) list.get(i).get("option");
                    //插入选项记录
                    for(int j = 0;j<optionList.size();j++){
                        QuestionaireOption qo = new QuestionaireOption();
                        qo.setOptionName(optionList.get(j).get("optionName").toString());
                        qo.setOptionValue(Integer.parseInt(optionList.get(j).get("value").toString()));
                        qo.setPaperId(paperId);
                        qo.setQuestionId(questionId);
                        qo.setSort(Integer.parseInt(optionList.get(j).get("sort").toString()));
                        questionnaireOcService.addOption(qo);
                    }
                }
            }
        }catch (Exception e){
            msg = "数据异常";
            code = -1;
            map.put("msg",msg);
            map.put("code",code);
            return JSONObject.toJSON(map);
        }
        msg = "创建成功";
        code = 1;
        map.put("msg",msg);
        map.put("code",code);
        return JSONObject.toJSON(map);
    }

    /**
     *查看用户问卷成绩
     * @param model
     * @param questionnaireId  问卷成绩管理id
     * @return
     */
    @RequestMapping(value = "/checkPapers",method = RequestMethod.GET)
    public Object checkPapers (Model model,Integer questionnaireId){
        //问卷成绩展示表id
        logger.info(questionnaireId);
        Papers papers = new Papers();
        //1.根据id查询  问卷管理记录
        QuestionnaireOc qoc = questionnaireOcService.selectByQCId(questionnaireId);
        Integer paperId = null;
        Integer personId = null;
        Qustionnaire qustionnaire = new Qustionnaire();
        if(qoc != null){
            paperId = qoc.getPaperId();
            personId = qoc.getPersonId();
            papers = questionnaireOcService.selectPapersByPaperId(paperId);
            qustionnaire.setPaperSummary(papers.getPaperSummary());
            qustionnaire.setPaperTitle(papers.getPaperTitle());

        }
        String paperTitle = "";
        List<QuestionAndOption > qustionnaireList= new ArrayList<>();
        List<QuestionnaireQuestion> questList = questionnaireOcService.selectByPaperId(paperId);
        for(QuestionnaireQuestion qu : questList){
            QuestionAndOption op = new QuestionAndOption();
            int quewstionId = qu.getQuestionId();
            List<QuestionaireOption> optionList =  questionnaireOcService.selectByQuestionId(quewstionId);
            if(personId != null){
                QuestionUserAnswer qua = questionnaireOcService.selectByQPId(quewstionId,personId);
                op.setQuestionUserAnswer(qua);
            }
            op.setQuestion(qu);
            op.setOptionList(optionList);
            qustionnaireList.add(op);
        }
        qustionnaire.setQoc(qoc);
        qustionnaire.setQuestionAndOptionsList(qustionnaireList);
        model.addAttribute(qustionnaire);
        return "questionnaire/questionnaire_check";
    }

    /**
     *根据模板id查看模板
     * @param model
     * @param paperId
     * @return
     */
    @SuppressWarnings("all")
    @RequestMapping("/questinnaireInfo")
    public Object questinnaireInfo(Model model ,Integer paperId){
        logger.info(paperId);//参数
        Qustionnaire qustionnaire = new Qustionnaire();
        List<QuestionAndOption > qustionnaireList= new ArrayList<>();
        //根据id  查询模板问题相关信息
        List<QuestionnaireQuestion> questList = questionnaireOcService.selectByPaperId(paperId);
        Papers papers = questionnaireOcService.selectPapersByPaperId(paperId);
        if(papers != null){
            qustionnaire.setPaperTitle(papers.getPaperTitle());
            qustionnaire.setPaperSummary(papers.getPaperSummary());
        }
        for(QuestionnaireQuestion qu : questList){
            QuestionAndOption op = new QuestionAndOption();
            int quewstionId = qu.getQuestionId();
            //根据问题id  查询选项
            List<QuestionaireOption> optionList =  questionnaireOcService.selectByQuestionId(quewstionId);
            op.setQuestion(qu);
            op.setOptionList(optionList);
            qustionnaireList.add(op);
        }
        qustionnaire.setQuestionAndOptionsList(qustionnaireList);
        model.addAttribute("qustionnaire",qustionnaire);
        return "questionnaire/questionnaire_model";
    }


    /**
     *未回访----问卷页面
     * @param modelMap
     * @param lessonId oc课程id
     * @param addUserId  添加oc课ssc的userId
     * @param addUserName  添加人的名字
     * @param cnName  学员中文名
     * @param enName 学员英文名
     * @param personId 学员个人id
     * @param teacher oc课上课老师的名字
     * @param releaseTime oc 课上课时间
     * @return
     */
    @RequestMapping("/questionnairePaper")
    public String questionnairePaper(ModelMap modelMap,Integer lessonId,Integer addUserId,
             String addUserName,String cnName,String enName,Integer personId,String studentLogin,
                                     String teacher,String releaseTime,String sscName) throws ParseException {
        //这些参数都是从oc 问卷调查模板---》问卷调查  带过来
        JSONObject questionParam = new JSONObject();
        QuestionnaireOc oc = new QuestionnaireOc();
        //最后封装的bean对象
        Qustionnaire qustionnaire = new Qustionnaire();
        String loginName = "";
        //1,参数校验
        if(!StringUtils.isEmpty(cnName)){
            loginName = cnName;
        }
        if(!StringUtils.isEmpty(enName)){
            loginName = enName;
        }
        if(!StringUtils.isEmpty(addUserName)){
             questionParam.put("addUserName",addUserName);
        }

        if(!StringUtils.isEmpty(studentLogin)){
             questionParam.put("studentLogin",studentLogin);
        }

        if(!StringUtils.isEmpty(sscName)){
            questionParam.put("sscName",sscName);
        }
        questionParam.put("loginName",loginName);
        oc.setStudentLogin(loginName);
        if(lessonId != null){
            questionParam.put("lessonId",lessonId);
        }
        if(personId != null){
            questionParam.put("personId",personId);
        }
        if(addUserId != null){
            questionParam.put("addUserId",addUserId);
        }
        if(!StringUtils.isEmpty(teacher)){
            questionParam.put("teacher",teacher);
            oc.setTeacher(teacher);
        }

        //参数替换（T）以及和格式化
        if(!StringUtils.isEmpty(releaseTime)){
            questionParam.put("releaseTime",releaseTime.replace("T"," "));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            oc.setReleaseTime(sdf.parse(releaseTime.replace("T"," ")));
        }
        Integer paperId = null;
        //2 从数据库paper表查询当前有效的  问卷模板
        Papers papers = questionnaireOcService.selectByState();
        String paperTitle = "";
        if(papers != null ){
            paperId = papers.getPaperId();
            paperTitle = papers.getPaperTitle();
            questionParam.put("paperId",paperId);
            qustionnaire.setPaperSummary(papers.getPaperSummary());
        }
        //查询问卷  题目以及选项

        List<QuestionAndOption > qustionnaireList= new ArrayList<>();
        List<QuestionnaireQuestion> questList = questionnaireOcService.selectByPaperId(paperId);
        for(QuestionnaireQuestion qu : questList){
            QuestionAndOption op = new QuestionAndOption();
            int quewstionId = qu.getQuestionId();
            List<QuestionaireOption> optionList =  questionnaireOcService.selectByQuestionId(quewstionId);
            op.setQuestion(qu);
            op.setOptionList(optionList);
            qustionnaireList.add(op);
        }
        //3放入一个对象封装
        qustionnaire.setPaperId(paperId);
        qustionnaire.setPaperTitle(paperTitle);
        qustionnaire.setQoc(oc);
        qustionnaire.setQuestionAndOptionsList(qustionnaireList);
        qustionnaire.setQuestionParam(questionParam);
        modelMap.addAttribute(qustionnaire);
        modelMap.addAttribute("oldCrmUrl",oldCrmUrl);
        //4问卷答题页面
        return "questionnaire/questionnaire_add_grade";
    }


    /**
     * 提交问卷成绩
     * @param session
     * @param jsonObject
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/addgrade",method = RequestMethod.POST)
    @ResponseBody
    public Object  addGrade(HttpSession session,@RequestBody JSONObject jsonObject){
        //查看参数，获取session 参数校验
        logger.info(jsonObject);
        Map<String,Object>map = new HashMap();
        int result = questionnaireOcService.addQoc(session,jsonObject);
        String msg = "";
        Integer code = null;
        if(result == 1){
           msg = "提交成功";
           code = 1;
        }else if(result == -1){
            msg = "数据已提交，请勿重复提交";
            code = -1;
        }else{
            msg = "数据提交失败";
            code = 0;
        }
        map.put("msg",msg);
        map.put("code",code);
        return JSONObject.toJSON(map);
    }


    /**
     * 问卷列表页面
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/toPapersdList")
    public String checkQuestionnaire(Model model,Integer pageNum,Integer pageSize){
         if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageInfo<Papers> pageInfo = questionnaireOcService.paperList(pageNum,pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute(pageInfo);
        return  "questionnaire/paperList";
    }

    /**
     *添加问卷调查页面
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/toaddPapers")
    public String toaddPapers(Model model,Integer pageNum,Integer pageSize){
        return  "questionnaire/questionnaire_edit";
    }

    /**
     * 更新问卷状态时生效的问卷只有一张
     * @param paperId
     * @return
     */
    @RequestMapping("/updataPaperById")
    @ResponseBody
    public RestResponseDTO updataPaperById(@RequestBody Integer paperId){
        logger.info(paperId);
        RestResponseDTO dto = new RestResponseDTO();
        if(paperId == null){
            dto.setMsg("参数错误");
            dto.setStatus(0);
            return dto;
        }
        try {
           questionnaireOcService.updatePaper(paperId);
        }catch (Exception e){
            e.printStackTrace();
            dto.setMsg("系统异常");
            dto.setStatus(0);
            return dto;
        }
        dto.setMsg("更新成功");
        dto.setStatus(1);
        return dto;
    }


    /**
     * 导出问卷调查管理
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    @RequestMapping(value = "/exportExcel")
    public String getUser(HttpServletResponse response,String strStartDate, String strEndDate,String teacher) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("oc课课后回访问卷调查统计表");
        createTitle(workbook,sheet);
        List<QuestionnaireOc> rows = questionnaireOcService.exportData(strStartDate,strEndDate,teacher);
        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd hh:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum=1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        for(QuestionnaireOc qoc:rows){
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(qoc.getStudentLogin());
            row.createCell(1).setCellValue(qoc.getTeacher());
            row.createCell(2).setCellValue(sdf.format(qoc.getReleaseTime()));
            row.createCell(3).setCellValue(qoc.getAddUserName());
            row.createCell(4).setCellValue(sdf.format(qoc.getCreateTime()));
            row.createCell(5).setCellValue(String.valueOf(qoc.getGrade()));
            rowNum++;
        }

        String fileName = "oc课课后回访问卷调查统计表.xls";

        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);

        return "download excel";
    }

    /**
     * 浏览器下载excel
     * @param filename
     * @param workbook
     * @param response
     * @throws Exception
     */
    protected void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8")+ ".xls");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workbook.close();
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     *创建表头
     * @param workbook
     * @param sheet
     */
    private void createTitle(HSSFWorkbook workbook,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(3,17*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("上课学员");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("上课老师");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("上课时间");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("提交人");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("提交时间");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("平均分");
        cell.setCellStyle(style);
    }

}
