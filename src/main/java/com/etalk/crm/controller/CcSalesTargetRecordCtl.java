package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.pojo.CaseShareThumbs;
import com.etalk.crm.pojo.CcStatistics;
import com.etalk.crm.service.CcSalesPerformanceService;
import com.etalk.crm.service.CcStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/9/17 17:15
 * @Description:cc 銷售目標 相关
 */
@RequestMapping("/CcSalesPerformanceCtl")
@Controller
public class CcSalesTargetRecordCtl {
    @Resource
    private CcSalesPerformanceService ccSalesPerformanceService;

    @Resource
    private CcStatisticsService ccStatisticsService;
    /*
     * 功能描述:销售工作表现  面板页面
     * @param: model
     * @return:
     * @auther: James
     * @date: 2018/9/17 17:18
     */
    @RequestMapping("/salesPerformance")
    public String CcSalesTargetCtl(HttpSession session,Model model){
        Integer userId = (Integer) session.getAttribute("userid");
        //dsr  面板标题 内容
        CcStatistics ccStatistics  = ccStatisticsService.dsrDescription(userId);
        model.addAttribute("ccStatistics",ccStatistics);
        return "CcSalesPerformanceCtl/CcSalesPerformanceCtl";
    }

    /**
     * 功能描述:cc个人 销售表现 面板数据
     * @param:
     * @return:
     * @auther: James
     * @date: 2018/9/18 11:22
     */
    @RequestMapping(value = "/ccSalesPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Object ccSalesPerformance(Model model, HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer ccId =  (Integer) session.getAttribute("userid");
        Integer thisMonthtSales = ccSalesPerformanceService.thisMonthtSales(ccId);
        Integer thisMonthtSalesTarget = ccSalesPerformanceService.thisMonthtSalesTarget(ccId);
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = "0";
        if(thisMonthtSales != null  && thisMonthtSalesTarget != null){
             result = numberFormat.format((float)thisMonthtSales/(float)thisMonthtSalesTarget*100);
        }
        if(thisMonthtSales == null){
            thisMonthtSales = 0;
        }
        if(thisMonthtSalesTarget == null){
            thisMonthtSalesTarget = 0;
        }
        result = result+"%";
        reultMap.put("thisMonthtSales",thisMonthtSales);
        reultMap.put("thisMonthtSalesTarget",thisMonthtSalesTarget);
        reultMap.put("percent",result);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 功能描述:店铺销售表现 面板数据
     * @param:
     * @return:
     *  @auther: James
     * @date: 2018/9/18 11:22
     */
    @RequestMapping(value = "/storesSalesPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Object storesSalesPerformance(Model model, HttpSession session){
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId =  (Integer) session.getAttribute("userid");
        Integer storesId = ccSalesPerformanceService.selectStoresByUserId(userId);
        Integer thisMonthtSales = ccSalesPerformanceService.thisMonthStoresSales(storesId);
        Integer thisMonthtSalesTarget = ccSalesPerformanceService.thisMonthStoresSalesTarget(storesId);
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = "0";
        if(thisMonthtSales != null  && thisMonthtSalesTarget != null){
             result = numberFormat.format((float)thisMonthtSales/(float)thisMonthtSalesTarget*100);
        }
        if(thisMonthtSales == null){
            thisMonthtSales = 0;
        }
        if(thisMonthtSalesTarget == null){
            thisMonthtSalesTarget = 0;
        }
        result = result+"%";
        reultMap.put("thisMonthtSales",thisMonthtSales);
        reultMap.put("thisMonthtSalesTarget",thisMonthtSalesTarget);
        reultMap.put("percent",result);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }

}
