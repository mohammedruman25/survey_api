package com.survey.api.ruleengine.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.showcase.service.ShowCaseService;
import com.survey.api.surveyactivity.dto.ActivityDto;
import com.survey.api.surveyactivity.service.ActivityService;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.surveymgmt.service.SurveyService;
import com.survey.api.surveyreport.dto.ReportCountDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/test/ruleengine")
public class TestController {

	@Autowired
	SurveyReleaseRuleEngineService surveyReleaseService;

	@Autowired
	SurveyService surServ;
	
	@Autowired
	ShowCaseService shServ;
	
//	@Autowired
//	SurveyReportingRuleEngineService surveyReportService;
	
	@Autowired
	ShowCaseVisibilityRuleEngine showcaseruleEngine;
	
	
	@Autowired
	ActivityService activityService;
	
	
	@PostMapping("/releaseSurveyPROFILE/{surveyId}")
	@ApiOperation(value = "TestRelease", response = String.class)

	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered"),
	        @ApiResponse(code = 400, message = "Bad Request"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> releaseSurveyPROFILE(@PathVariable(name = "surveyId")Long surveyId, @Valid @RequestBody List<SurveyReleaseData> releaseDtos,Authentication authentication) {
		
		surveyReleaseService.generateSurvetReleasemappingforActivityList(surveyId, releaseDtos, false);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Release Done!"));
	}
	
	@PostMapping("/releaseSurveyENTITY/{surveyId}")
	@ApiOperation(value = "TestRelease", response = String.class)

	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered"),
	        @ApiResponse(code = 400, message = "Bad Request"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> releaseSurveyENTITY(@PathVariable(name = "surveyId")Long surveyId, @Valid @RequestBody List<SurveyReleaseData> releaseDtos,Authentication authentication) {
		
		surveyReleaseService.generateSurvetReleasemappingforActivityList(surveyId, releaseDtos, true);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Release Done!"));
	}
	
	
	
	@PostMapping("/getProfileActivity/{designationId}/{regionId}")
	@ApiOperation(value = "TestRelease", response = String.class)

	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered"),
	        @ApiResponse(code = 400, message = "Bad Request"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public List<ActivityDto> getActivity(@PathVariable(name = "designationId")Long designationId,@PathVariable(name = "regionId")Long regionId,Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

		Long userDesignationId = auth.getDesignationType().getId();
		Long userRegionId = auth.getRegionData().getId();
		
		return activityService.getAllProfileActivity(userDesignationId,userRegionId);
	}
	
	@PostMapping("/getEntityActivity/{designationId}/{regionId}")
	@ApiOperation(value = "TestRelease", response = String.class)

	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered"),
	        @ApiResponse(code = 400, message = "Bad Request"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public List<ActivityDto> getEntityActivity(@PathVariable(name = "designationId")Long designationId,@PathVariable(name = "regionId")Long regionId,Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = auth.getDesignationType().getId();
		Long userRegionId = auth.getRegionData().getId();
		return activityService.getAllEntityActivity(designationId, regionId);
	}
	
	@PostMapping("/setSurevyExpired")
	public String setSurveyExpired() {
		
		surServ.setSurveyExpired();
		
		return "Success";
	}
	
	@PostMapping("/setShowCaseExpired")
	public String setShowCaseExpired() {
		
		shServ.setShowCaseExpired();
		
		return "Success";
	}
	
	
	@PostMapping("/create/showcase/{showcaseid}/{regiondataId}")
	public void createShowCaseExpired(@PathVariable(name = "showcaseid")Long showcaseid,@PathVariable(name = "regiondataId")Long regiondataId,Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
		Long userDesignationId = auth.getDesignationType().getId();
		Long userRegionId = auth.getRegionData().getId();
		showcaseruleEngine.createShowcaseMapping(showcaseid, regiondataId);
	}
	
	
	
	
	@PostMapping("/getSUrveyMetrix")
	public  List<ReportCountDTO> getSUrveyMetrix() {
		ArrayList<Long> surveyIds = new ArrayList<Long>();
		surveyIds.add(43L);
		surveyIds.add(67L);
		surveyIds.add(66L);

		return surveyReleaseService.getSurveyCount(surveyIds);
		
	}
	
	
	
}
