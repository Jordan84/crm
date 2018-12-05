package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author Terwer
 * @Date 2018/10/29 14:32
 * @Version 1.0
 * @Description 知识点错词关联
 **/
@Getter
@Setter
public class QuestionKnowledgeMistake {
    private Integer qknowledgeId;
    private Integer mistakeId;
    private String recorder;
    private Date recordTime;
}
