package com.survey.api.surveyreport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.surveyreport.dto.FilterDTO;
import com.survey.api.surveyreport.dto.SurveyReportDto;
import com.survey.api.surveyreport.service.WebSurveyReportService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/websurveyreport")
public class WebSurveyReportController {

	@Autowired
	private WebSurveyReportService webReportSvc;
	
	/**
	 * This end point will return surveys based on filter
	 * 
	 * @param filterDTO
	 * @return Response with posts based on filter
	 */
	@ApiOperation(value = "Get surveys by filters", response = SurveyReportDto.class, responseContainer = "Page")
	@PostMapping(value = "/filter")
	public ResponseEntity<?> getFilterProducts(@RequestBody FilterDTO filterDTO, Authentication authentication) {
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
		Page<SurveyReportDto> result = webReportSvc.getFilteredSurveys(filterDTO);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
	}
}
