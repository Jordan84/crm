package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 * @date 2018/06/14
 */
@Getter
@Setter
public class SchoolInfo {
    /**
     * 学校ID
     */
    private Integer id;
    /**
     * 区域编码
     */
    private Integer areaCode;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 是否是学校
     */
    private Integer isSchool;
}
