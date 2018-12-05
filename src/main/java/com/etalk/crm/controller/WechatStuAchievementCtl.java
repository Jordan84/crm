package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.client.InvokingService;
import com.etalk.crm.dao.PersonMapper;
import com.etalk.crm.pojo.AchievementManagement;
import com.etalk.crm.pojo.GradeLevel;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.pojo.QuestionGrade;
import com.etalk.crm.service.AchievementManagementService;
import com.etalk.crm.service.QuestionService;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Auther: James
 * @Date: 2018/10/16 11:02
 * @Description:微信 提交 学员成绩
 */
@Controller
@RequestMapping("/wechatStuAchievement")
public class WechatStuAchievementCtl {
    private static final Logger logger= LogManager.getLogger(WechatStuAchievementCtl.class);

    @Resource
    private InvokingService invokingService;

    @Resource
    private AchievementManagementService achievementManagementService;

    @Resource
    private PersonMapper personMapper;

    @Resource
    private QuestionService questionService;

    /**
     * 功能描述:成绩等级列表
     * @param:
     * @return:
     * @auther: James
     * @date:  2018/10/16 11:02
     */
    @RequestMapping(value = "/selectGradeLevelList",method = RequestMethod.POST)
    @ResponseBody
    public Object gradeLevelList(){
         logger.info("成绩等级列表");
        List<GradeLevel> gradeLevelList = achievementManagementService.gradeLevelList();
         //添加年级
        List<QuestionGrade> gradeList = questionService.selectQGradeList();
        Map<String,Object> reultMap = new HashMap<String,Object>();
        reultMap.put("gradeLevelList",gradeLevelList);
        reultMap.put("gradeList",gradeList);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }

     /**
     * 功能描述:成绩分制列表
     * @param:
     * @return:
     * @auther: James
     * @date:  2018/10/16 11:02
     */
    @RequestMapping(value = "/selectGradeTypeList",method = RequestMethod.POST)
    @ResponseBody
    public Object selectGradeTypeList(){
         logger.info("成绩分制列表");
        List<GradeLevel> gradeTypeList = achievementManagementService.gradeTypeList();
        Map<String,Object> reultMap = new HashMap<String,Object>();
        reultMap.put("gradeTypeList",gradeTypeList);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }
     /**
     * 功能描述:微信学员  成绩提交
     * @param:
     * @return:
     * @auther: James
     * @date:  2018/10/16 11:02
     */
    @RequestMapping(value = "/addAchievement",method = RequestMethod.POST)
    @ResponseBody
    public  Object addAchievement(int personId, Integer totalScore, String time,
                                  int term, BigDecimal score, Integer levelScore, int gradeId, int testTime, int wechatAchievementMsgId){
        logger.info("微信学员  成绩提交");
        logger.info("personId:"+personId);
        logger.info("totalScore:"+totalScore);
        logger.info("time:"+time);
        logger.info("term:"+term);
        logger.info("score:"+score);
        logger.info("levelScore:"+levelScore);
        logger.info("gradeId:"+gradeId);
        logger.info("testTime:"+testTime);
        logger.info("wechatAchievementMsgId:"+wechatAchievementMsgId);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        AchievementManagement achievementManagement = new AchievementManagement();
        Person person = personMapper.selectByPersonId(personId);
        if(person != null){
            achievementManagement.setPersonId(personId);
            achievementManagement.setCcId(person.getUserId());
            achievementManagement.setSscId((person.getSscId()));
        }else{
            reultMap.put("code", -2);
            reultMap.put("msg", "该学生身份不合法");
            return JSONObject.toJSON(reultMap);
        }
        achievementManagement.setAddUserId(0);
        achievementManagement.setTerm(term);
        achievementManagement.setScore(score);
        if(levelScore == null){
            levelScore = 0;
        }
        achievementManagement.setLevelScore(levelScore);
        achievementManagement.setGradeId(gradeId);
        achievementManagement.setTotalScore(totalScore);
        achievementManagement.setTestTime(testTime);
        achievementManagement.setWechatAchievementMsgId(wechatAchievementMsgId);
        if(StringUtil.isNotEmpty(time)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            try {
                achievementManagement.setYear(sdf.parse(time));
            } catch (ParseException e) {
                logger.info(e.getMessage());
            }
        }
        if(achievementManagement.getTotalScore() <5){
            //1 为填写的分数
            achievementManagement.setScoreType(1);
        }else{
            //2 下拉选登记分数
            achievementManagement.setScoreType(2);
        }
        //判断 某学员在 某年度  某个学期  期中 或 期末 考试成绩  是否存在
        int isExist = achievementManagementService.isAchievementExist(achievementManagement);
        if(isExist == 0){
            int result = achievementManagementService.addAchievement(achievementManagement);
            if(result >0){
                reultMap.put("code", 1);
                reultMap.put("msg", "提交成功");
            }else{
                reultMap.put("code", -1);
                reultMap.put("msg", "提交失败");
            }
        }else{
            reultMap.put("code", 0);
            reultMap.put("msg", "该学生成绩已存在");
        }
        return JSONObject.toJSON(reultMap);
    }


    /**
     * @Author James
     * @Description 查询微信成绩收集 参数
     * @Date 16:34 2018/10/30
     * @Param wechatAchievementMsgId  成绩收集消息 推送  id
     * @return
     **/
    @RequestMapping(value = "/achievementCollectionParam",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Object achievementCollectionParam(int wechatAchievementMsgId){
        logger.info("查询微信成绩收集 参数");
        logger.info(" 成绩收集消息推送id>>>>>>>wechatAchievementMsgId:"+wechatAchievementMsgId);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        if(wechatAchievementMsgId == 0){
           reultMap.put("code", -1);
           reultMap.put("msg", "wechatAchievementMsgId不能为0或者为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        AchievementManagement achievementManagement = achievementManagementService.selectAchievementCollectionParam(wechatAchievementMsgId);
        achievementManagement.setTime(sdf.format(achievementManagement.getYear()));
        reultMap.put("code", 1);
        reultMap.put("achievementCollectionParam", achievementManagement);
        return JSONObject.toJSON(reultMap);
    }
    
    
    /*
     * @Author James
     * @Description 微信  学院成绩收集   消息推送
     * @Date 16:42 2018/10/30
     * @Param  subject 科目  testTime：考试时间  param： 参数
     * @return 
     **/
    @RequestMapping(value = "/wechatAchievementCollectionPush",method = RequestMethod.POST)
    @ResponseBody
    public Object wechatAchievementCollectionPush(String subject,String testTime,String param){
         logger.info("微信 学员成绩收集消息推送");
         Map<String,Object> reultMap = new HashMap<String,Object>();
         invokingService.sendUploadGradeMessage(subject,testTime,param);
         reultMap.put("code", 1);
         return JSONObject.toJSON(reultMap);
    }

}
