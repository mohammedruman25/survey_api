package com.survey.api.surveymgmt.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionContent {
	private String statement;
	private String hint;
	private Validation validation;
	private MatrixQuestionContent matrixQuestionContent;
	
	private List<String> options;
	
	@Getter
	@Setter
	@NoArgsConstructor
	private class Validation{
		String min;
		String max;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	private class MatrixQuestionContent{
		int noOfRow;
		int noOfColumn;
		List<String> rowHeaders;
		List<String> columnHeaders;
	}

}
