package com.survey.api.surveyreport.dto;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.survey.api.surveymgmt.dto.SurveySortByEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {

	private List<KeyValue> filters;
	private SurveySortByEnum sortBy;
	private Sort.Direction sortDirection;
	private int pageNumber;
	
}
