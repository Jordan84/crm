package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: James
 * @Date: 2018/10/8 16:25
 * @Description:成绩管理
 */
@Entity
@Getter
@Setter
public class AchievementManagement implements Serializable {
    private int id ;

    private int personId;

    private int addUserId;

    private int ccId;

    private int sscId;
    /**
     * 学期
     */
    private int term;

    /**
     * 总分
     */
    private int totalScore;

    private Date addTime;
    /**
     * 等级分数  例如  A+
     */
     private int levelScore;

    /**
     * 数字分数
     */
    private BigDecimal score;

    /**
     * 年度
     */
    private Date year;

    private String time;

    /**
     * 考试期   1：期中  2：期末
     */
    private  int testTime;

    /**
     * 分数 类型
     */
    private int scoreType;

    private String  startDate;

    private String endDate;

    /**
     * 学员登录名
     */
    private String studentLogin;

    private String cnName;

    private String ccName;

    private String sscName;

    private String levelName;

    private String totalScoreName;

    private int gradeId;

    private int wechatAchievementMsgId;

    private int pushTimes;

    private int alreadyPushTimes;
}
