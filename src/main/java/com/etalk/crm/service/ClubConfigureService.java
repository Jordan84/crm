package com.etalk.crm.service;

import com.etalk.crm.pojo.ClubConfigureInfo;
import com.etalk.crm.pojo.ClubScopeConfigure;
import com.etalk.crm.pojo.GradeLevel;

import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2018/11/7 16:11
 * @Description:
 */
public interface ClubConfigureService {
     /*
     * @Author James
     * @Description  销售俱乐部  配置表列表 查询
     * @Date 15:44 2018/11/7
     * @Param
     * @return
     **/
    List<ClubConfigureInfo> clubConfigureInfoList();

    /*
     * @Author James
     * @Description 俱乐部配置表现骨干信息
     * @Date 15:48 2018/11/7
     * @Param clubConfigurInfoId 俱乐部配置表id
     * @return
     **/
    ClubConfigureInfo clubConfigureInfoById(int clubConfigurInfoId);


    /*
     * @Author James
     * @Description 查询某个配置表   配置区间是否存在
     * @Date 15:52 2018/11/7
     * @Param clubConfigureId  配置表 id
     * @return
     **/
    int selectConfigureById(int clubConfigureId);


    /*
     * @Author James
     * @Description 某个 俱乐部配置表  区间配置 区间列表
     * @Date 15:55 2018/11/7
     * @Param clubConfigureId  配置表 id
     * @return
     **/
    List<ClubScopeConfigure>clubConfgiureScopeList(int clubConfigureId);

    /*
     * @Author James
     * @Description 删除 某个俱乐部 配置表 区间列表
     * @Date 15:58 2018/11/7
     * @Param clubConfigureId  配置表 id
     * @return
     **/
    int delconfgiureList(int clubConfigureId);

    /*
     * @Author James
     * @Description 插入某个配置表  区间列表
     * @Date 16:00 2018/11/7
     * @Param list  某个配置表  区间列表
     * @return int
     **/
    int addConfgiureList(List<ClubScopeConfigure> list);


    /*
     * @Author James
     * @Description  添加  销售俱乐部  配置表相关信息
     * @Date 16:08 2018/11/7
     * @Param clubConfigureInfo  销售俱乐部  配置表相关信息
     * @return
     **/
    int addClubConfigureInfo(ClubConfigureInfo clubConfigureInfo);

    /*
     * @Author James
     * @Description 修改销售俱乐部  配置表相关信息
     * @Date 16:09 2018/11/7
     * @Param clubConfigureInfo  销售俱乐部  配置表相关信息
     * @return
     **/
    int eidtClubConfigureInfo(ClubConfigureInfo clubConfigureInfo);

    /**
     * 提交 编辑 或者 添加 数据
     * @param paramList
     * @param clubConfigureInfo
     * @return
     */
   int subclubScopeConfigure(List<Map> paramList, ClubConfigureInfo clubConfigureInfo);

    /*
     * @Author James
     * @Description查询 销售俱乐部 类别
     * @Date 19:16 2018/11/7
     * @Param
     * @return
     **/
    List<GradeLevel> selectClubType();

    /*
     * @Author James
     * @Description selectClubById
     * @Date 16:18 2018/11/8
     * @Param clubType 俱乐部id
     * @return
     **/
    GradeLevel selectClubById(int clubType);
}
