package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jordan
 */
@Entity
@Setter
@Getter
public class MessageTemplate implements Serializable {
	private Integer id;

	private String templateId;

	private String msgTitle;

	private String firstData;

	private String keyword1;

	private String keyword2;

	private String keyword3;

	private String keyword4;

	private String key1;

	private String key2;

	private String key3;

	private String key4;

	private String remark;

	private String linkUrl;

	private String keywordNames;

	private String creator;

	private Date createTime;
}
