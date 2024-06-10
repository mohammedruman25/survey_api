package com.survey.api.surveymgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.LoginRequest;
import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.surveymgmt.dto.SurveyDto;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.surveymgmt.dto.SurveySortByEnum;
import com.survey.api.surveymgmt.entity.QuestionType;
import com.survey.api.surveymgmt.service.SurveyService;
import com.survey.api.surveyreport.dto.FilterDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/survey")
public class SurveyMgmtController {
	
	@Autowired
	SurveyService surSvc;
	
	@GetMapping("health")
	public ResponseEntity<?> healthCheck(){
		return new ResponseEntity<String>("Survey Managment Controller ok ...", HttpStatus.OK);
	}
	
	@ApiOperation( value = "This is used to add new survey", response = SurveyDto.class)
	@PostMapping("/add")
	public ResponseEntity<?> saveNewSurvey(@RequestBody SurveyDto surDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.saveNewSurvey(surDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value =  "This is used to update survey, only in DRAFT state", response = SurveyDto.class)
	@PostMapping("/{surveyId}/update")
	public ResponseEntity<?> updateSurvey(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyDto surDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.updateSurvey(surDto, surveyId, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value =  "This is used to update survey Expiry date", response = SurveyDto.class)
	@PostMapping("/{surveyId}/updateExpiry")
	public ResponseEntity<?> updateSurveyExpiry(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyDto surDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.updateSurveyExpiry(surDto, surveyId, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to release survey", response = SurveyDto.class)
	@PostMapping("/{surveyId}/release")
	public ResponseEntity<?> releaseSurvey(@PathVariable("surveyId") Long surveyId, @RequestBody List<SurveyReleaseData> surDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.releaseSurvey(surveyId,surDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to delete survey", response = String.class)
	@DeleteMapping("/{surveyId}/delete")
	public ResponseEntity<?> deleteSurvey(@PathVariable("surveyId") Long surveyId, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		String result = surSvc.deleteSurvey(surveyId, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to delete survey", response = String.class)
	@PostMapping("/{surveyId}/copy")
	public ResponseEntity<?> copySurvey(@PathVariable("surveyId") Long surveyId, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.copySurvey(surveyId, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to get list of survey based on filter", response = SurveyDto.class, responseContainer = "List")
	@GetMapping("/get")
	public ResponseEntity<?> getSurvey(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchStr, @RequestParam(defaultValue = "updatedDateTime") SurveySortByEnum sortBy, 
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Page<SurveyDto> result = surSvc.getAllSurvey(pageNo, pageSize, searchStr, sortBy, sortDirection);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	
	@ApiOperation( value = "This is used to get one survey", response = SurveyDto.class)
	@GetMapping("/get/{surveyId}")
	public ResponseEntity<?> getSurvey(@PathVariable("surveyId") Long surveyId,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyDto result = surSvc.getOneSurvey(surveyId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	
	
	
	
	
	
	@ApiOperation( value = "This is used to get list questiontype", response = QuestionType.class, responseContainer = "List")
	@GetMapping("/getquestiontype")
	public ResponseEntity<?> getQuestionType() {
		
		List<QuestionType> result = surSvc.getAllQuestionType();
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 

}
