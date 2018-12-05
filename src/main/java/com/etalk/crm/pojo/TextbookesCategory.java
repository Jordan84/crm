package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/10/24 16:51
 * @Version 1.0
 * @Description 教材类别实体类
 **/
@Getter
@Setter
public class TextbookesCategory {
    /**
     * id
     */
    private int id;
    /**
     * 类别名称
     */
    private String categoryName;
    /**
     * 创建者
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createrTime;
    /**
     * 修改者
     */
    private String updater;
    /**
     * 修改时间
     */
    private Date updateTime;
    /***/
    private int level;
    /**
     * 备注
     */
    private String remark;
    /**
     * 组别名称
     */
    private String groupName;
    /**
     * 根级分类id
     */
    private int rootId;
    /**
     * 父级id
     */
    private int parentId;
    /**
     * 分类级别
     */
    private String classifyLevel;
    /**
     * 是否有效 1为有效0为无效
     **/
    private String state;
    /**
     * 根级分类名称
     */
    private String rootName;
    /**
     * 父级分类名称
     */
    private String parentName;
    /**
     * 是否删除0为已删除1为正常
     */
    private String type;
}
