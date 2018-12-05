package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Terwer
 */
@Entity
@Getter
@Setter
public class QuestionGrade implements Serializable {
    private Integer id;

    private String cnName;

    private String enName;

    private String sort;

    private Integer state;
}