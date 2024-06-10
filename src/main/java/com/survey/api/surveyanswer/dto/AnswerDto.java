package com.survey.api.surveyanswer.dto;

import java.io.Serializable;

import com.survey.api.surveyanswer.entity.AnswerContent;
import com.survey.api.surveymgmt.dto.QuestionDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AnswerDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6267355020541548903L;


	private Long id;
	
	
	private QuestionDto question;
	

	
	
	private AnswerContent answerContent;

}
