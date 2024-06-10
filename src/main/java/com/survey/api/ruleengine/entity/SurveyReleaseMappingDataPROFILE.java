package com.survey.api.ruleengine.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SURVEY_RELEASE_MAPPING_DATA_PROFILE", indexes = {@Index(name = "survey_rel_map_profile_index1",  columnList="survey_id", unique = false),
        @Index(name = "survey_rel_map_profile_index2", columnList="target_designation_id",     unique = false),
        @Index(name = "survey_rel_map_profile_index3", columnList="target_region_data_id",     unique = false)}, 
		uniqueConstraints=@UniqueConstraint(columnNames={"survey_id", "target_designation_id","target_region_data_id"}))
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SurveyReleaseMappingDataPROFILE implements Serializable {
	


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "survey_id")
	private Long surveyId;
	
	@Column(name = "target_designation_id")
	private Long targetDesignationId;

	@Column(name = "target_region_data_id")
	private Long targetRegionDataId;
	
	
	@Column(name = "is_survey_in_progress" , columnDefinition = "boolean default false")
	private Boolean isSurveyInProgress = false;
	
	@Column(name = "is_survey_answered" , columnDefinition = "boolean default false")
	private Boolean isSurveyAnswered = false;
	
	@Column(name = "is_survey_expired" , columnDefinition = "boolean default false")
	private Boolean isSurveyExpired = false;
	

}
