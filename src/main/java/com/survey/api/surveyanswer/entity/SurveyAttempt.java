package com.survey.api.surveyanswer.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.survey.api.surveymgmt.entity.Question;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.user.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SURVEY_ATTEMPT")
@NoArgsConstructor
@Getter
@Setter
public class SurveyAttempt implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3380842060808507239L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	@Column(name = "survey_attempt_id", nullable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name =  "survey_id", nullable = false)
	private Survey survey;
	
	@OneToOne
	@JoinColumn(name =  "user_id", nullable = false)
	User user;
	
	@Column(name = "attempt_date_time", nullable = false, updatable = false)
	private OffsetDateTime attemptDateTime;
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "surveyAttempt")
	private List<Answer> answers;
	

	@Column(nullable = true)
	private String longitude;
	@Column(nullable = true)
	private String latitude;
	

	@Column(name = "target_entity_data_id",nullable = true)
	private Long targetEntityDataId;  // This will be School Ids if Survey is entity type or Null
	
	

	@Column(name = "is_survey_in_progress" , columnDefinition = "boolean default false")
	private Boolean isSurveyInProgress = false;
	
	@Column(name = "is_survey_answered_completely" , columnDefinition = "boolean default false")
	private Boolean isSurveyAnsweredComplete = false;

}
