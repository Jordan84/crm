package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**评论管理
 * @Auther: James
 * @Date: 2018/7/5 16:36
 * @Description:
 */
@Getter
@Setter
@Entity
public class CaseShareComment implements Serializable {
    private int id;

    private int caseShareId;

    private String addUserName;

    private int addUserId;

    private String comment;

    private Date createTime;
}
