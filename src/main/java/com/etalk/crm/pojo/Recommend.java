package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Recommend implements Serializable {
    private int id;

    private String name;

    private String imgUrl;
}
