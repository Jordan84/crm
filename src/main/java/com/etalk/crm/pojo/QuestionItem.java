package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

/**
 * 大题对应模型
 *
 * @author Terwer
 * @date 2018/05/21
 */
@Entity
@Getter
@Setter
public class QuestionItem {
    /**
     * 大题标题
     */
    private String questionTitle;
    /**
     * 大题包含的小题
     */
    private List<QuestionBanks> listQuestionBanks;
}
