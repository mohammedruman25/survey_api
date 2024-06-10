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
public class ActivityDto  implements Serializable{
	
	
	private Long id;
	private String code; //Region Data Code in case of Survey this will be Null
	private String title; //RegionData Name or Activity Title 
	private String type; //e.g. BLOCK|CLUSTER|SCHOOL|DISTRICT|SURVEY
	List<ActivityDto> children; // when children is Null UI can call the Survey get by ID call 

	//Survey related Fields
	private OffsetDateTime startDateTime;
	private String surveyStatus; //e.g. IN PROGRESS / Completed based on if completly Answered or not 
	private ProgramDtoSmall program;   // if will be not null if SUrvey is associated with any program
	private String description; //For survey only
	private OffsetDateTime expireDateTime; //for surveyOnly

}
