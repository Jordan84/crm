package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/** case share 标签分类
 * @Auther: James
 * @Date: 2018/6/25 10:23
 * @Description:
 */
@Entity
@Getter
@Setter
public class CaseShareLabel implements Serializable {
    private int id;

    private String name;

    private int state;
}
