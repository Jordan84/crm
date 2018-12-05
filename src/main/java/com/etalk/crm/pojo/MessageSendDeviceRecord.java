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
public class MessageSendDeviceRecord {

	private Integer msgSendId;

	private Integer receivingPlatformId;

	private String deviceId;

	private Integer personId;
}
