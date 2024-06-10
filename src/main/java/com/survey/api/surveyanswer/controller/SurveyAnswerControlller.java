package com.survey.api.surveyanswer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.surveyanswer.dto.SurveyAnswerDto;
import com.survey.api.surveyanswer.service.SurveyAnswerService;
import com.survey.api.surveymgmt.dto.SurveyDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/survey/{surveyId}/answer")
public class SurveyAnswerControlller {
	
	@Autowired
	SurveyAnswerService surAnsService;
	
	@ApiOperation( value = "This is used to save new survey answer", response = SurveyAnswerDto.class)
	@PostMapping("/add")
	public ResponseEntity<?> addAnswer(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyAnswerDto surAnsDto, Authentication authentication) {

		UserAuthDetails user = (UserAuthDetails) authentication.getPrincipal();
		
		SurveyAnswerDto result = surAnsService.addNewSurveyAnswer(surAnsDto, surveyId, user.getId(), user.getDesignationType().getId(), user.getRegionData().getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to upload files for existing answer", response = String.class)
	@PostMapping("/{answerId}/uploadFiles")
	public ResponseEntity<?> uploadAnswerFile(@PathVariable("surveyId") Long surveyId,@PathVariable("answerId") Long answerId,
			@RequestParam List<MultipartFile> files,
			Authentication authentication) throws IOException {
		
		List<byte[]> bytLstFiles = new ArrayList<byte[]>();
		List<String> fileNames = new ArrayList<String>();
		for(MultipartFile f : files) {
			bytLstFiles.add(f.getBytes());
			fileNames.add(f.getOriginalFilename());
		}
		
		String result = surAnsService.uploadAnswerFiles(bytLstFiles, fileNames, answerId);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
	}
	
	@ApiOperation( value = "This is used to delete files for existing answer", response = String.class)
	@DeleteMapping("/{answerId}/deleteFiles")
	public ResponseEntity<?> deleteAnswerFile(@PathVariable("surveyId") Long surveyId,@PathVariable("answerId") Long answerId,
			Authentication authentication) throws IOException {
				
		String result = surAnsService.deleteAnswerFiles(answerId);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
	}
	
	
	@ApiOperation( value = "This is used to get survey answer", response = SurveyAnswerDto.class, responseContainer = "List")
	@GetMapping("/getAll")
	public ResponseEntity<?> getallAnswer(@PathVariable("surveyId") Long surveyId, Authentication authentication) {

		UserAuthDetails user = (UserAuthDetails) authentication.getPrincipal();
		List<SurveyAnswerDto> result = surAnsService.getSurveyAnswers(surveyId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to get one answer", response = SurveyDto.class)
	@GetMapping("/getOne")
	public ResponseEntity<?> getOneSurveyAnswer(@PathVariable("surveyId") Long surveyId, @RequestParam(name = "userId", required = true) Long userId, 
			@RequestParam(name = "entityId", required = false) Long enityId,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		SurveyAnswerDto result = surAnsService.getSurveyAnswer(surveyId, userId, enityId);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "Get Answer File")
	@GetMapping("/getAnswerFile/{UUID}")
	public void getAnswerFile(@PathVariable("UUID") String uuid, Authentication authentication, HttpServletResponse response) throws IOException {

		UserAuthDetails user = (UserAuthDetails) authentication.getPrincipal();
		SurveyAppFileDto result = surAnsService.getFile(uuid);
    	
		response.setHeader("Content-Disposition", "attachment; filename=" + result.getOriginalFileName());
		response.getOutputStream().write(result.getFileData());
    	
	}

}
