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
public class User {

    private Integer id;

    private String loginName;

    private String enName;

    private String cnName;

    private Integer roleId;

    private Integer robCase;

    private Integer accountId;

    private Integer storesId;
}
