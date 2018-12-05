package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

/**
 * @author Terwer
 */
@Getter
@Setter
public class Questions{
    private List<QuestionBanks> models;
}
