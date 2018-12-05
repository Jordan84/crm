package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Jordan
 */
@Entity
@Getter
@Setter
public class SubmitTemplateInfo implements Serializable {

	private String templateId;

	private String msgTitle;

	private String firstData;

	private String keyword1;
	private String keyword2;
	private String keyword3;
	private String keyword4;

	private String remark;

	private String linkUrl;

	private Integer cancelClass;
	private Integer cancelClassReasonId;
	private Integer closeClass;

	private List<Integer> listStudentId;

	private List<Integer> platformList;

	private Integer remindType;

	private String remindStartTime;

	private String remindEndTime;

	private String creator;

	private String classStartTime;

	private String classEndTime;

	private Integer classWayId;

	private Integer state;

	private List<Integer> teaSiteId;

	private String courseStartTime;

	private String courseEndTime;

	/**是否紧急通知*/
	private Integer isUrgentNotice;

	private List<String> excludeTeacher;
}
