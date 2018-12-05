package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Terwer
 * @date 2018/06/04
 */
@Getter
@Setter
public class PaperInputAnswer {
    /**
     * 分数ID
     */
    private Integer scoreId;
    /**
     * 总分数
     */
    private Integer score;
    /**
     * 大题包含的小题
     */
    private List<QuestionInputAnswer> listQuestionInputAnswer;
}
