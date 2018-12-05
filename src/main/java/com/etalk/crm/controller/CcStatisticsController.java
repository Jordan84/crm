package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.CcStatistics;
import com.etalk.crm.pojo.GradeLevel;
import com.etalk.crm.service.CcStatisticsService;
import com.etalk.crm.service.ClubConfigureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/10/17 15:47
 * @Description: 作为销售总监，希望CC可以看到渠道跟进数据，以便CC了解自己的资源情
 */
@RequestMapping("/CcStatistics")
@Controller
public class CcStatisticsController {
    @Resource
    private CcStatisticsService ccStatisticsService;
    @Resource
    ClubConfigureService clubConfigureService;

    /**
     * 功能描述:
     * @param: 某cc leads(例子) demo（demo）  orders(订单) Ratio(demo/orders)
     * @return:
     * @auther: James
     * @date:  2018/10/17 15:47
     */
    @RequestMapping(value = "/ldorStatistics",method = RequestMethod.POST)
    @ResponseBody
    public Object ldorStatistics(HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        List<CcStatistics> ldorStatisticsList = ccStatisticsService.ldorStatistics(userId);
        reultMap.put("code",1);
        reultMap.put("ldorStatisticsList",ldorStatisticsList);
        return JSONObject.toJSON(reultMap);
    }

     /**
     * 功能描述:
     * @param: drs 列表 数据展示
     * @return:
     * @auther: James
     * @date:  2018/10/17 15:47
     */
    @RequestMapping(value = "/drsList",method = RequestMethod.POST)
    @ResponseBody
    public Object drsList(HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        List<CcStatistics> drsList = ccStatisticsService.dsrList(userId);
        reultMap.put("code",1);
        reultMap.put("drsList",drsList);
        return JSONObject.toJSON(reultMap);
    }

     /*
      * @Author James
      * @Description 面板显示  底薪（底薪 在那个区间）
      * @Date 11:28 2018/10/25
      * @Param
      * @return
      **/
    @RequestMapping(value = "/showBaseSalary",method = RequestMethod.POST)
    @ResponseBody
    public Object showBaseSalary(HttpSession session) throws ParseException {
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        //本季度每个月  销售额
        List<CcStatistics> ccMonthSalaryList  = ccStatisticsService.everyMonthSalary(userId);
        //本季度销售业绩
        int result = ccStatisticsService.thisQuarterlyResults(userId);
        //底薪计算
        int baseSalary = ccStatisticsService.calculationCcBaseSalary(userId,result);
        reultMap.put("code",1);
        reultMap.put("ccMonthSalaryList",ccMonthSalaryList);
        reultMap.put("baseSalary",baseSalary);
        return JSONObject.toJSON(reultMap);
    }

     /*
      * @Author James
      * @Description 面板显示  薪资相关 （计算后的底薪加计算后的提成）
      * @Date 11:28 2018/10/25
      * @Param
      * @return
      **/
    @RequestMapping(value = "/showCcSalary",method = RequestMethod.POST)
    @ResponseBody
    public Object showCcSalary(HttpSession session) throws ParseException {
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        //上个季度销售业绩总额
        int lastQuarterlyResults = ccStatisticsService.lastQuarterlyResults(userId);
        //底薪 计算
        int baseSalary = ccStatisticsService.calculationCcBaseSalary(userId,lastQuarterlyResults);
        //本月销售业绩总额
        Integer ccthisMonthSalesVolume = ccStatisticsService.ccthisMonthSalesVolume(userId);
        //本渔销售提成 百分比
        BigDecimal calculationCcSalesPercentage = ccStatisticsService.calculationCcSalesPercentage(userId,ccthisMonthSalesVolume);
        //提成薪资计算
        //百分比  转成小数
        BigDecimal bd8 = new BigDecimal("100");
        BigDecimal Percentage = calculationCcSalesPercentage.divide(bd8,4,BigDecimal.ROUND_HALF_UP);

        BigDecimal bCcthisMonthSalesVolume = new BigDecimal(ccthisMonthSalesVolume);
        BigDecimal salesCommission = Percentage.multiply(bCcthisMonthSalesVolume);
        DecimalFormat df = new DecimalFormat("#0.00");
        BigDecimal bBaseSalary = new BigDecimal(baseSalary);
        reultMap.put("code",1);
        reultMap.put("lastQuarterlyResults",lastQuarterlyResults);
        reultMap.put("baseSalary",baseSalary);
        reultMap.put("salesCommission",df.format(salesCommission));
        reultMap.put("ccSalary",df.format(salesCommission.add(bBaseSalary)));
        reultMap.put("bCcthisMonthSalesVolume",bCcthisMonthSalesVolume);
        return JSONObject.toJSON(reultMap);
    }


     /*
     * @Author James
     * @Description 计算  cc  属于哪个  销售俱乐部
     * @Date 16:02 2018/11/8
     * @Param
     * @return
     **/
    @RequestMapping(value = "/calculationCcSalesClub",method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String calculationCcSalesClub(HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = (Integer) session.getAttribute("userid");
        //通过计算  得出该cc 位于哪个俱乐部区间  从而得到 该区间 俱乐部id
        int ccSalesClubType = ccStatisticsService.calculationCcSalesClub(userId);
        //查询  俱乐部  信息
        GradeLevel ccSalesClubInfo = clubConfigureService.selectClubById(ccSalesClubType);
        int ccThreeMonthSalesVolume = ccStatisticsService.ccThreeMonthSalesVolume(userId);
        reultMap.put("code",1);
        reultMap.put("ccSalesClubInfo",ccSalesClubInfo);
        reultMap.put("ccThreeMonthSalesVolume",ccThreeMonthSalesVolume);
        return JSON.toJSONString(reultMap);
    }

    @RequestMapping(value = "/PersonalResourceAnalysisList",method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String PersonalResourceAnalysisList(HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        List<CcStatistics> resourceAnalysisList = ccStatisticsService.PersonalResourceAnalysisList(Integer.parseInt(session.getAttribute("userid").toString()));
        reultMap.put("code",1);
        reultMap.put("resourceAnalysisList",resourceAnalysisList);
        return JSON.toJSONString(reultMap);
    }
}
