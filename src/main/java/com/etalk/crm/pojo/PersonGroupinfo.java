package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PersonGroupinfo implements Serializable {
    private Integer id;
    /**
     * 老师分组id
     */
    private Integer groupId;
    /**
     * 老师登录名
     */
    private String loginName;
    /**
     * 登记时间
     */
    private Date recordTime;
    private PersonGroup personGroup;
    private Person person;
}