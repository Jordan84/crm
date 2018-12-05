package com.etalk.crm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * @author Terwer
 */
@Entity
@Getter
@Setter
public class TestPaper {
    /**
     * 试卷ID
     */
    private Integer id;
    /**
     * 试卷标题
     */
    private String cnName;
    /**
     * 试卷类型ID
     */
    @JsonIgnore
    private Integer tpaperTypeId;
    /**
     * 试卷类型名称
     */
    private String typeCnName;
    /**
     * 难度系数ID
     */
    @JsonIgnore
    private Integer qddegreeId;
    /**
     * 难度系数名称
     */
    private String degreeCnName;
    /**
     * 年级ID
     */
    @JsonIgnore
    private Integer gradeId;
    /**
     * 年级名称
     */
    private String gradeCnName;
    /**
     *标记本用户本试卷是否可以再测试一次：0未做，1待批改，2完成
     */
    private Integer state;
    /**
     * 对应题目（包含大题标题以及小题）
     */
    private List<QuestionItem> listQuestionItems;

    /**
     * 操作时间
     */
    private Date recordTime;

}
