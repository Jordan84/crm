package com.etalk.crm.dao;

import com.etalk.crm.pojo.CcStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/11/20 11:13
 * @Description:
 */
@Mapper
public interface PersonalResourceAnalysisMapper {
    
    /*
     * @Author James
     * @Description cc leads  本月跟尽量
     * @Date 18:57 2018/11/20
     * @Param ccId  cc身份id
     * @return 
     **/
    int ccLeadsCounter(@Param("ccId")int ccId);

    /*
     * @Author James
     * @Description leads  本月最强对手已做到
     * @Date 18:58 2018/11/20
     * @Param
     * @return
     **/
    int opponentMaxLeadsCounter();
     /*
     * @Author James
     * @Description leads  本月leads 数比当前cc 低的人数
     * @Date 18:58 2018/11/20
     * @Param thisCcCounter  当前 cc leads 数量
     * @return
     **/
    int leadsCounterMoreThanThisCc(@Param("thisCcCounter")int thisCcCounter);
    
    
    /*
     * @Author James
     * @Description 本月所发动态统计
     * @Date 19:00 2018/11/20
     * @Param  ccId  cc身份id
     * @return 
     **/
    int stuDynamicsCounter(@Param("ccId")int ccId);

    /*
     * @Author James
     * @Description 本月最多 学员动态数量
     * @Date 19:01 2018/11/20
     * @Param
     * @return
     **/
    int maxStuDynamicsCounter();

    /*
     * @Author James
     * @Description 本月学员动态数量比当前cc 低的人数
     * @Date 19:02 2018/11/20
     * @Param 当前 cc 动态 数量
     * @return
     **/
    int stuDynamicsMoreThanThisCc(@Param("thisCcCounter")int thisCcCounter);
     /*
     * @Author James
     * @Description 本月MKT渠道  demo 数量
     * @Date 19:00 2018/11/20
     * @Param  ccId  cc身份id
     * @return
     **/
    int selectmktDemoCounter(@Param("ccId")int ccId);
    
    
    /*
     * @Author James
     * @Description 本月MKT渠道demo 最大数量
     * @Date 19:04 2018/11/20
     * @Param 
     * @return 
     **/
    int maxMktDemoCounter();

    /*
     * @Author James
     * @Description MKTDemo 数量比当前 cc 多的 人数
     * @Date 19:05 2018/11/20
     * @Param thisCcCounter 当前 cc mkt渠道 demo  数量
     * @return
     **/
    int mktDemoMoreThanThisCc(@Param("thisCcCounter")int thisCcCounter);

    /*
     * @Author James
     * @Description  cc  本月 ref pwic pwio 三个渠道demo 数量
     * @Date 9:53 2018/11/21
     * @Param ccId
     * @return
     **/
    List<CcStatistics> rePwicPwioDemoList(@Param("ccId")int ccId);

    /*
     * @Author James
     * @Description cc 本月(type) rf pwic pwio各个渠道  demo 最多  数量(分类型查)
     * @Date 10:10 2018/11/21
     * @Param type 渠道 id
     * @return
     **/
    int maxRefPwicPwioDemoCounter(@Param("type")int type);
    /*
     * @Author James
     * @Description 本月(type) rf pwic pwio渠道demo  比当前cc少的   人数
     * @Date 10:16 2018/11/21
     * @Param type 渠道   thisCcCounter：当前cc 在该渠道demo  数量
     * @return
     **/
    int refPwicPwioMorethanThisCc(@Param("type")int type,@Param("thisCcCounter")int thisCcCounter);
    /*
     * @Author James
     * @Description cc 关单  周期统计 平均天数  三个阶段分开查
     * @Date 10:18 2018/11/21
     * @Param  step（三个时间段） 该月不同时段  1-10号   11-20号  21-月末最后一天
     * @return
     **/
    BigDecimal ccCloseLeadsCycle(@Param("ccId")int ccId,@Param("step")int step);
    /*
     * @Author James
     * @Description cc 关单周期统计 平均天数（最大）三个阶段分开查 1-10   11-20 21-30
     * @Date 10:21 2018/11/21
     * @Param step  该月不同时段  1-10号   11-20号  21-月末最后一天
     * @return
     **/
    BigDecimal maxCcCloseLeadsCycle(@Param("step")int step);

    /*
     * @Author James
     * @Description //TODO
     * @Date 10:23 2018/11/21
     * @Param
     * @return
     **/
    int  moreThanThisCcCloseLeadsCycle(Map<String,Object> map);
    /*
     * @Author James
     * @Description 查询cc  总人数  计算打败了多少cc
     * @Date 14:30 2018/11/21
     * @Param
     * @return
     **/
    int selectCcCounter();
}
