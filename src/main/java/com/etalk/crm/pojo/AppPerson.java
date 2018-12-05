package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Jordan
 */
@Entity
@Setter
@Getter
public class AppPerson {

	private Integer id;

	private String deviceId;
}
