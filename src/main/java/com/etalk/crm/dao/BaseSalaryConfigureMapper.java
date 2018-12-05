package com.etalk.crm.dao;

import com.etalk.crm.pojo.BaseSalaryConfigure;
import com.etalk.crm.pojo.BaseSalaryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/10/23 10:44
 * @Description:
 */
@Mapper
public interface BaseSalaryConfigureMapper {
    
    /*
     * @Author James
     * @Description  薪资配置列表
     * @Date 11:44 2018/10/24
     * @Param 
     * @return 
     **/
   List<BaseSalaryInfo> salaryInfoList();


   /*
    * @Author James
    * @Description基本薪资 相关配置
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   List<BaseSalaryConfigure> confgiureList(@Param("baseSalaryId")int baseSalaryId);

   /*
    * @Author James
    * @Description 根据  id查询  底薪配置表
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   BaseSalaryInfo selectsalaryInfoById (@Param("baseSalaryId")int baseSalaryId);

     /*
    * @Author James
    * @Description删除底薪配置列表
    * @Date 11:44 2018/10/24
    * @Param
    * @return
    **/
   int delconfgiureList(@Param("baseSalaryId")int baseSalaryId);

   /*
    * @Author James
    * @Description 批量插入  各个区间底薪配置
    * @Date 11:44 2018/10/24
    * @Param 
    * @return 
    **/
   int addConfgiureList(@Param("list")List<BaseSalaryConfigure> list);
    
    /*
     * @Author James
     * @Description 添加底薪配置表相关信息
     * @Date 11:45 2018/10/24
     * @Param 
     * @return 
     **/
   int addSalaryInfo(BaseSalaryInfo baseSalaryInfo);
   
   /*
    * @Author James
    * @Description 修改底薪配置表相关信息
    * @Date 11:47 2018/10/24
    * @Param 
    * @return 
    **/
   int eidtSalaryInfo(BaseSalaryInfo baseSalaryInfo);
   
   
   /*
    * @Author James
    * @Description 根据  配置信息表   id 判断 区间配置列表  是否存在
    * @Date 19:19 2018/10/24
    * @Param 
    * @return 
    **/
   int selectConfigureById(@Param("baseSalaryId")int baseSalaryId);

    /*
     * @Author James
     * @Description 底薪 配置信息表  表名是否重复
     * @Date 16:51 2018/11/11
     * @Param clubInfoName  俱乐部配置表 名字 id 配置表  id
     * @return int
     **/
    int selectbaseSalaryInfoByName(BaseSalaryInfo baseSalaryInfo);
}
