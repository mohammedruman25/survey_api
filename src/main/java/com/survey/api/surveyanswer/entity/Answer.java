package com.survey.api.surveyanswer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.survey.api.common.entity.BaseEntity;
import com.survey.api.surveymgmt.entity.Question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SURVEY_ANSWER")
@NoArgsConstructor
@Getter
@Setter
public class Answer extends BaseEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1648963421463021496L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name =  "survey_attempt_id", nullable = false)
	private SurveyAttempt surveyAttempt;
	
	@OneToOne
	private Question question;
	

	
	
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private AnswerContent answerContent;
}
