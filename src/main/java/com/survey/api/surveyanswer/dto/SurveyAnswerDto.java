package com.survey.api.surveyanswer.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.Column;

import com.survey.api.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyAnswerDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7180527548072887635L;

	private Long id;	
	
	private Long surveyId;
	
	User user;
	
	private OffsetDateTime attemptDateTime;
	
	private List<AnswerDto> answers;
	

	private String longitude;
	private String latitude;
	
	private Long targetEntityDataId;  // This will be School Ids if Survey is entity type or Null
	

	private Boolean isSurveyInProgress = false;
	
	private Boolean isSurveyAnsweredComplete = false;

}
