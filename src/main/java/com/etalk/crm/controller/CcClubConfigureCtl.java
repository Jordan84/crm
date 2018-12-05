package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.ClubConfigureService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/11/7 14:25
 * @Description:cc 俱乐部配置管理
 */
@RequestMapping(value = "/ccClubConfigure",produces = "application/json; charset=utf-8")
@Controller
public class CcClubConfigureCtl {
    private static final Logger logger= LogManager.getLogger(CcClubConfigureCtl.class);

    @Resource
    private ClubConfigureService clubConfigureService;
    /*
     * @Author James
     * @Description cc 俱乐部配置  表 列表
     * @Date 15:10 2018/11/7
     * @Param model
     * @return
     **/
    @RequestMapping(value="/ccClubConfigureInfo",method = RequestMethod.GET)
    public String ccClubConfigureInfo(Model model){
        logger.info("进入cc 俱乐部配置表列表");
        List<ClubConfigureInfo> clubConfigureInfoList = clubConfigureService.clubConfigureInfoList();
        model.addAttribute("clubConfigureInfoList",clubConfigureInfoList);
        return "ccClubConfigureInfo/clubConfigureList";
    }

   /*
    * @Author James
    * @Description 查看销售俱乐部配置表区间列表
    * @Date 16:42 2018/11/7
    * @Param
    * @return
    **/
    @RequestMapping(value = "/clubConfigureDetail",method = RequestMethod.GET)
    public String baseSalaryConfigureList(Model model,int id){
        logger.info("查看销售俱乐部配置表区间列表");
        logger.info("销售俱乐部配置表id:"+id);
        //配置 区间 列表
        List<ClubScopeConfigure> clubScopeConfigureList = clubConfigureService.clubConfgiureScopeList(id);
        //配置表 基本信息
        ClubConfigureInfo clubConfigureInfo = clubConfigureService.clubConfigureInfoById(id);
        //销售俱乐部  类别 列表
        List<GradeLevel> clubTypeList = clubConfigureService.selectClubType();
        model.addAttribute("clubTypeList",clubTypeList);
        model.addAttribute("clubScopeConfigureList",clubScopeConfigureList);
        model.addAttribute("clubConfigureInfo",clubConfigureInfo);
        return "ccClubConfigureInfo/clubConfigureDetail";
    }


    /*
     * @Author James
     * @Description  进入销售俱乐部配置区间添加页面
     * @Date 16:48 2018/11/7
     * @Param
     * @return
     **/
    @RequestMapping(value = "/addClubConf",method = RequestMethod.GET)
    public String addsalaryConfigure(Model model){
        logger.info("进入销售俱乐部配置表区间添加页面");
        //销售俱乐部  类别 列表
        List<GradeLevel> clubTypeList = clubConfigureService.selectClubType();
        model.addAttribute("clubTypeList",clubTypeList);
        return "ccClubConfigureInfo/addClubConf";
    }

    
    /*
     * @Author James
     * @Description 销售俱乐部配置区间 修改页面
     * @Date 16:51 2018/11/7
     * @Param 
     * @return 
     **/
    @RequestMapping(value = "/eidtsalaryConfigure",method = RequestMethod.GET)
    public String eidtsalaryConfigure(int id,Model model){
        logger.info("销售俱乐部配置表区间 修改页面");
        logger.info("销售俱乐部配置表id:"+id);
        //配置 区间 列表
        List<ClubScopeConfigure> clubScopeConfigureList = clubConfigureService.clubConfgiureScopeList(id);
        //销售俱乐部  类别 列表
        List<GradeLevel> clubTypeList = clubConfigureService.selectClubType();
        model.addAttribute("clubTypeList",clubTypeList);
        //配置表 基本信息
        ClubConfigureInfo clubConfigureInfo = clubConfigureService.clubConfigureInfoById(id);
        model.addAttribute("clubScopeConfigureList",clubScopeConfigureList);
         //配置 表基本信息
        model.addAttribute("clubConfigureInfo",clubConfigureInfo);
        //配置 表id
        model.addAttribute("clubConfigureInfoId",id);
        return "ccClubConfigureInfo/eidtClubConf";
    }

    
    /*
     * @Author James
     * @Description 销售俱乐部  配置区间列表提交
     * @Date 16:52 2018/11/7
     * @Param 
     * @return 
     **/
    @RequestMapping(value = "/subclubScopeConfigure",method = RequestMethod.POST)
    @ResponseBody
    public String submitsalaryConfigure(@RequestBody JSONObject jsonObject, HttpSession session){
        logger.info("销售俱乐部配置区间列表表单数据异步提交");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        ClubConfigureInfo clubConfigureInfo = new ClubConfigureInfo();
        clubConfigureInfo.setAddUserId(userId);
        clubConfigureInfo.setState(1);
        //获取 配置 表 名称clubConfigureName
        clubConfigureInfo.setClubConfigureName(jsonObject.get("clubConfigureName").toString());
        if(jsonObject.get("id") != null && StringUtil.isNotEmpty(jsonObject.get("id").toString())){
            clubConfigureInfo.setId(Integer.parseInt(jsonObject.get("id").toString()));
        }
        //获取 配置区间 相关  列表
        List<Map> paramList = (List<Map>)jsonObject.get("paramList");
        //插入  更改  操作
        int result = clubConfigureService.subclubScopeConfigure(paramList,clubConfigureInfo);
        if(result >0){
            reultMap.put("code", 1);
            reultMap.put("msg", "提交成功");
        }else if(result==-2){
            reultMap.put("code", -2);
            reultMap.put("msg", "该配置表名字已存在，请重新填写");
        }else{
            reultMap.put("code", 0);
            reultMap.put("msg", "提交失败");
        }
        return JSONObject.toJSONString(reultMap);
    }


   
   /*
    * @Author James
    * @Description 更改 销售俱乐部 配置表状态
    * @Date 17:05 2018/11/7
    * @Param 
    * @return 
    **/
    @RequestMapping(value = "/isValid",method = RequestMethod.POST)
    @ResponseBody
    public String isValid(int id,int state){
        logger.info("更改销售俱乐部配置表状态");
        logger.info("销售俱乐部配置表id："+id);
        logger.info("销售俱乐部配置表："+state);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        ClubConfigureInfo clubConfigureInfo = new ClubConfigureInfo();
        clubConfigureInfo.setId(id);
        clubConfigureInfo.setState(state);
        int result = clubConfigureService.eidtClubConfigureInfo(clubConfigureInfo);
        if(result >0){
            reultMap.put("code", 1);
            reultMap.put("msg", "修改成功");
        }else{
            reultMap.put("code", -1);
            reultMap.put("msg", "修改失败");
        }
        return JSONObject.toJSONString(reultMap);
    }

}
