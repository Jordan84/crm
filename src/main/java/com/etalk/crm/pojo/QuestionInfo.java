package com.etalk.crm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Terwer
 * @Date 2018/11/10 10:53
 * @Version 1.0
 * @Description 题目信息
 **/
@Getter
@Setter
public class QuestionInfo {
    /**
     * 版本号
     */
    @JsonIgnore
    private Integer version;
    /**
     * 难度级别
     */
    @JsonProperty("level")
    private Integer qlevel;
    /**
     * 对应难度级别的题目数目
     */
    @JsonProperty("count")
    private Integer qcount;
}
