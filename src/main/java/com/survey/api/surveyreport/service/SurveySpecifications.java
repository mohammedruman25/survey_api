package com.survey.api.surveyreport.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.surveymgmt.entity.Program;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveyreport.dto.FilterDTO;
import com.survey.api.surveyreport.dto.KeyValue;
import com.survey.api.surveyreport.dto.SurveyFilterKeys;

@Component
public class SurveySpecifications {
	
	public Specification<Survey> createSurveySpecifications(FilterDTO searchCriteria) {
		return addSpecification(searchCriteria);
	}

	private Specification<Survey> addSpecification(FilterDTO searchCriteria) {
		List<KeyValue> keyValueList = searchCriteria.getFilters();
		Map<SurveyFilterKeys, String> keyValueMap = new HashMap<>();
		for (KeyValue keyValue : keyValueList) {
			keyValueMap.put(keyValue.getKey(), keyValue.getValue());
		}
		Specification<Survey> filterSpec = createDefaultSpec();
		
		if (keyValueMap.containsKey(SurveyFilterKeys.SURVEYTITLE)) {
			String value = keyValueMap.get(SurveyFilterKeys.SURVEYTITLE);
			filterSpec = filterSpec.and(surveyTitleIn(value));
		}
		
		if (keyValueMap.containsKey(SurveyFilterKeys.PROGRAMTITLE)) {
			String value = keyValueMap.get(SurveyFilterKeys.PROGRAMTITLE);
			filterSpec = filterSpec.and(programTitleIn(value));
		}
		
		if (keyValueMap.containsKey(SurveyFilterKeys.SURVEYTYPE)) {
			String value = keyValueMap.get(SurveyFilterKeys.SURVEYTYPE);
			filterSpec = filterSpec.and(surveyTypeIn(value));
		}
		
		return filterSpec;
	}
	
	private Specification<Survey> surveyTitleIn(String value) {
		return (root, query, builder) -> {
			return builder.like( builder.upper(root.get("title")), "%" + value.toUpperCase() + "%");
		};
	}
	
	private Specification<Survey> surveyTypeIn(String value) {
		SurveyTypeEnum sType = SurveyTypeEnum.valueOf(value);
		return (root, query, builder) -> {
			return root.get("surveyType").in(sType);
		};
	}
	
	private Specification<Survey> programTitleIn(String value) {
		return (root, query, builder) -> {
			Join<Program, Survey> surveyProgram = root.join("program");
			return builder.like(builder.upper(surveyProgram.get("title")) , "%" + value.toUpperCase() + "%");
		};
	}

	private Specification<Survey> createDefaultSpec() {
		return (root, query, builder) -> {
			return root.get("id").isNotNull();
		};
	}

	public Sort getSortBy(String sortBy, Sort.Direction direction) {
		return Sort.by(direction, sortBy);
	}

}
