package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author Terwer
 * @Date 2018/11/6 16:11
 * @Version 1.0
 * @Description 移动端题目
 **/
@Getter
@Setter
public class MobileQuestion implements Serializable {
    /**
     * 用户ID
     */
    private Integer personId;
    /**
     * 题目ID
     */
    private Integer qrId;
    /**
     * 题目类型
     */
    private Integer qrtype;

    /**
     * 题目
     */
    private QuestionBanks questionBanks;

    /**
     * 重写equals用于比较题目
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MobileQuestion) {
            MobileQuestion mq = (MobileQuestion) obj;
            return this.getQrId().equals(mq.getQrId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qrId);
    }
}
