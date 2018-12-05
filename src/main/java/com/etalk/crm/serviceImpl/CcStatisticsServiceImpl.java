package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.*;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.CcStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: James
 * @Date: 2018/10/17 15:35
 * @Description:
 */
@Service
public class CcStatisticsServiceImpl implements CcStatisticsService {
    @Resource
    private CcStatisticsMapper ccStatisticsMapper;

    @Resource
    private BaseSalaryConfigureMapper baseSalaryConfigureMapper;
    @Resource
    private CcSalesCommissionsConfMapper ccSalesCommissionsConfMapper;
    @Resource
    private ClubScopeConfigureMapper clubScopeConfigureMapper;
    @Resource
    private PersonalResourceAnalysisMapper personalResourceAnalysisMapper;


    @Override
    public List<CcStatistics> selectLeads(int ccId) {
        return ccStatisticsMapper.selectLeads(ccId);
    }

    @Override
    public List<CcStatistics> selectDemo(int ccId) {
        return ccStatisticsMapper.selectDemo(ccId);
    }

    @Override
    public List<CcStatistics> selectOrders(int ccId) {
        return ccStatisticsMapper.selectOrders(ccId);
    }

    @Override
    public List<CcStatistics> selectMaxRatio() {
        return ccStatisticsMapper.selectMaxRatio();
    }

    @Override
    public List<CcStatistics> ldorStatistics(int ccId) {
        //存放 最终合并的 数据
        List<CcStatistics> newList = new ArrayList<CcStatistics>();
        //订单
        List<CcStatistics> orders = this.selectOrders(ccId);
        //demo
        List<CcStatistics> demo = this.selectDemo(ccId);
         //例子
        List<CcStatistics> leads = this.selectLeads(ccId);
        // 6:SM、 7:OS 9:OFADS 三个渠道合并
        List<CcStatistics>  sooList = new ArrayList<CcStatistics>();
        //除了  2：ref  其余的集合
        List<CcStatistics> exceptRefList = new ArrayList<CcStatistics>();
         //demo除以ordes 筛选出出最大数  maxRatio
        List<CcStatistics> MaxRatioList = this.selectMaxRatio();
        //统计 os sm ofads 三个合计mkt  渠道最大 的ratio   demo/orders  (demo数除订单数量)
        Double mergeSooMaxRatio =  ccStatisticsMapper.mergeSooMaxRatio();
        //统计 除了 ref 其他渠道最大 的ratio   demo/orders  (demo数除订单数量)
        Double mergeExceptRefaxRatio =  ccStatisticsMapper.MargeExceptRefMaxRatio();
        /*所有 渠道查询*/
        List<CcStatistics> typeList = ccStatisticsMapper.selectType();
        //各个渠道 统计数据合并  最外层  是渠道list
        //放入本月 每一天各个渠道 统计后的数据
        for(CcStatistics cs :typeList){
            //每个 渠道  例子统计合并
            for(CcStatistics csLeads : leads){
                if(cs.getId() == csLeads.getId()){
                    cs.setLeadsCounter(csLeads.getLeadsCounter());
                }
            }
            //渠道（大渠道web） id 为 6、7、8  数据合并  合并之后再计算
            if(cs.getId() == 6 || cs.getId() == 7 || cs.getId() == 9){
                sooList.add(cs);
            }
            //渠道不等于2  其余渠道数据合并（mkt大渠道）合并之后再计算
            if(cs.getId() != 2){
                exceptRefList.add(cs);
            }
            //demo  统计
            for(int j = 0;j<demo.size();j++){
                if(cs.getId() == demo.get(j).getId()){
                    cs.setDemoCounter(demo.get(j).getDemoCounter());
                 }
            }
            //订单统计
            for(int k = 0;k<orders.size();k++){
                if(cs.getId() == orders.get(k).getId()){
                    cs.setOrderCounter(orders.get(k).getOrderCounter());
                }
            }
            //各个 渠道最大的 RatioList 统计
            for(int j = 0;j<MaxRatioList.size();j++){
                 if(cs.getId() == MaxRatioList.get(j).getId()){
                     cs.setMaxRatio(MaxRatioList.get(j).getMaxRatio());
                 }
            }
            //放入新的 list
           newList.add(cs);
        }
        //渠道（大渠道web） id 为 6、7、8  数据合并
        if(sooList != null && sooList.size()>0){
            CcStatistics ccStatistics = new CcStatistics();
            int leadsMerge = 0;
            int demoMerge = 0;
            int orderMerge = 0;
            for(int i = 0 ;i<sooList.size();i++){
                //累加每个渠道数据
                leadsMerge+=sooList.get(i).getLeadsCounter();
                demoMerge +=sooList.get(i).getDemoCounter();
                orderMerge+=sooList.get(i).getOrderCounter();
            }
            //放入累加每个渠道数据
            ccStatistics.setChinnelName("Web");
            ccStatistics.setOrderCounter(orderMerge);
            ccStatistics.setDemoCounter(demoMerge);
            ccStatistics.setLeadsCounter(leadsMerge);
            ccStatistics.setMaxRatio(mergeSooMaxRatio);
            //放入新的 list
            newList.add(ccStatistics);
        }
        //渠道不等于2  其余渠道数据合并（mkt大渠道）合并之后再计算
        if(exceptRefList != null && exceptRefList.size()>0){
            CcStatistics ccStatistics = new CcStatistics();
            int leadsMerge = 0;
            int demoMerge = 0;
            int orderMerge = 0;
            for(int i = 0 ;i<exceptRefList.size();i++){
                //每个渠道数据
                leadsMerge+=exceptRefList.get(i).getLeadsCounter();
                demoMerge +=exceptRefList.get(i).getDemoCounter();
                orderMerge+=exceptRefList.get(i).getOrderCounter();
            }
            //放入累加每个渠道数据
            ccStatistics.setChinnelName("MKT");
            ccStatistics.setOrderCounter(orderMerge);
            ccStatistics.setDemoCounter(demoMerge);
            ccStatistics.setLeadsCounter(leadsMerge);
            ccStatistics.setMaxRatio(mergeExceptRefaxRatio);
            //放入新的 list
            newList.add(ccStatistics);
        }
        //所有数据合并 在 新的list
        return newList;
    }

    /**
     *  dsr  cc 个人  （例子  demo  总金额  定金 订单数）详细数据展示
     * @param ccId
     * @return
     */
    @Override
    public List<CcStatistics> dsrList(int ccId) {
         //cc  个人 demo  统计
        List<CcStatistics> dsrDemo = ccStatisticsMapper.dsrDemo(ccId);
        //cc  个人 订单  总金额统计
        List<CcStatistics> dsrOrders = ccStatisticsMapper.dsrOrders(ccId);
        //cc  个人 成单定金  统计
        List<CcStatistics> dsrCcDeposit = ccStatisticsMapper.dsrOrders(ccId);
         //cc  个人 例子  统计
        List<CcStatistics> ccLeadsCounter = ccStatisticsMapper.ccLeadsCounter(ccId);

        List <CcStatistics> drsList = new ArrayList();
        //将 cc个人 各方面 销售数据 放入一个  集合
        //当前月份  天数  形成  日历数据
        for(int i = 1;i<getCurrentMonthDay()+1;i++){
            CcStatistics cs = new CcStatistics();
            cs.setDays(i);
            //demo  数据放入
            if(dsrDemo != null && dsrDemo.size()>0){
                for(int j = 0 ;j<dsrDemo.size();j++){
                    if(i==Integer.parseInt(dsrDemo.get(j).getTime())){
                        cs.setDemoCounter(dsrDemo.get(j).getDemoCounter());
                    }
                }
            }
            //订单统计数据 放入
            if(dsrOrders != null && dsrOrders.size()>0){
                for(int j = 0 ;j<dsrOrders.size();j++){
                    if(i==Integer.parseInt(dsrOrders.get(j).getTime())){
                        cs.setOrderCounter(dsrOrders.get(j).getOrderCounter());
                        cs.setTotalAmount(dsrOrders.get(j).getTotalAmount());
                    }
                }
            }
            //例子统计 数据放入
            if(ccLeadsCounter != null && dsrOrders.size()>0){
                for(int j = 0 ;j<ccLeadsCounter.size();j++){
                    if(i==Integer.parseInt(ccLeadsCounter.get(j).getTime())){
                        cs.setLeadsCounter(ccLeadsCounter.get(j).getLeadsCounter());
                    }
                }
            }
            //定金统计数据放入
            if(dsrCcDeposit != null && dsrCcDeposit.size()>0){
                 for(int j = 0 ;j<dsrCcDeposit.size();j++){
                    if(i==Integer.parseInt(dsrCcDeposit.get(j).getTime())){
                        cs.setDeposit(dsrCcDeposit.get(j).getDeposit());
                    }
                }
            }
            //最后合并在一起
          drsList.add(cs);
        }
        return drsList;
    }


    /*
     * @Author James
     * @Description 判断当我月有多少天
     * @Date 17:04 2018/11/5
     * @Param
     * @return
     **/
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    @Override
    public CcStatistics dsrDescription(int ccId) {
        return ccStatisticsMapper.dsrDescription(ccId);
    }

    @Override
    public List<CcStatistics> everyMonthSalary(int ccId) throws ParseException {
        Date dateArray[] = getSeasonDate(new Date());//(sdf.parse("2018-07-01"));
        List<CcStatistics> quarterlySalaryList = new ArrayList<>();
        List<CcStatistics> everyMonthSalary = ccStatisticsMapper.everyMonthSalary(ccId);
        for(int i = 0 ;i<dateArray.length;i++){
            CcStatistics css = new CcStatistics();
            //本季度日期  展示
            css.setDate(dateArray[i]);
            //本季度几个月销售数据放入
            if(everyMonthSalary != null && everyMonthSalary.size()>0){
                for(int j = 0; j<everyMonthSalary.size();j++){
                    if(sameDate(dateArray[i],everyMonthSalary.get(j).getDate())){
                        css.setTotalAmount(everyMonthSalary.get(j).getTotalAmount());
                    }
                }
            }
            quarterlySalaryList.add(css);
        }
        return quarterlySalaryList;
    }

    @Override
    public Integer thisQuarterlyResults(int ccId) {
        return ccStatisticsMapper.thisQuarterlyResults(ccId);
    }

    @Override
    public int calculationCcBaseSalary(int ccId,int result) {
        Integer confId = ccStatisticsMapper.ccBaseSalaryConf(ccId);
        //判断该cc 是否配置 底薪配置表  没有返回0  底薪计算为0
        if(confId == null){
            return 0;
        }
        List<BaseSalaryConfigure> baseSalaryConfList = baseSalaryConfigureMapper.confgiureList(confId);
        int baseSalary = 0;
        for(int i = 0;i<baseSalaryConfList.size();i++){
            //区间第一个 符号
            int minSign = baseSalaryConfList.get(i).getIntervalMinSign();
            //区间第二个值
            int minValue = baseSalaryConfList.get(i).getIntervalMinValue();
            //区间  第二个符号
            int maxSign = baseSalaryConfList.get(i).getIntervalMaxSign();
            //区间第一个值
            int maxValue = baseSalaryConfList.get(i).getIntervalMaxValue();
            //开始计算  该工资位于那个区间
            //判断 提成  是否位于 最后一个区间
            //1  2 判断 是否 带 ‘=’  符号
            if(minSign == 1 && maxSign == 1){
                if(minValue < result && result < maxValue){
                    baseSalary =  baseSalaryConfList.get(i).getBaseSalary();
                }
            }else if(minSign == 1 && maxSign == 2){
                if(minValue < result && result <= maxValue){
                    baseSalary =  baseSalaryConfList.get(i).getBaseSalary();
                }
            }else if(minSign == 2 && maxSign == 1){
                if(minValue <= result && result < maxValue){
                     baseSalary =  baseSalaryConfList.get(i).getBaseSalary();
                }
            }else if(minSign == 2 && maxSign == 2){
                if(minValue <= result && result <= maxValue){
                     baseSalary =  baseSalaryConfList.get(i).getBaseSalary();
                }
            }
        }
        return baseSalary;
    }

    @Override
    public BigDecimal calculationCcSalesPercentage(int ccId, int result){
        Integer confId = ccStatisticsMapper.ccSalesCommissionConfId(ccId);
        //判断该cc 是否配置 提成配置表  没有返回0  提成计算为0
        if(confId == null){
            return new BigDecimal(0);
        }
        List<CcSalesCommissionsConf> salesCommissionsList = ccSalesCommissionsConfMapper.confgiureList(confId);
        BigDecimal salesCommissionPercentage = new BigDecimal(0);
        for(int i = 0;i<salesCommissionsList.size();i++){
            //区间第一个 符号
            int minSign = salesCommissionsList.get(i).getIntervalMinSign();
            //区间第二个值
            int minValue = salesCommissionsList.get(i).getIntervalMinValue();
            //区间  第二个符号
            int maxSign = salesCommissionsList.get(i).getIntervalMaxSign();
            //区间第一个值
            int maxValue = salesCommissionsList.get(i).getIntervalMaxValue();
            //开始计算  该工资位于那个区间
//            if(i == salesCommissionsList.size()-1 && maxSign==0){
//                //最后一个区间  且只有 第一个 (例如：5000<X ||  5000<=X  )
//                if(minSign == 1){
//                    if(result>minValue){
//                        salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
//                    }
//                }else{
//                    if(result >= minValue){
//                         salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
//                    }
//                }
//            }
            if(minSign == 1 && maxSign == 1){
                if(minValue < result && result < maxValue){
                    salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
                }
            }else if(minSign == 1 && maxSign == 2){
                if(minValue < result && result <= maxValue){
                    salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
                }
            }else if(minSign == 2 && maxSign == 1){
                if(minValue <= result && result < maxValue){
                     salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
                }
            }else if(minSign == 2 && maxSign == 2){
                if(minValue <= result && result <= maxValue){
                     salesCommissionPercentage =  salesCommissionsList.get(i).getPercentage();
                }
            }
        }
        return salesCommissionPercentage;
    }

    @Override
    public Integer lastQuarterlyResults(int ccId) {
        return ccStatisticsMapper.lastQuarterlyResults(ccId);
    }

    @Override
    public Integer ccthisMonthSalesVolume(int ccId) {
        return ccStatisticsMapper.ccthisMonthSalesVolume(ccId);
    }


    /*
     * @Author James
     * @Description 判断 传入的两个日期是否 是同一天
     * @Date 17:05 2018/11/5
     * @Param
     * @return
     **/
    public static boolean sameDate(Date d1, Date d2){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
		//fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
		return fmt.format(d1).equals(fmt.format(d2));
	}

    /**
    * 取得季度月
    *
    * @param date
    * @return
    */
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

        /**
    *
    * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
    *
    * @param date
    * @return
    */
    public static int getSeason(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
            season = 1;
            break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
            season = 2;
            break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
            season = 3;
            break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
            season = 4;
            break;
            default:
            break;
        }
            return season;
    }


    /*
     * @Author James
     * @Description 计算  cc  属于哪个  销售俱乐部
     * @Date 15:41 2018/11/8
     * @Param
     * @return
     **/
    @Override
    public int calculationCcSalesClub(int ccId){
        Integer ccThreeMonthSalesVolume = this.ccThreeMonthSalesVolume(ccId);
        Integer confId = null;
        List<ClubConfigureInfo> effectiveClubInfList = clubScopeConfigureMapper.selectEffectiveClubInfo();
        if(effectiveClubInfList != null && effectiveClubInfList.size()>0){
            confId = clubScopeConfigureMapper.selectEffectiveClubInfo().get(0).getId();
        }
        //判断该cc 是否配置 提成配置表  没有返回0  提成计算为0
        if(confId == null){
            return 0;
        }
        List<ClubScopeConfigure> clubConfgiureScopeList = clubScopeConfigureMapper.clubConfgiureScopeList(confId);
        int salesClubType = 0;
        for(int i = 0;i<clubConfgiureScopeList.size();i++){
            //区间第一个 符号
            int minSign = clubConfgiureScopeList.get(i).getIntervalMinSign();
            //区间第二个值
            int minValue = clubConfgiureScopeList.get(i).getIntervalMinValue();
            //区间  第二个符号
            int maxSign = clubConfgiureScopeList.get(i).getIntervalMaxSign();
            //区间第一个值
            int maxValue = clubConfgiureScopeList.get(i).getIntervalMaxValue();
            //开始计算  该工资位于那个区间
            if(minSign == 1 && maxSign == 1){
                if(minValue < ccThreeMonthSalesVolume && ccThreeMonthSalesVolume < maxValue){
                    salesClubType =  clubConfgiureScopeList.get(i).getSalesClubType();
                }
            }else if(minSign == 1 && maxSign == 2){
                if(minValue < ccThreeMonthSalesVolume && ccThreeMonthSalesVolume <= maxValue){
                    salesClubType =  clubConfgiureScopeList.get(i).getSalesClubType();
                }
            }else if(minSign == 2 && maxSign == 1){
                if(minValue <= ccThreeMonthSalesVolume && ccThreeMonthSalesVolume < maxValue){
                     salesClubType =  clubConfgiureScopeList.get(i).getSalesClubType();
                }
            }else if(minSign == 2 && maxSign == 2){
                if(minValue <= ccThreeMonthSalesVolume && ccThreeMonthSalesVolume <= maxValue){
                     salesClubType =  clubConfgiureScopeList.get(i).getSalesClubType();
                }
            }
        }
        return salesClubType;
    }

    @Override
    public int ccThreeMonthSalesVolume(int ccId) {
        return ccStatisticsMapper.ccThreeMonthSalesVolume(ccId);
    }

    @Override
    public List<CcStatistics> PersonalResourceAnalysisList(int ccId) {
        //所有结果集拼接在一起返回的 list
        List<CcStatistics> list = new ArrayList();
        //cc 总人数
        int ccCounter =  personalResourceAnalysisMapper.selectCcCounter();
        //当前cc  leads 跟进数量统一
        CcStatistics leads = new CcStatistics();
        leads.setChinnelName("Leads跟进量");
        int ccLeadsCounter = personalResourceAnalysisMapper.ccLeadsCounter(ccId);
        leads.setLeadsCounter(ccLeadsCounter);
        //当前 cc  leads数量 超过了所少人
        int leadsCounterMoreThanThisCc = personalResourceAnalysisMapper.leadsCounterMoreThanThisCc(ccLeadsCounter);
        //打败了 多少 cc
        BigDecimal leadsCounter = new BigDecimal(leadsCounterMoreThanThisCc);
        BigDecimal Percentage = leadsCounter.divide(new BigDecimal(ccCounter),2,BigDecimal.ROUND_HALF_UP);
        leads.setPercentage(Percentage);
        leads.setDataType(1);
        //本月  cc leads跟进最多  统计
        int opponentMaxLeadsCounter = personalResourceAnalysisMapper.opponentMaxLeadsCounter();
        leads.setPercentage(percentage(leadsCounterMoreThanThisCc,ccCounter));
        leads.setTotalAmount(opponentMaxLeadsCounter);
        list.add(leads);

        //本月 cc  动态跟尽量
        CcStatistics stuDynamics = new CcStatistics();
        stuDynamics.setChinnelName("动态量");
        int stuDynamicsCounter = personalResourceAnalysisMapper.stuDynamicsCounter(ccId);
        stuDynamics.setDemoCounter(stuDynamicsCounter);
        //本月 cc  动态跟尽量  超过当前 cc  人数
        int stuDynamicsMoreThanThisCc = personalResourceAnalysisMapper.stuDynamicsMoreThanThisCc(stuDynamicsCounter);
        //本月 cc 动态跟尽量 最多的数量
        int maxStuDynamicsCounter = personalResourceAnalysisMapper.maxStuDynamicsCounter();
        stuDynamics.setTotalAmount(maxStuDynamicsCounter);
        //打败了 多少cc
        stuDynamics.setPercentage(percentage(stuDynamicsMoreThanThisCc,ccCounter));
        stuDynamics.setDataType(1);
        list.add(stuDynamics);

        //本月 当前 cc 在mkt渠道  跟进的demo课数量
        CcStatistics mkt = new CcStatistics();
        mkt.setChinnelName("已上Mkt DEMO");
        int mktDemoCounter = personalResourceAnalysisMapper.selectmktDemoCounter(ccId);
        mkt.setDemoCounter(mktDemoCounter);
        //本月 当前 cc  在mkt渠道  跟进的demo课数量 超过其他cc  的人数（打败了多少人）
        int mktDemoMoreThanThisCc = personalResourceAnalysisMapper.mktDemoMoreThanThisCc(mktDemoCounter);
        //打败cc
        mkt.setPercentage(percentage(mktDemoMoreThanThisCc,ccCounter));
        //本月在mkt渠道  跟进的demo课数量  最大码值
        int maxMktDemoCounter = personalResourceAnalysisMapper.maxMktDemoCounter();
        mkt.setTotalAmount(maxMktDemoCounter);
        mkt.setDataType(1);
        list.add(mkt);

        //当前 cc  在 ref pwic pwio 三个渠道  跟进 demo课数量 列表
        List<CcStatistics> rePwicPwioDemoList = personalResourceAnalysisMapper.rePwicPwioDemoList(ccId);
        for(CcStatistics cc : rePwicPwioDemoList){
            //ref 渠道
            if(cc.getId() == 2){
                CcStatistics ref = new CcStatistics();
                ref.setChinnelName("已上Ref DEMO");
                //ref 渠道demo课跟进量 超过多少个 cc
                int refNumberofCc = personalResourceAnalysisMapper.refPwicPwioMorethanThisCc(2,cc.getDemoCounter());
                //打败 多少 cc
                ref.setPercentage(percentage(refNumberofCc,ccCounter));
                //ref 渠道 demo课跟进量 最大值
                int refMaxDemoCounter = personalResourceAnalysisMapper.maxRefPwicPwioDemoCounter(2);
                ref.setTotalAmount(refMaxDemoCounter);
                //ref 渠道demo 课跟进量
                int refCounter = cc.getDemoCounter();
                ref.setDemoCounter(refCounter);
                ref.setDataType(1);
                list.add(ref);
            }
            //pwic  渠道
            if(cc.getId() == 4){
                //pwic 渠道demo课跟进量 超过多少个 cc
                CcStatistics pwic = new CcStatistics();
                pwic.setChinnelName("PWIC DEMO");
                int pwicNumberofCc = personalResourceAnalysisMapper.refPwicPwioMorethanThisCc(4,cc.getDemoCounter());
                 //打败 多少 cc
                pwic.setPercentage(percentage(pwicNumberofCc,ccCounter));
                //pwic 渠道 demo课跟进量 最大值
                int pwicMaxDemoCounter = personalResourceAnalysisMapper.maxRefPwicPwioDemoCounter(4);
                pwic.setTotalAmount(pwicMaxDemoCounter);
                 //ref 渠道demo 课跟进量
                int pwicCounter = cc.getDemoCounter();
                pwic.setDemoCounter(pwicCounter);
                pwic.setDataType(1);
                list.add(pwic);
            }
            //pwio 渠道
            if(cc.getId() == 5){
                //pwio 渠道demo课跟进量 超过多少个 cc
                CcStatistics pwio = new CcStatistics();
                pwio.setChinnelName("PWIO DEMO");
                int pwioNumberofCc = personalResourceAnalysisMapper.refPwicPwioMorethanThisCc(5,cc.getDemoCounter());
                 //打败 多少 cc
                pwio.setPercentage(percentage(pwioNumberofCc,ccCounter));
                //pwio 渠道 demo课跟进量 最大值
                int pwioMaxDemoCounter = personalResourceAnalysisMapper.maxRefPwicPwioDemoCounter(5);
                //pwio 渠道demo 课跟进量
                int pwioCount = cc.getDemoCounter();
                pwio.setDemoCounter(pwioCount);
                pwio.setDataType(1);
                list.add(pwio);
            }
        }
        //关单周期 统计 相关
        Map<String,Object> map = new HashMap();
        //第一P  关单周期平均天数  该cc关单周期平均天数 超过其他cc 关单周期平均天数 的 人数    最小 关单周期平均天数
        CcStatistics stP = new CcStatistics();
        stP.setChinnelName("第一P平均关单周期");
        BigDecimal stPCloseLeadsDays = personalResourceAnalysisMapper.ccCloseLeadsCycle(ccId,1);
        stP.setCloseLeadsDays(stPCloseLeadsDays);
        map.put("step",1);
        map.put("thisCcCounter",stPCloseLeadsDays);
        int stPNumberofCc = personalResourceAnalysisMapper.moreThanThisCcCloseLeadsCycle(map);
        //打败 多少 cc
        stP.setPercentage(percentage(stPNumberofCc,ccCounter));
        stP.setMaxDemoCounter(personalResourceAnalysisMapper.maxCcCloseLeadsCycle(1));
        stP.setDataType(2);
        list.add(stP);

        //第二P  关单周期平均天数  该cc关单周期平均天数 超过其他cc 关单周期平均天数 的 人数    最小 关单周期平均天数
        CcStatistics ondP = new CcStatistics();
        ondP.setChinnelName("第二P平均关单周期");
        BigDecimal ondPCloseLeadsDays = personalResourceAnalysisMapper.ccCloseLeadsCycle(ccId,2);
        ondP.setCloseLeadsDays(ondPCloseLeadsDays);
        map.clear();
        map.put("step",2);
        map.put("thisCcCounter",ondPCloseLeadsDays);
        int ondPNumberofCc = personalResourceAnalysisMapper.moreThanThisCcCloseLeadsCycle(map);
        ondP.setPercentage(percentage(ondPNumberofCc,ccCounter));
        ondP.setMaxDemoCounter(personalResourceAnalysisMapper.maxCcCloseLeadsCycle(2));
        ondP.setDataType(2);
        list.add(ondP);

        //第三P  关单周期平均天数 该cc关单周期平均天数 超过其他cc 关单周期平均天数 的 人数    最小 关单周期平均天数
        CcStatistics rdP = new CcStatistics();
        rdP.setChinnelName("第三P平均关单周期");
        BigDecimal rdPCloseLeadsDays = personalResourceAnalysisMapper.ccCloseLeadsCycle(ccId,2);
        rdP.setCloseLeadsDays(rdPCloseLeadsDays);
        map.clear();
        map.put("step",3);
        map.put("thisCcCounter",ondPCloseLeadsDays);
        int rdPNumberofCc = personalResourceAnalysisMapper.moreThanThisCcCloseLeadsCycle(map);
        rdP.setPercentage(percentage(rdPNumberofCc,ccCounter));
        rdP.setMaxDemoCounter(personalResourceAnalysisMapper.maxCcCloseLeadsCycle(3));
        rdP.setDataType(2);
        list.add(rdP);
        return list;
    }

    /*
     * @Author James
     * @Description 百分比计算公共方法
     * @Date 16:07 2018/11/21
     * @Param
     * @return
     **/
    public BigDecimal percentage(int  param1,int param2){
        BigDecimal percentage = new BigDecimal(param1).divide(new BigDecimal(param2),2,BigDecimal.ROUND_HALF_UP);
        percentage = percentage.multiply(new BigDecimal(100));
        return percentage;
    }
}
