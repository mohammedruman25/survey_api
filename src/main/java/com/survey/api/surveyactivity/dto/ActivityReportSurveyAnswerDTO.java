package com.survey.api.surveyactivity.dto;

import java.io.Serializable;

import com.survey.api.surveyanswer.dto.SurveyAnswerDto;
import com.survey.api.surveymgmt.dto.SurveyDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ActivityReportSurveyAnswerDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 869742188099113518L;
	Long surveyId; 
	Long entityId;// to be filled in case of entity survey
	
	SurveyDto survey;
	SurveyAnswerDto answer;

}
