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
public class Stores {

    private Integer id;

    private String name;

    private String enName;

    private String logoImg;

    private Integer homeFlag;

    private Integer roleFlag;

    private Integer shareAreaId;
}
