package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Terwer
 * @date 2018/05/24
 */
@Getter
@Setter
public class KnowledgePointMistakes {
    /**
     * id
     */
    private Integer id;
    /**
     * 中文
     */
    private String cnName;
    /**
     * 父id
     */
    private Integer parentId;
    /**
     * 字错题本列表0
     */
    private List<KnowledgePointMistakes> listChildKnowledgePointMistakes;
}
