package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 17:23
 * @Description:caseshare
 */
@Entity
@Getter
@Setter
public class CaseShareManage implements Serializable {
    private int id;

    private int addUserId;
    /**
     *来源
     */
    private int sourceTypeId;
    /**
     * 性别
     */
    private int sex;
    /**
     * 创建时间
     */
    private Date createTime;
      /**
     * 更新时间
     */
    private Date updateTime;
      /**
     * 创建人
     */
    private String addUserName;

    /**
     * 访问量
     */
    private Integer pageViews;
    /**
     * 点赞总计
     */
    private Integer pointCount;

    private String title;
      /**
     * 学生姓名
     */
    private String cnName;
      /**
     * 年级
     */
    private int gradeId;
      /**
     * 年龄
     */
    private int age;

    private String school;
      /**
     * 套餐
     */
    private int packageId;
      /**
     * 家庭信息
     */
    private String familyInformation;

     /**
     * 学习困难
     */
    private String learningDifficulty;
     /**
     * case  分享
     */
    private String caseShare;

    /**
     * 收获
     */
    private String gain;

    /**
     * 注意
     */
    private String attention;

    /**
     * 分类名称
     */
    private String TypeName;
    /**
     * 年级
     */
    private String gradeName;

    /**
     * 学员id
     */
    private int personId;
    /**
     * 标签
     */
    private String labelStr;

    /**
     * 套餐 名
     */
    private String packageName;
    /**
     * 学员登录名
     */
    private String  loginName;
    /**
     * 发布时间排序
     */
    private Integer publishSort;
    /**
     * 浏览量排序字段
     */
    private Integer pageViewsSort;
    /**
     * 点赞数量排序字段
     */
    private Integer thumpupSort;

    /**
     * 标签  list
     */
    private List<Integer> labelList;

    /**
     * 状态
     */
    private int state;

    /**
     * 审核不通过原因
     */
    private String rejectReason;

    /**
     * 推荐  精选 标识
     */
    private String recommend;

    /**
     * 推荐  精选  字符转 list
     */
    private List<Recommend> recommendList;
}
