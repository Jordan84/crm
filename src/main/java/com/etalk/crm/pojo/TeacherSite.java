package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Jordan
 */
@Entity
@Getter
@Setter
public class TeacherSite {

	private Integer id;

	private String cnName;

	private String enName;
}
