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
public class QuestionTypes implements Serializable {

    private Integer id;

    private String cnName;

    private String enName;

    private Integer state;
}