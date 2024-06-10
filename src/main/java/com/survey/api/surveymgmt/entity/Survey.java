package com.survey.api.surveymgmt.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.survey.api.common.entity.BaseEntity;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.user.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SURVEY")
@NoArgsConstructor
@Getter
@Setter
public class Survey extends BaseEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3551002620370518444L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	@Column(name = "survey_id", nullable = false)
	private Long id;

	
	private String title;
	

	@Column(columnDefinition="TEXT")
    private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "survey", orphanRemoval = true)
	private List<Question> questions;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private SurveyStatus status;
	
	@OneToOne
	User updatedBy;
	
	@ManyToOne
	@JoinColumn(name =  "program_id", nullable = true)
	private Program program;
	
	@Column(name = "updated_date_time")
	private OffsetDateTime updatedDateTime;
	

	
	@Column(name = "release_date_time")
	private OffsetDateTime releaseDateTime;
	

	@Column(name = "start_date_time")
	private OffsetDateTime startDateTime;
	
	@Column(name = "expire_date_time")
	private OffsetDateTime expireDateTime;
	
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private List<SurveyReleaseData> surveyReleaseData;
	

	@Column(nullable = true)
	private Boolean isSurveyLocationType;	
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private SurveyTypeEnum surveyType;
	
}

