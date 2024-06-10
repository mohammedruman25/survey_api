package com.survey.api.surveyreport.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.lookup.dto.RegionDataDTO;
import com.survey.api.surveymgmt.dto.ProgramDtoSmall;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.surveymgmt.entity.SurveyStatus;
import com.survey.api.user.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyReportDto implements Serializable{
	
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -4907238389127406214L;


	private Long id;

	
	private String title;
	

    private String description;
    
    private SurveyStatus status;
	

	private ProgramDtoSmall program;
	@JsonIgnore
	private UserDTO updatedBy;
	private OffsetDateTime updatedDateTime;	
	private OffsetDateTime releaseDateTime;
	private OffsetDateTime expireDateTime;
	
	private Boolean isSurveyLocationType;
	private SurveyTypeEnum surveyType;
	

	private Long releaseCount;
	private Long responseCount;
	

}
