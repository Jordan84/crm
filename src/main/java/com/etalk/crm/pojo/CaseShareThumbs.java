package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**点赞功能
 * @Auther: James
 * @Date: 2018/7/5 10:06
 * @Description:
 */
@Setter
@Getter
@Entity
public class CaseShareThumbs implements Serializable {
    private int id;

    private int addUserId;

    private String addUserName;

    private int caseShareId;

}
