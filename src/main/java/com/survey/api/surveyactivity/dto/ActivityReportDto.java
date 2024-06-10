package com.survey.api.surveyactivity.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import com.survey.api.surveymgmt.dto.ProgramDtoSmall;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Getter
@Setter
public class ActivityReportDto  implements Serializable{
	
	private Long id;
	private String code; //Region Data Code in case of Survey this will be Null
	private String title; //RegionData Name or Activity Title 
	private String type; //e.g. BLOCK|CLUSTER|SCHOOL|DISTRICT|SURVEY
	List<ActivityReportDto> children; // when children is Null UI can call the Survey get by ID call 
	private ProgramDtoSmall program;   // if will be not null if SUrvey is associated with any program
	private String description; //For survey only
	private OffsetDateTime expireDateTime; //for surveyOnly
	
	private Boolean isSurveyAnswered;
	private Boolean isSurveyExpired;


}
