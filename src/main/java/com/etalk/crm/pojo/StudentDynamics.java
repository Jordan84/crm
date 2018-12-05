package com.etalk.crm.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * 学员动态
 */
@Entity
@Getter
@Setter
public class StudentDynamics implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**主键id*/
	private int id;
	/**动态内容**/
	private String content;
	/**关联学生登录名**/
	private String studentLogin;
	/**添加时间*/
	@JSONField(format = "yyyy-MM-dd hh:mm")
	private Date addTime;
	/**添加人*/
	private String addUser;
	/**添加人id*/
	private int addUserId;
	/**学生id*/
	private int personId;

	/**
	 *
	 */
	private Integer state;
	/**
	 *
	 */
	private String name;
	/**
	 *
	 */
	private Date remindTime;


	private Integer questionnaireId;

}
