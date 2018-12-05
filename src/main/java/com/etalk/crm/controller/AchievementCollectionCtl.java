package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.client.InvokingService;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.AchievementManagementService;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.service.UserService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/11/1 10:29
 * @Description:
 */
@Controller
@RequestMapping(value = "/achievementCollectionManager", produces = "application/json; charset=utf-8")
public class AchievementCollectionCtl {
    protected static final Logger logger = LogManager.getLogger(AchievementManagementService.class);

    @Resource
    private AchievementManagementService achievementManagementService;

    @Resource
    private UserService userService;

    @Resource
    private InvokingService invokingService;

    @Resource
    private QuestionService questionService;

    /**
     * 功能描述:成绩 分页收集列表
     * @param:
     * @return:
     * @auther: James
     * @date:  2018/10/8 15:03
     */
    @RequestMapping("/achievementManagementList")
    public String achievementManagementList(Model model,Integer pageNum, Integer pageSize
            ,Integer ccId,Integer sscId,String time,String startDate,String endDate,String studentLogin){
        logger.info("pageNum:"+pageNum);
        logger.info("pageSize:"+pageSize);
        List<Integer> ccSscRoleList = new ArrayList<>();
        /*查询SSC列表*/
        logger.info("查询SSC列表");
        ccSscRoleList.add(8);
        ccSscRoleList.add(9);
        ccSscRoleList.add(15);
		List<User> sscList = userService.ccSscList(ccSscRoleList);
		model.addAttribute("sscList",sscList);
        ccSscRoleList.clear();
		logger.info("查询cc列表");
		//查询cc列表
	    ccSscRoleList.add(2);
	    ccSscRoleList.add(10);
	    ccSscRoleList.add(12);
	    ccSscRoleList.add(14);
	    List<User> ccList = userService.ccSscList(ccSscRoleList);
	    model.addAttribute("ccList",ccList);
		logger.info("成绩分页数据");
		if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        AchievementManagement achievementManagement = new AchievementManagement();
        achievementManagement.setSscId(sscId==null?0:sscId);
        achievementManagement.setCcId(ccId==null?0:ccId);
        achievementManagement.setTime(time);
        achievementManagement.setStartDate(startDate);
        achievementManagement.setEndDate(endDate);
        achievementManagement.setStudentLogin(studentLogin);
        PageInfo<AchievementManagement> pageInfo = achievementManagementService.achievementList(pageNum,pageSize,achievementManagement);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageSize",pageSize);
        //参数  放入
        model.addAttribute("achievementManagement",achievementManagement);
        return "achievementManagement/achievement_management";
    }

    /**
     * 学员成绩添加页面
     * @return
     */
    @RequestMapping("/addAchievement")
    public String addAchievement(Model model){
        //等级
        List<GradeLevel> gradeTypeList = achievementManagementService.gradeTypeList();
        model.addAttribute("gradeTypeList",gradeTypeList);
        //添加年级
        List<QuestionGrade> gradeList = questionService.selectQGradeList();
        model.addAttribute("gradeList",gradeList);
        return "achievementManagement/addAchievement";
    }

    /**
     * 功能描述:学员成绩信息 编辑页面
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-12
     */
    @RequestMapping("/achievementEdit")
    public String achievementEdit(int id,Model model){
        logger.info("学员成绩信息 编辑页面");
        logger.info("id:"+id);
        AchievementManagement achievementManagement = achievementManagementService.selectById(id);
        List<GradeLevel> gradeTypeList = achievementManagementService.gradeTypeList();
        model.addAttribute("gradeTypeList",gradeTypeList);
        model.addAttribute("achievementManagement",achievementManagement);
         //添加年级
        List<QuestionGrade> gradeList = questionService.selectQGradeList();
        model.addAttribute("gradeList",gradeList);
        return "achievementManagement/achievement_edit";
    }

    /**
     * 功能描述:添加学员成绩相关信息
     * @param:
     * @return:
     * @auther: james
     * @date:  2018-10-10
     */
    @RequestMapping(value = "/ajaxAddAchievement",method = RequestMethod.POST)
    @ResponseBody
    public  String ajaxAddAchievement(@RequestBody AchievementManagement achievementManagement,HttpSession session){
        logger.info("添加学员成绩相关信息");
        logger.info(achievementManagement.toString());
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        achievementManagement = pubFunction(achievementManagement,session);
        logger.info("添加学员成绩相关信息");
        int isExist = achievementManagementService.isAchievementExist(achievementManagement);
        if(isExist == 0){
            int result = achievementManagementService.addAchievement(achievementManagement);
            if(result >0){
                logger.info("添加操作成功");
                jsonMap.put("code", 1);
                jsonMap.put("msg", "添加操作成功");
            }else{
                logger.info("添加操作失败");
                jsonMap.put("code", -1);
                jsonMap.put("msg", "添加操作成功");
            }
        }else{
            logger.info("该学生在该学期成绩已存在");
            jsonMap.put("code", 0);
            jsonMap.put("msg", "该学生在该学期成绩已存在");
        }
        return JSONObject.toJSONString(jsonMap);
    }


    /**
     * 功能描述:提交  编辑学员成绩相关信息
     * @param:
     * @return:
     * @auther: james
     * @date:  2018-10-10
     */
    @RequestMapping(value = "/ajaxAchievementEdit",method = RequestMethod.POST)
    @ResponseBody
    public  String ajaxAchievementEdit(@RequestBody AchievementManagement achievementManagement,HttpSession session){
        logger.info("提交编辑学员成绩相关信息");
        logger.info(achievementManagement.toString());
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        achievementManagement = pubFunction(achievementManagement,session);
        logger.info("提交编辑学员成绩相关信息");
        int isExist = achievementManagementService.isAchievementExist(achievementManagement);
        if(isExist == 0){
            int result = achievementManagementService.ajaxachievementEdit(achievementManagement);
            if(result >0){
                logger.info("编辑操作成功");
                jsonMap.put("code", 1);
                jsonMap.put("msg", "编辑操作成功");
            }else{
                logger.info("编辑操作失败");
                jsonMap.put("code", -1);
                jsonMap.put("msg", "编辑操作失败");
            }
        }else{
            logger.info("该学生在该学期成绩已存在");
            jsonMap.put("code", 0);
            jsonMap.put("msg", "该学生在该学期成绩已存在");
        }
       return JSONObject.toJSONString(jsonMap);
    }

     /**
     * 功能描述:学员成绩相关信息 添加 编辑  处理 公共方法
     * @param:
     * @return:
     * @auther: james
     * @date:  2018-10-12
     */
    private AchievementManagement pubFunction(AchievementManagement achievementManagement, HttpSession session){
        logger.info("调用achievementManagement 公共处理方法");
        if(session != null){
            achievementManagement.setAddUserId(Integer.parseInt(session.getAttribute("userid").toString()));
        }
        if(StringUtil.isNotEmpty(achievementManagement.getTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            try {
                achievementManagement.setYear(sdf.parse(achievementManagement.getTime()));
            } catch (ParseException e) {
                logger.info(e.getMessage());
            }
        }
        //分数 分类
        if(achievementManagement.getTotalScore() <5){
            achievementManagement.setScoreType(1);
        }else{
            achievementManagement.setScoreType(2);
        }
        return achievementManagement;
    }

    /**
     * 功能描述:
     * @param: 删除  某条 学员成绩
     * @return:
     * @auther: James
     * @date:  2018-10-10
     */
    @RequestMapping(value = "/delAchievement",method = RequestMethod.POST)
    @ResponseBody
    public String delAchievement(int id){
        logger.info("删除某条 学员成绩");
        logger.info("参数id:"+id);
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        int result = achievementManagementService.delAchievementManagement(id);
        if(result >0){
            jsonMap.put("code", 1);
        }else{
            jsonMap.put("code", -1);
        }
        return JSONObject.toJSONString(jsonMap);
    }

     /**
     * 下拉学员列表
     * @param
     * @param搜索关键字：+search
     * @return
     */
    @RequestMapping(value = "/personList",method = RequestMethod.POST)
    @ResponseBody
    public String  personList(String search){
        logger.info("加载下拉学员列表");
        logger.info("搜索关键字："+search);
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        List<Person> personList = achievementManagementService.personList(search);
        jsonMap.put("personList",personList);
        jsonMap.put("code", 1);
        return JSONObject.toJSONString(jsonMap);
    }

    /*
     * @Author James
     * @Description 添加 微信成绩收集 消息列表
     * @Date 15:01 2018/10/30
     * @Param
     * @return
     **/
    @RequestMapping(value = "/addWechatAchievementMsg",method = RequestMethod.POST)
    @ResponseBody
    public String addWechatAchievementMsg(HttpSession session,@RequestBody AchievementManagement achievementManagement){
        logger.info("微信成绩收集 消息推送 消息列表");
        logger.info("参数："+achievementManagement.toString());
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        achievementManagement = pubFunction(achievementManagement,session);
        int result = achievementManagementService.selectWechatAchievementMsg(achievementManagement);
        String termStr = "";
        String testTimeStr = "";
        if(achievementManagement.getTerm()==1){
                termStr = "上学期";
        }else{
                termStr = "下学期";
        }
        if(achievementManagement.getTestTime() == 1){
            testTimeStr = "期中考试";
        }else{
            testTimeStr = "期末考试";
        }
        if(result >0){
            jsonMap.put("code", -1);
            jsonMap.put("msg",achievementManagement.getTime()+"年"+termStr+testTimeStr+"成绩收集消息已存在，请勿重复提交，谢谢");
        }else{
             result = achievementManagementService.addWechatAchievementMsg(achievementManagement);
             if(result >0){
                jsonMap.put("code", 1);
                jsonMap.put("msg","添加成功");
             }else{
                jsonMap.put("code", 0);
                jsonMap.put("msg","添加失败");
             }
        }
        return JSONObject.toJSONString(jsonMap);
    }
    
    
    /*
     * @Author James
     * @Description 学员成绩 消息 推送
     * @Date 10:52 2018/11/14
     * @Param 
     * @return 
     **/
    @RequestMapping(value = "/pushWechatMsg",method = RequestMethod.POST)
    @ResponseBody
    public String pushWechatMsg(int wechatAchievementMsgId){
         AchievementManagement wechatMsgInfo  = achievementManagementService.selectWechatMsgById(wechatAchievementMsgId);
         Map<String,Object> jsonMap = new HashMap<String,Object>();
             if(wechatMsgInfo.getAlreadyPushTimes() < wechatMsgInfo.getPushTimes()){
                 int result = achievementManagementService.updateWechatMsgById(wechatMsgInfo.getId());
                 if(result > 0){
                     String termStr = "";
                     String testTimeStr = "";
                     if(wechatMsgInfo.getTerm()==1){
                         termStr = "上学期";
                     }else {
                         termStr = "下学期";
                     }
                     if(wechatMsgInfo.getTestTime() == 1){
                         testTimeStr = "期中考试";
                     }else {
                         testTimeStr = "期末考试";
                     }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    String str = sdf.format(wechatMsgInfo.getYear())+"年"+termStr+testTimeStr;
                    logger.info("微信 学员成绩收集消息推送"+str);
                    invokingService.sendUploadGradeMessage("英语成绩收集",str,"wechatAchievementMsgId="+wechatMsgInfo.getId());
                    jsonMap.put("code", 1);
                    jsonMap.put("msg","推送成功");
                 }else{
                    jsonMap.put("code", -1);
                    jsonMap.put("msg","推送失败");
                 }
             }else{
                jsonMap.put("code", 0);
                jsonMap.put("msg","可推送次数已经达到上限，请联系管理员修改");
             }
             return JSONObject.toJSONString(jsonMap);
    }

     /**
     * 功能描述:成绩收集信息消息推送  微信模板  相关
     * @param:
     * @return:
     * @auther: James
     * @date:  2018/10/12
     */
    @RequestMapping("/wechatTemplate")
    public String  wechatTemplate(){
        logger.info("成绩收集信息消息推送微信模板相关");
        return "achievementManagement/achievement_weChat_template";
    }
    
    
    /*
     * @Author James
     * @Description 学员成绩收集 微信消息推送 管理页面
     * @Date 18:44 2018/11/13
     * @Param model
     * @return 
     **/
    @RequestMapping("/wechatMsgManage")
    public String wechatMsgManage(Model model,Integer pageNum,Integer pageSize,String time){
        logger.info("学员成绩收集 微信消息推送 管理页面");
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
         if (time == null) {
            time = "";
        }
        PageInfo<AchievementManagement> pageInfo = achievementManagementService.wechatAchievementMsgList(pageNum,pageSize,time);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("time", time);
        return "achievementManagement/wechatAchievementMsgList";
    }

}
