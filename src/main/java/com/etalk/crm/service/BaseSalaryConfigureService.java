package com.etalk.crm.service;

import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.BaseSalaryInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/10/23 10:58
 * @Description:
 */
public interface BaseSalaryConfigureService {
     /*
     * @Author James
     * @Description  薪资配置列表
     * @Date 13:33 13:33
     * @Param
     * @return
     **/
   List<BaseSalaryInfo> salaryInfoList();


   /*
    * @Author James
    * @Description基本薪资 相关配置
    * @Date 13:40 13:40
    * @Param
    * @return
    **/
   List<BaseSalaryConfigure> confgiureList(int baseSalaryId);
    /*
    * @Author James
    * @Description 根据  id查询  底薪配置表
    * @Date 9:55 9:55
    * @Param
    * @return
    **/
   BaseSalaryInfo selectsalaryInfoById (int baseSalaryId);
    /**
     * 提交 编辑 或者 添加 数据
     * @param baseSalaryInfo
     * @param baseSalaryInfo
     * @return
     */
   int submitsalaryConfigure(List<Map> paramList, BaseSalaryInfo baseSalaryInfo);

   /*
    * @Author James
    * @Description 修改底薪配置表相关信息
    * @Date 11:45 2018/10/24
    * @Param
    * @return
    **/
   int eidtSalaryInfo(BaseSalaryInfo baseSalaryInfo);
}
