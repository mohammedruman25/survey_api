package com.survey.api.surveyactivity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.surveyactivity.dto.ActivityDto;
import com.survey.api.surveyactivity.dto.ActivityReportDto;
import com.survey.api.surveyactivity.dto.ActivityReportSurveyAnswerDTO;
import com.survey.api.surveyactivity.service.ActivityMobileReportService;
import com.survey.api.surveyactivity.service.ActivityService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/survey/report/mobile")
public class MobileReportController {

	@Autowired
	ActivityMobileReportService activityMobileReportService;
	
	@ApiOperation( value = "This is used to get list of Completed Activity for Mobile App. IT will return the activity of users only", response = ActivityDto.class, responseContainer = "List")
	@GetMapping("/profile")
	public ResponseEntity<?> getAllActivityReportProfileType(Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		
		List<ActivityReportDto> result = activityMobileReportService.getAllProfileActivityReport(userDesignationId,userRegionId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to get list of Completed Activity for Mobile App. IT will return the activity of users only", response = ActivityDto.class, responseContainer = "List")
	@GetMapping("/entity")
	public ResponseEntity<?> getAllActivityReportEntityType(Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		
		List<ActivityReportDto> result = activityMobileReportService.getAllEntityActivityReport(userDesignationId,userRegionId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This APi will be called from the list when user want to see the details of survey he answered. ", response = ActivityReportSurveyAnswerDTO.class)
	@GetMapping("/getsurveydetailbyid")
	public ResponseEntity<?> getsurveydetailbyid(@RequestBody ActivityReportSurveyAnswerDTO dto, Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		
		ActivityReportSurveyAnswerDTO result = activityMobileReportService.getmobileSurveyReport(userPrincipal.getId(),dto,userDesignationId,userRegionId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	
	
	
}
