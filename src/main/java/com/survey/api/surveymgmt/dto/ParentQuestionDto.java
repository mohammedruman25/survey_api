package com.survey.api.surveymgmt.dto;

import java.io.Serializable;

import com.survey.api.surveymgmt.entity.QuestionContent;
import com.survey.api.surveymgmt.entity.QuestionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ParentQuestionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7050104100417666685L;

	private Long id;	
	
	private QuestionContent content;
	
	private QuestionType questionType;
	


}
