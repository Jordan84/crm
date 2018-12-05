package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Terwer
 * @date 2018/06/18
 */
@Getter
@Setter
public class SchoolCollectTime {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 学校ID
     */
    private Integer schoolId;
    /**
     * 采集时间
     */
    @DateTimeFormat(pattern="HH:mm")
    private Date collectTime;
}
