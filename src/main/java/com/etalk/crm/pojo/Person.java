package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Jordan
 */
@Entity
@Getter
@Setter
public class Person {

    private Integer id;

    private String loginName;

    private String school;

    private int age;

    private int sex;
    /**
     * 学生姓名
     */
    private String cnName;

    /**
     * 学员家庭情况
     */
    private String familyInformation;

    /**
     * 学习困难
     */
    private String learningDifficulty;

    private String phone;

    private int grade;

    private int sscId;

    private int userId;

    private String ccName;

    private String sscName;

    private int ccId;

    private int storesId;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 推荐人Id
     */
    private Integer refererId;
}
