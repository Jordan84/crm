package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonGroup implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer storesId;
    private String name;
    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 分组说明
     */
    private String remark;
    /**
     * 状态（是否显示，1显示0不显示）
     */
    private Integer state;
    private String recorder;
    private Date recordTime;
    private PersonGroup personGroup;
    private Stores stores;
    private List<PersonGroupinfo> personGroupInfoList=new ArrayList<PersonGroupinfo>();
}