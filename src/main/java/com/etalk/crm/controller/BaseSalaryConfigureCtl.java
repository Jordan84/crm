package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.BaseSalaryInfo;
import com.etalk.crm.service.BaseSalaryConfigureService;
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
 * @Date: 2018/10/23 10:33
 * @Description:销售底薪配置
 */
@Controller
@RequestMapping("/baseSalaryConfigure")
public class BaseSalaryConfigureCtl {
    private static final Logger logger= LogManager.getLogger(BaseSalaryConfigureCtl.class);

    @Resource
    private BaseSalaryConfigureService baseSalaryConfigureService;
    /*
     * @Author James
     * @Description 底薪配置列表
     * @Date 10:43 10:43
     * @Param 
     * @return 
     **/
    @RequestMapping(value="/baseSalaryList",method = RequestMethod.GET)
    public String baseSalaryList(Model model){
        logger.info("进入底薪配置列表以及相关操作界面");
        List<BaseSalaryInfo> baseSalaryList = baseSalaryConfigureService.salaryInfoList();
        model.addAttribute("baseSalaryList",baseSalaryList);
        return "baseSalaryConfigure/baseSalaryInfoList";
    }


    /*
     * @Author James
     * @Description 底薪区间列表
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/salaryConfigureDetail",method = RequestMethod.GET)
    public String baseSalaryConfigureList(Model model,int id){
         logger.info("查看底薪区间列表");
         logger.info("底薪信息表id:"+id);
        List<BaseSalaryConfigure> baseSalaryConfigureList = baseSalaryConfigureService.confgiureList(id);
        BaseSalaryInfo baseSalaryInfo = baseSalaryConfigureService.selectsalaryInfoById(id);
        model.addAttribute("salaryConfigureDetail",baseSalaryConfigureList);
        model.addAttribute("baseSalaryInfo",baseSalaryInfo);
        return "baseSalaryConfigure/salaryConfigureDetail";
    }

     /*
     * @Author James
     * @Description 进入底薪配置添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/addSalaryConfigure",method = RequestMethod.GET)
    public String addsalaryConfigure(){
         logger.info("进入底薪配置添加页面");
        return "baseSalaryConfigure/addSalaryConfigure";
    }

     /*
     * @Author James
     * @Description 进入底薪配置添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/eidtsalaryConfigure",method = RequestMethod.GET)
    public String eidtsalaryConfigure(int id,Model model){
        logger.info("进入底薪配置编辑页面");
        logger.info("底薪信息表id:"+id);
        List<BaseSalaryConfigure> baseSalaryConfigureList = baseSalaryConfigureService.confgiureList(id);
        BaseSalaryInfo baseSalaryInfo = baseSalaryConfigureService.selectsalaryInfoById(id);
        model.addAttribute("salaryConfigureDetail",baseSalaryConfigureList);
         //配置 表基本信息
        model.addAttribute("baseSalaryInfo",baseSalaryInfo);
        //配置 表id
        model.addAttribute("baseSalaryId",id);
        return "baseSalaryConfigure/editSalaryConfigure";
    }

    /*
     * @Author James
     * @Description 进入底薪配置添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/submitsalaryConfigure",method = RequestMethod.POST)
    @ResponseBody
    public Object submitsalaryConfigure(@RequestBody JSONObject jsonObject, HttpSession session){
        logger.info("表单数据异步提交");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        BaseSalaryInfo baseSalaryInfo = new BaseSalaryInfo();
        baseSalaryInfo.setAddUserId(userId);
        baseSalaryInfo.setState(1);
        //获取 配置 表 名称
        baseSalaryInfo.setBaseSalaryName(jsonObject.get("baseSalaryName").toString());
        if(jsonObject.get("id") != null && StringUtil.isNotEmpty(jsonObject.get("id").toString())){
            baseSalaryInfo.setId(Integer.parseInt(jsonObject.get("id").toString()));
        }
        //获取 配置区间 相关  列表
        List<Map> paramList = (List<Map>)jsonObject.get("paramList");
        //插入  更改  操作
        int result = baseSalaryConfigureService.submitsalaryConfigure(paramList,baseSalaryInfo);
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
        return JSONObject.toJSON(reultMap);
    }
    
    
    /*
     * @Author James
     * @Description 更改 配置表 状态
     * @Date 20:09 2018/10/24
     * @Param 
     * @return 
     **/
    @RequestMapping(value = "/isValid",method = RequestMethod.POST)
    @ResponseBody
    public Object isValid(int id,int state){
        logger.info("更改 配置表 状态");
        logger.info("配置表id"+id);
        logger.info("配置表状态"+state);
        Map<String,Object> reultMap = new HashMap<String,Object>();

        BaseSalaryInfo baseSalaryInfo = new BaseSalaryInfo();
        baseSalaryInfo.setId(id);
        baseSalaryInfo.setState(state);
        int result = baseSalaryConfigureService.eidtSalaryInfo(baseSalaryInfo);
        if(result >0){
            reultMap.put("code", 1);
            reultMap.put("msg", "修改成功");
        }else{
            reultMap.put("code", -1);
            reultMap.put("msg", "修改失败");
        }
        return JSONObject.toJSON(reultMap);
    }
}
