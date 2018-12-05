package com.etalk.crm.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * oc课课后问卷调查
 * @author James
 */
@Entity
@Getter
@Setter
public class QuestionnaireOc implements Serializable {
    /**
     *主键ID
     */
    private Integer id;

    /**
     * oc问卷主键
     */
    private Integer paperId;

    /**
     * 学员ID
     */
    private Integer personId;

    /**
     * 添加人ID
     */
    private  Integer addUserId;

    /**
     * 添加人登录名
     */
    private String addUserName;

    /**
     * 学员登录名
     */
    private String loginName;

    /**
     * 成绩是否有效
     */
    private Integer state;

    /**
     * 成绩
     */
    private BigDecimal grade;

    /**
     * 提交时间
     */
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    /**
     * oc 课id
     */
    private  Integer lessonId;

    /**
     * oc课时间
     */
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private  Date releaseTime;

    /**
     * 上课老师
     */
    private String teacher;

    /**
     * 学员登录名
     */
    private String studentLogin;

    /**
     * ssc 登录名
     */
    private String sscName;

    private String personName;
}
