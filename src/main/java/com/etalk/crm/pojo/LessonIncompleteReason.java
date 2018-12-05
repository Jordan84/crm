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
public class LessonIncompleteReason {

	private Integer lessonId;

	private Integer reasonId;

	private Integer state;

	private String creator;

}
