package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestPaperQBankRelation {
    /**
     * 试卷ID
     */
    private Integer testPaperId;
    /**
     * 题目ID
     */
    private Integer qbankId;
    /**
     * 题目父ID
     */
    private Integer qbankParentid;
}
