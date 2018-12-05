package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * case  不通过理由
 */
@Entity
@Getter
@Setter
public class CaseRejectReason implements Serializable {
    private int id;

    /**
     * caseManager id
     */
    private int caseId;

    /**
     * 不通过理由
     */
    private String rejectReason;

    /**
     * 添加时间
     */
    private Date add_time;
}
