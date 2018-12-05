package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Terwer
 */
@Entity
@Getter
@Setter
public class QuestionKnowledge implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 知识点名称
     */
    private String cnName;
    /**
     * 英文名
     */
    private String enName;
    /**
     * 排序
     */
    private String sort;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 关联题目ID集合
     */
    private List<Integer> relateQuestion;
    /**
     * 关联题目
     */
    private String relateQuestionString;
    /**
     * 第一级分类
     */
    private Integer rootId;
    /**
     * 第二级别分类
     */
    private Integer parentId;
    /**
     * 第三级分类
     */
    private Integer subId;
    /**
     * 教材类型
     */
    private Integer type;
    /**
     * 错词本ID集合
     */
    private String mistakeString;
    /**
     * 错词本ID集合
     */
    private String mistakeNameString;
    /**
     * 关联教材
     */
    private Integer relatedTextbooks;
    /**
     * 关联页码索引ID集合
     */
    private List<Integer> page;
    /**
     * 页码索引
     */
    private String pageString;
    /**
     * 关联页码ID集合
     */
    private List<String> pageName;
    /**
     * 页码
     */
    private String pageNameString;
    /**
     * 创建人
     */
    private String recorder;
    /**
     * 创建时间
     */
    private Date recordTime;
}