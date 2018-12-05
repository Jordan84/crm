package com.etalk.crm.pojo;

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
public class MessageSendRecord {

	private Integer id;

	private String templateId;

	private String msgTitle;

	private String firstData;

	private String keyword1;

	private String keyword2;

	private String keyword3;

	private String keyword4;

	private String remark;

	private String linkUrl;

	private Integer state;

	private Integer receiveType;

	private Integer remindType;

	private Integer cancelClass;

	private Integer closeClass;

	private String showStartTime;

	private String showEndTime;

	private Date remindEndTime;

	private Date closeStartTime;

	private Date closeEndTime;

	private String platformName;

	private String messageInfo;

	private String keywords;

	private String creator;

	private Date createTime;
	/**是否紧急通知*/
	private Integer isUrgentNotice;
}
