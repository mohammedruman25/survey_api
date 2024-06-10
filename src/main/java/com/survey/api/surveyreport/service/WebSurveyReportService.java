package com.survey.api.surveyreport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.survey.api.ruleengine.service.SurveyReleaseRuleEngineService;
import com.survey.api.surveymgmt.dto.SurveyDto;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.repo.SurveyRepo;
import com.survey.api.surveyreport.dto.FilterDTO;
import com.survey.api.surveyreport.dto.ReportCountDTO;
import com.survey.api.surveyreport.dto.SurveyReportDto;

@Service
public class WebSurveyReportService {
	
	@Autowired
	private SurveyRepo surveyRepo;
	
		
	@Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	private SurveyReleaseRuleEngineService releaseRuleServ;
	
	@Autowired
	private SurveySpecifications surSpec;
	
	@Value("${surveys.pageSize}")
	private int pageSize;
	
	public Page<SurveyReportDto> getFilteredSurveys(FilterDTO filterDTO){
		Pageable pageRequest = PageRequest.of(filterDTO.getPageNumber(), pageSize,
				surSpec.getSortBy(filterDTO.getSortBy().name(), filterDTO.getSortDirection()));
		
		Specification<Survey> surSpecifications = surSpec.createSurveySpecifications(filterDTO);
		
		Page<Survey> surList = surveyRepo.findAll(surSpecifications, pageRequest);
		Page<SurveyReportDto> dtoList = surList.map(this::getDtoFromEntity);
		
		List<Long> srvIdLst = new ArrayList<>();
		dtoList.forEach(itm -> srvIdLst.add(itm.getId()));
		
		List<ReportCountDTO> surveyCount = releaseRuleServ.getSurveyCount(srvIdLst);
		
		dtoList.forEach(itm -> {
			Long surId = itm.getId();
			Optional<ReportCountDTO> optDto = surveyCount.stream().filter(s -> surId.equals(s.getSurveyId())).findFirst();
			if(optDto.isPresent()) {
				itm.setResponseCount(optDto.get().getResponseCount());
				itm.setReleaseCount(optDto.get().getReleaseCount());
			}
		});
		
		return dtoList;
	}
	
	private SurveyReportDto getDtoFromEntity(Survey survey) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<Survey, SurveyDto>() {
			@Override
			protected void configure() {
				skip(destination.getQuestions());
			}
		});
		
		
		
		SurveyReportDto sur = modelMapper.map(survey, SurveyReportDto.class);
		
		return sur;
	}

}
