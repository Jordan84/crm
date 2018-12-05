package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Terwer
 * @date 2018/06/12
 */
@Getter
@Setter
public class City {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String cnName;
    /**
     * 英文名称
     */
    private String enName;
    /**
     * 区域编码
     */
    private Integer areaCode;
    /**
     * 父级ID
     */
    private Integer parentId;
    /**
     * 排序
     */
    private Integer sort;
}
