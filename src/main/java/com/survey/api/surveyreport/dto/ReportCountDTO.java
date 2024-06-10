package com.survey.api.surveyreport.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Data
@ToString
public class ReportCountDTO implements Serializable{
	
	private Long surveyId;
	private Long releaseCount;
	private Long responseCount;
	private Boolean isEntityType;

}
