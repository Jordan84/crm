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
public class WeixinMessage {

	private Integer id;

	private String messageName;

	private String messageInfo;

	private String keyword1Name;

	private String keyword2Name;

	private String keyword3Name;

	private String keyword4Name;
}
