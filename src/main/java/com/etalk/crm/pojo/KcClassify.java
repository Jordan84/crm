package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Terwer
 * @Date 2018/11/12 19:17
 * @Version 1.0
 * @Description 套餐分类
 **/
@Getter
@Setter
public class KcClassify {

    private Integer id;
    private String name;
    private Integer pgroupId;
    private Integer level;
    private Integer parentid;
    /**
     * 课程介绍
     */
    private String introduce;
    private Integer state;
    private String recorder;
    private Integer storesId;
    /**
     * 所属机构信息
     */
    private Stores stores;
    private Date recordTime;
    /**
     * 套餐分类  0.正常套餐1.体验课(DEMO)2.包月套餐
     */
    private String typeFlag;

    private List<KcPackage> kcPackagesList = new ArrayList<KcPackage>(0);

    private List<KcClassify> kcClassifyChildren =new ArrayList<KcClassify>(0);

    private List<PersonGroupinfo> personGroupInfoList=new ArrayList<PersonGroupinfo>(0);

    private KcClassify kcClassify;

    private PersonGroup personGroup;

    /**所属根分类id*/
    private int rootId;
    /**分类等级*/
    private String classifyLevel;
}
