package com.etalk.crm.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/11/7 15:36
 * @Version 1.0
 * @Description 所学课程
 **/
@Getter
@Setter
public class LessonItem {
    /**
     * 课程ID
     */
    private Integer lessonId;
    /**
     * 知识点ID
     */
    private Integer qknowledgeId;
    /**
     * 单元ID
     */
    private Integer unitId;
    /**
     * 上课时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date classTime;
}
