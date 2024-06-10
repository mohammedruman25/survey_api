package com.survey.api.surveymgmt.dto;

import java.io.Serializable;
import java.util.List;

import com.survey.api.surveymgmt.entity.QuestionContent;
import com.survey.api.surveymgmt.entity.QuestionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6705137222667803620L;

	private Long id;	
	
	private QuestionContent content;
	
	private QuestionType questionType;
	
	private Boolean isOptional;
	

    private String showCondition;
	
    private List<QuestionDto> childQuestions;

}
