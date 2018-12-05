package com.etalk.crm.service;

import com.etalk.crm.pojo.CcSalesCommissionsConf;
import com.etalk.crm.pojo.SalesCommissionInfo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/10/26 14:08
 * @Description:
 */
public interface CcSalesCommissionConfService {
     /*
     * @Author James
     * @Description 销售提成配置列表  信息
     * @Date 14:44 2018/10/26
     * @Param
     * @return
     **/
    List<SalesCommissionInfo> salesCcommissionInfoList();

    /*
     * @Author James
     * @Description 查询某个销售提成配置列表信息
     * @Date 14:44 2018/10/26
     * @Param
     * @return
     **/
    SalesCommissionInfo selectSCIById(int salesCcommissionId);

     /*
    * @Author James
    * @Description 根据  配置信息表   id 判断 区间配置列表  是否存在
    * @Date 19:19 2018/10/24
    * @Param
    * @return
    **/
   int selectConfigureById(int salesCcommissionId);

    /*
    * @Author James
    * @Description提成 相关配置
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   List<CcSalesCommissionsConf> confgiureList(int salesCcommissionId);

//     /*
//    * @Author James
//    * @Description删除提成配置列表
//    * @Date 11:44 2018/10/24
//    * @Param
//    * @return
//    **/
//   int delconfgiureList(int salesCcommissionId);

    /*
     * @Author James
     * @Description 提交 编辑 或者 添加 数据
     * @Date 11:45 2018/10/24
     * @Param
     * @return
     **/
   int submitccSalesCommissionConf(List<Map> paramList, SalesCommissionInfo SalesCommissionInfo);

   /*
    * @Author James
    * @Description 修改提成配置表相关信息
    * @Date 11:47 2018/10/24
    * @Param
    * @return
    **/
   int eidtCommissionInfo(SalesCommissionInfo SalesCommissionInfo);
}
