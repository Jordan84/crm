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
public class Lessons implements Serializable {
	private Integer id;

	private Integer personId;

	private String studentLogin;

	private String teacherLogin;

	private Integer classWayId;

	private Integer state;

	private List<Integer> teaSiteId;

	private String classStartTime;

	private String classEndTime;

	private List<String> excludeTeacher;

	private Integer msgSendId;

	private Integer cancelClass;

	private String courseStartTime;

	private String courseEndTime;

}
