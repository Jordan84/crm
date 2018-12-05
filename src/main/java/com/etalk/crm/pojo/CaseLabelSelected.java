package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**存取caseshare   选择的标签
 * @Auther: James
 * @Date: 2018/8/2 17:17
 * @Description:
 */
@Getter
@Setter
@Entity
public class CaseLabelSelected  implements Serializable {
    private int id ;

    /**
     * caseshareId
     */
    private int caseShareId;

    /**
     * 标签Id
     */
    private int labelId;
}
