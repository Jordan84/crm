package com.etalk.crm.pojo;

import lombok.Data;
/**
 * @description:
 * @author: liubangwe
 * @date: 2018-11-28 14:58
 */

@Data
public class RecommendUsers {
    private static final long serialVersionUID = -5068255177342711619L;

    /**
     * 学员id
     */
    private Integer uid;

    /**
     * 学员的推荐人id
     */
    private Integer toUid;
}
