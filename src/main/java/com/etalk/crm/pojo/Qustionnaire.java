package com.etalk.crm.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Qustionnaire {
    private Integer paperId;

    private String paperTitle;

    private String paperSummary;

    private QuestionnaireOc qoc;

    private JSONObject questionParam;

    private List<QuestionAndOption> questionAndOptionsList;


}
