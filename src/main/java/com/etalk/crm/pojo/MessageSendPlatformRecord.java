package com.etalk.crm.pojo;

/**
 * @author Jordan
 */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author Jordan
 */
@Entity
@Setter
@Getter
public class MessageSendPlatformRecord {

	private Integer msgSendId;

	private Integer receivingPlatformId;

}
