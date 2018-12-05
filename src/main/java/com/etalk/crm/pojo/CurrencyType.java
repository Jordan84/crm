package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class CurrencyType implements Serializable {
    private Integer id;
    private String cnName;
    private String enName;
    private Integer state;
    private String creater;
    private Date createTime;

}