package com.survey.api.surveyanswer.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AnswerContent {
	
	private List<String> choice;
	private String input;
	private List<String> filePaths;
	private List<List<String>> matrixAnswers;

}
