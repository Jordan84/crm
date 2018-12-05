package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.BaseSalaryInfo;
import com.etalk.crm.pojo.CcSalesCommissionsConf;
import com.etalk.crm.pojo.SalesCommissionInfo;
import com.etalk.crm.service.CcSalesCommissionConfService;
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
 * @Date: 2018/10/26 13:58
 * @Description:  cc 销售提成配置
 */
@Controller
@RequestMapping("/ccSalesCommissionConf")
public class CcSalesCommissionConfCtl {
    private static final Logger logger= LogManager.getLogger(CcSalesCommissionConfCtl.class);

    @Resource
    private CcSalesCommissionConfService ccSalesCommissionConfService;
    /*
     * @Author James
     * @Description 销售提成配置列表
     * @Date 10:43 10:43
     * @Param
     * @return
     **/
    @RequestMapping("/ccSalesCommissionConfList")
    public String ccSalesCommissionConfList(Model model){
        logger.info("进入销售提成配置列表以及相关操作界面");
        List<SalesCommissionInfo> salesCcommissionList = ccSalesCommissionConfService.salesCcommissionInfoList();
        model.addAttribute("salesCcommissionList",salesCcommissionList);
        return "ccSalesCommissionConf/ccSalesCommissionConfList";
    }

        /*
     * @Author James
     * @Description 底薪区间列表
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/salesCommissionConfDetail",method = RequestMethod.GET)
    public String salesCommissionConfDetail(Model model,int id){
         logger.info("查看销售提成区间列表");
         logger.info("提成信息表id:"+id);
        List<CcSalesCommissionsConf> salesCommissionsConfList = ccSalesCommissionConfService.confgiureList(id);
        SalesCommissionInfo salesCommissionInfo = ccSalesCommissionConfService.selectSCIById(id);
        model.addAttribute("salesCommissionsConfList",salesCommissionsConfList);
        model.addAttribute("salesCommissionName", salesCommissionInfo.getSalesCommissionName());
        return "ccSalesCommissionConf/salesCommissionConfDetail";
    }


     /*
     * @Author James
     * @Description 进入销售提成添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/addSalesCommissionConf",method = RequestMethod.GET)
    public String addsalaryConfigure(){
         logger.info("进入销售提成配置添加页面");
        return "ccSalesCommissionConf/addSalesCommissionConf";
    }


         /*
     * @Author James
     * @Description 进入销售提成配置添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/eidtSalesCommissionConf",method = RequestMethod.GET)
    public String eidtSalesCommissionConf(int id,Model model){
        logger.info("进入销售提成配置编辑页面");
        logger.info("销售提成信息表id:"+id);
        List<CcSalesCommissionsConf> ccSalesCommissionConfList = ccSalesCommissionConfService.confgiureList(id);
        SalesCommissionInfo salesCommissionInfo = ccSalesCommissionConfService.selectSCIById(id);
        model.addAttribute("ccSalesCommissionConfList",ccSalesCommissionConfList);
         //配置 表基本信息
        model.addAttribute("salesCommissionName", salesCommissionInfo.getSalesCommissionName());
        //配置 表id
        model.addAttribute("salesCcommissionId",id);
        return "ccSalesCommissionConf/eidtSalesCommissionConf";
    }

    /*
     * @Author James
     * @Description 进入底薪配置添加页面
     * @Date 15:45 15:45
     * @Param
     * @return String
     **/
    @RequestMapping(value = "/submitccSalesCommissionConf",method = RequestMethod.POST)
    @ResponseBody
    public Object submitccSalesCommissionConf(@RequestBody JSONObject jsonObject, HttpSession session){
        logger.info("表单数据异步提交");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        SalesCommissionInfo salesCommissionInfo = new SalesCommissionInfo();
        salesCommissionInfo.setAddUserId(userId);
        salesCommissionInfo.setState(1);
        //获取 配置 表 名称
        salesCommissionInfo.setSalesCommissionName(jsonObject.get("salesCommissionName").toString());
        if(jsonObject.get("id") != null && StringUtil.isNotEmpty(jsonObject.get("id").toString())){
            salesCommissionInfo.setId(Integer.parseInt(jsonObject.get("id").toString()));
        }
        //获取 配置区间 相关  列表
        List<Map> paramList = (List<Map>)jsonObject.get("paramList");
        //插入  更改  操作
        int result = ccSalesCommissionConfService.submitccSalesCommissionConf(paramList, salesCommissionInfo);
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

        SalesCommissionInfo salesCommissionInfo = new SalesCommissionInfo();
        salesCommissionInfo.setId(id);
        salesCommissionInfo.setState(state);
        int result = ccSalesCommissionConfService.eidtCommissionInfo(salesCommissionInfo);
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
