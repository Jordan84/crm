package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Author Terwer
 * @Date 2018/10/24 15:07
 * @Version 1.0
 * @Description 教材
 **/
@Getter
@Setter
public class Textbooks {
    private Integer id;
    private List<Integer> listId;
    private Integer storesId;
    /**
     * 教材名称
     */
    private String name;
    /**
     * 教材英文名称
     */
    private String enname;
    /**
     * 教材地址
     */
    private String address;
    /**
     * 级别（学生英语等级对应级别）
     */
    private Integer level;
    /**
     * 年级
     */
    private Integer grade;
    /**
     * 教材最大页数
     */
    private Integer maxpage;

    /**
     * 教材关联的知识点页数
     */
    private String pageString;

    private Integer state;
    /**
     * 教材使用城市
     */
    private String city;
    /**
     * 教材出版社
     */
    private String publishing;
    /**
     * 使用人群分类（1中小学同步教材，2，成人教材，3热门教材
     */
    private Integer type;
    /**
     * 上完教材所需课时
     */
    private Integer classPeriods;
    /**
     * 推荐内容
     */
    private String recommended;
    /**
     * 适用人群
     */
    private String suitableCrowd;
    /**
     * 教材简介
     */
    private String introduction;
    /**
     * 登记人
     */
    private String recorder;
    private Date recordtime;
    /**
     * 最后更新配置文件时间
     */
    private Date updateConfigTime;
    /**
     * 排序方法
     */
    private String orderByClause;
    /**
     * 对应老师级别
     */
    private int tealevel;
    /**
     * 所属机构名称
     */
    private String storesName;
    /**
     * 关联教材分类id
     */
    private int textbooksClassifyId;
    /**
     * 关联教材教材分类二级id
     */
    private int textbooksClassifyParentId;
    /**
     * 关联教材教材分类一级id
     **/
    private int textbooksClassifyRootId;
    /**
     * 教材所属年级或级别
     **/
    private int classLevel;
}