package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author Jordan
 */
@Entity
@Setter
@Getter
public class MenuInfo implements Serializable {
    /**
     * 菜单id
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单url
     */
    private String menuUrl;
    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 权限id
     */
    private Integer jurisdictionId;

    private Integer menuLevel;

    private String classInfo;
}
