package com.etalk.crm.dao;


import com.etalk.crm.pojo.AchievementManagement;
import com.etalk.crm.pojo.GradeLevel;
import com.etalk.crm.pojo.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @author: James
 * @Date: 2018/10/8 16:36
 * @Description:
 */
@Mapper
public interface AchievementManagementMapper {
    /**
     * 添加成绩  列表
     * @param achievementManagement
     * @return
     * @auther: James
     * @date:   2018-10-8
     */
    int addAchievement(AchievementManagement achievementManagement);

     /**
      * 功能描述:学员成绩等级列表
      * @param:
      * @return:
      * @auther: James
      * @date:  2018-10-11
      */
     List<GradeLevel> gradeLevelList();
     /**
      * 功能描述:学员成绩  分制列表
      * @param:
      * @return:
      * @auther: James
      * @date:  2018-10-15
      */
     List<GradeLevel> gradeTypeList();

      /**
      * 功能描述:判断 学员某阶段成绩信息 是否存在
      * @param:
      * @return:
      * @auther: James
      * @date:  2018-10-12
      */
     int isAchievementExist(AchievementManagement achievementManagement);


     /**
      * @Author James
      * @Description 查询微信成绩收集 参数查询
      * @Date 16:31 2018/10/30
      * @Param wechatAchievementMsgId  消息推送  id
      * @return
      **/
     AchievementManagement selectAchievementCollectionParam(@Param("wechatAchievementMsgId")int wechatAchievementMsgId);

      /**
     * 功能描述:成绩管理列表
     * @param: achievementManagement
     * @return: List
     * @auther: James
     * @date:  2018-10-8
     */
    List<AchievementManagement> achievementList(Map<String,Object> paramMap);
    /**
     * 功能描述:删除添加的成绩
     * @param: id
     * @return: int
     * @auther: James
     * @date:   2018-10-8
     */
    int delAchievementManagement(@Param("id") int id);

    /**
     * 功能描述:根据id  查询  某条成绩
     * @param:id
     * @return:
     * @auther: James
     * @date:  2018-10-10
     */
    AchievementManagement selectById(@Param("id") int id);

     /**
     * 根据  输入的 关键 字 查询 学员 下拉列表
     * @param
     * @param loginName
     * @return
     */
     List<Person> personList(@Param("loginName")String loginName);
      /**
      * 功能描述:提交  编辑 学员成绩信息
      * @param:
      * @return:
      * @auther: James
      * @date:  2018-10-12
      */
     int ajaxachievementEdit(AchievementManagement achievementManagement);

      /**
      * 功能描述:学员成绩手机  微信消息推送
      * @param:
      * @return:
      * @auther: James
      * @date:  2018-10-30
      */
     int addWechatAchievementMsg(AchievementManagement achievementManagement);


     /**
      * @Author James
      * @Description 判断  微信成绩收集   消息推送   是否 已推送
      * @Date 15:54 2018/10/30
      * @Param
      * @return
      **/
     int selectWechatAchievementMsg(AchievementManagement achievementManagement);

     /*
      * @Author James
      * @Description 学员成绩收集 微信消息列表
      * @Date 19:13 2018/11/13
      * @Param 
      * @return 
      **/
     List<AchievementManagement> wechatAchievementMsgList(@Param("time")String time);

     /*
      * @Author James
      * @Description 查询 某条微信推送消息  信息
      * @Date 19:13 2018/11/13
      * @Param
      * @return
      **/
     AchievementManagement selectWechatMsgById(@Param("id")int wechatAchievementMsgId);
     
     
     /*
      * @Author James
      * @Description 修改  学员成绩收集  微信消息推送已推送次数
      * @Date 11:21 2018/11/14
      * @Param 
      * @return 
      **/
     int updateWechatMsgById(@Param("id")int wechatAchievementMsgId);
}
