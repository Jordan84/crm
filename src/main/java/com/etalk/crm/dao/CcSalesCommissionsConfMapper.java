package com.etalk.crm.dao;

import com.etalk.crm.pojo.CcSalesCommissionsConf;
import com.etalk.crm.pojo.SalesCommissionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/10/26 14:22
 * @Description:销售提成
 */
@Mapper
public interface CcSalesCommissionsConfMapper {
    
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
    SalesCommissionInfo selectSCIById(@Param("salesCcommissionId")int salesCcommissionId);

     /*
    * @Author James
    * @Description 根据  配置信息表   id 判断 区间配置列表  是否存在
    * @Date 19:19 2018/10/24
    * @Param
    * @return
    **/
   int selectConfigureById(@Param("salesCcommissionId")int salesCcommissionId);

    /*
    * @Author James
    * @Description提成 相关配置
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   List<CcSalesCommissionsConf> confgiureList(@Param("salesCcommissionId")int salesCcommissionId);

     /*
    * @Author James
    * @Description删除提成配置列表
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   int delconfgiureList(@Param("salesCcommissionId")int salesCcommissionId);

    /*
    * @Author James
    * @Description 批量插入  各个区间提成配置
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   int addConfgiureList(@Param("list")List<CcSalesCommissionsConf> list);

    /*
     * @Author James
     * @Description 添加提成配置表相关信息
     * @Date 11:45 2018/10/24
     * @Param
     * @return
     **/
   int addCommissionInfo(SalesCommissionInfo SalesCommissionInfo);

   /*
    * @Author James
    * @Description 修改提成配置表相关信息
    * @Date 11:47 2018/10/24
    * @Param
    * @return
    **/
   int eidtCommissionInfo(SalesCommissionInfo SalesCommissionInfo);


   /*
     * @Author James
     * @Description 提成配置信息表  表名是否重复
     * @Date 16:51 2018/11/11
     * @Param clubInfoName  俱乐部配置表 名字
     * @return int
     **/
    int selectCommissionInfoByName(SalesCommissionInfo SalesCommissionInfo);
}
