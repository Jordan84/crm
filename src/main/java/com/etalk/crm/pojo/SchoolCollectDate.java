package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Terwer
 * @date 2018/06/19
 */
@Getter
@Setter
public class SchoolCollectDate {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 学校ID
     */
    private Integer schoolId;
    /**
     * 采集日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date collectDate;
}
