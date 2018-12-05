package com.etalk.crm.service;

import com.etalk.crm.pojo.CcStatistics;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/10/17 15:34
 * @Description:作为销售总监，希望CC可以看到渠道跟进数据，以便CC了解自己的资源情况（225）
 */
public interface CcStatisticsService {
     /**
     * 功能描述:某个  cc 例子
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectLeads (int ccId);

     /**
     * 功能描述:某个  cc 例子
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectDemo (int ccId);

     /**
     * 功能描述:某个  cc 成交的订单
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectOrders (int ccId);

    /**
     * 功能描述:除了  os sm ofads 各个最大 的ratio   demo/orders  (demo数除订单数量)
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectMaxRatio();
    /**
     * 功能描述:
     * @param: 某cc leads(例子) demo（demo）  orders(订单) Ratio(demo/orders)
     * @return: 
     * @auther: 
     * @date:  
     */
    List <CcStatistics>ldorStatistics(int ccId);


    /**
     * dsr  demo  order  两个查询 合并结果
     * @param ccId
     * @return
     */
    List<CcStatistics> dsrList(int ccId);

     /**
     * 功能描述: dsr  标题描述
     * @param: ccId
     * @return:
     * @auther:James
     * @date:2018/10/22
     */
    CcStatistics dsrDescription(int ccId);


     /*
      * @Author James
      * @Description 面板显示  底薪
      * @Date 11:28 2018/10/25
      * @Param
      * @return
      **/
    List<CcStatistics> everyMonthSalary(int ccId) throws ParseException;
    /*
     * @Author James
     * @Description 计算 cc 底薪 （由销售额所在的区间决定）
     * @Date 19:51 2018/10/25
     * @Param 
     * @return 
     **/
    int calculationCcBaseSalary(int ccId,int result);

     /*
     * @Author James
     * @Description 计算 cc 提成工资（由销售额所在的提成配置表区间决定）
     * @Date 19:51 2018/10/25
     * @Param
     * @return
     **/
    BigDecimal calculationCcSalesPercentage(int ccId, int result);

    /*
     * @Author James
     * @Description 本季度销售业绩
     * @Date 19:51 2018/10/25
     * @Param
     * @return
     **/
    Integer thisQuarterlyResults(int ccId);


     /*
     * @Author James
     * @Description 查询  某cc  上个季度销售业绩总额
     * @Date 19:53 2018/10/25
     * @Param
     * @return
     **/
    Integer lastQuarterlyResults(int ccId);

     /*
     * @Author James
     * @Description 查询  某cc 本月销售业绩
     * @Date 19:53 2018/10/25
     * @Param
     * @return
     **/
    Integer ccthisMonthSalesVolume(int ccId);

    /*
     * @Author James
     * @Description 计算  cc  属于哪个  销售俱乐部
     * @Date 16:01 2018/11/8
     * @Param ccId ccId
     * @return
     **/
    int calculationCcSalesClub(int ccId);


    /*
     * @Author James
     * @Description cc 近三个月 销售业绩统计
     * @Date 15:32 2018/11/8
     * @Param ccId ccId
     * @return
     **/
    int ccThreeMonthSalesVolume(int ccId);

    /*
     * @Author James
     * @Description cc  各个渠道资源分析以及 关单周期分析列表  拼接
     * @Date 10:26 2018/11/21
     * @Param
     * @return
     **/
    List<CcStatistics> PersonalResourceAnalysisList(int ccId);
}
