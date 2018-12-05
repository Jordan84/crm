package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jordan
 */
@Getter
@Setter
public class WeixinUser {
	private Integer id;

	private String openId;

	private Integer studentId;

	private String studentLoginName;
}
