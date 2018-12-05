package com.etalk.crm.dao;

import com.etalk.crm.pojo.CcStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/10/17 10:58
 * @Description: 作为销售总监，希望CC可以看到渠道跟进数据，以便CC了解自己的资源情况（225）
 */
@Mapper
public interface CcStatisticsMapper {
    /**
     * 功能描述:某个  cc 例子
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectLeads (@Param("ccId") int ccId);

     /**
     * 功能描述:某个  cc 例子
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectDemo (@Param("ccId") int ccId);

     /**
     * 功能描述:某个  cc 成交的订单
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectOrders (@Param("ccId") int ccId);

    /**
     * 功能描述:除了  os sm ofads 各个渠道最大 的ratio   demo/orders  (demo数除订单数量)
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    List<CcStatistics> selectMaxRatio();

    /**
     * 功能描述:统计 os sm ofads 三个合计mkt  渠道最大 的ratio   demo/orders  (demo数除订单数量)
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    Double mergeSooMaxRatio();

    /**
     * 功能描述:统计 除了2:rf 以外合计mkt  渠道最大 的ratio   demo/orders  (demo数除订单数量)
     * @param:
     * @return:
     * @auther: James
     * @date:  2018-10-17
     */
    Double MargeExceptRefMaxRatio();

    /**
     * dsr  demo
     * @param ccId
     * @return
     */
    List<CcStatistics> dsrDemo(@Param("ccId") int ccId);

    /**
     * dsr  订单
     * @param ccId
     * @return
     */
    List<CcStatistics> dsrOrders(@Param("ccId") int ccId);

    /**
     * 功能描述: dsr  标题描述
     * @param: ccId
     * @return:
     * @auther:James
     * @date:2018/10/22
     */
    CcStatistics dsrDescription(@Param("ccId") int ccId);
     /**
     * 功能描述: dsr cc 个人成单  定金 数统计
     * @param: ccId
     * @return:
     * @auther:James
     * @date:2018/10/22
     */
     List<CcStatistics> ccDeposit(@Param("ccId") int ccId);

    /**
     * 功能描述: dsr  cc个人例子数统计
     * @param: ccId
     * @return:
     * @auther:James
     * @date:2018/10/22
     */
    List<CcStatistics> ccLeadsCounter(@Param("ccId") int ccId);
     
     /*
      * @Author James
      * @Description 面板显示 本季度  各个月销售业绩   以及底薪展示
      * @Date 11:29 2018/10/25
      * @Param 
      * @return 
      **/   
    List<CcStatistics> everyMonthSalary(@Param("ccId") int ccId);

    /*
      * @Author James
      * @Description 本季度销售业绩总额
      * @Date 11:29 2018/10/25
      * @Param
      * @return
      **/
    int  thisQuarterlyResults(@Param("ccId") int ccId);
    
    
    /*
     * @Author James
     * @Description 查询  某cc  设置的 底薪配置区间表
     * @Date 19:53 2018/10/25
     * @Param 
     * @return 
     **/
    Integer ccBaseSalaryConf(@Param("ccId") int ccId);

     /*
     * @Author James
     * @Description 查询  某cc  上个季度销售业绩总额
     * @Date 19:53 2018/10/25
     * @Param
     * @return
     **/
    Integer lastQuarterlyResults(@Param("ccId") int ccId);

      /*
     * @Author James
     * @Description 查询  某cc  设置的 底薪配置区间表
     * @Date 19:53 2018/10/25
     * @Param
     * @return
     **/
    Integer ccSalesCommissionConfId(@Param("ccId") int ccId);

      /*
     * @Author James
     * @Description 查询  某cc  本月销售业绩
     * @Date 19:53 2018/10/25
     * @Param
     * @return
     **/
    Integer ccthisMonthSalesVolume(@Param("ccId") int ccId);

    /**
     * 渠道 资源
     * @return
     */
    List<CcStatistics> selectType();
    
    
    /*
     * @Author James
     * @Description cc 近三个月 销售业绩统计
     * @Date 15:32 2018/11/8
     * @Param ccId ccId
     * @return 
     **/
    int ccThreeMonthSalesVolume(@Param("ccId") int ccId);
}
