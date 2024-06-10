package com.survey.api.surveyactivity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.surveyactivity.dto.ActivityDto;
import com.survey.api.surveyactivity.service.ActivityService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/survey/activity")
public class ActivityController {


	
	@Autowired
	ActivityService activityService;
	
	
//	@ApiOperation( value = "This is used to get list of survey based on filter", response = SurveyDto.class, responseContainer = "List")
//	@GetMapping("/getallactivity/profile")
//	public ResponseEntity<?> getAllActivity(@RequestParam(defaultValue = "0") Integer pageNo, 
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(defaultValue = "") String searchStr, @RequestParam(defaultValue = "updatedDateTime") SurveySortByEnum sortBy, 
//            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
//            Authentication authentication) {
//		
//		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
//		Page<SurveyDto> result = surSvc.getAllActivity(pageNo, pageSize, searchStr, sortBy, sortDirection, userPrincipal);
//    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
//    	
//	} 
	
	
	
	@ApiOperation( value = "This is used to get list of Activity to Be Filled ", response = ActivityDto.class, responseContainer = "List")
	@GetMapping("/getallactivity/profile")
	public ResponseEntity<?> getAllActivityProfileType(Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		
		List<ActivityDto> result = activityService.getAllProfileActivity(userDesignationId,userRegionId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	
	@ApiOperation( value = "This is used to get list of Activity to Be Filled ", response = ActivityDto.class, responseContainer = "List")
	@GetMapping("/getallactivity/open")
	public ResponseEntity<?> getAllActivityOpen(Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		
		List<ActivityDto> result = activityService.getAllActivityOPEN(userDesignationId,userRegionId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	
	
	
	@ApiOperation( value = "This is used to get list of Activity to Be Filled for Entity Reporting ", response = ActivityDto.class, responseContainer = "List")
	@GetMapping("/getallactivity/entity")
	public ResponseEntity<?> getAllActivityEntityType(Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = userPrincipal.getDesignationType().getId();
		Long userRegionId = userPrincipal.getRegionData().getId();
		List<ActivityDto> result = activityService.getAllEntityActivity(userDesignationId,userRegionId);

    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	
	

	
	
}
