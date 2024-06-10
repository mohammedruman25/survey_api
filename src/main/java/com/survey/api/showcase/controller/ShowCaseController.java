package com.survey.api.showcase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.showcase.service.ShowCaseService;
import com.survey.api.surveymgmt.dto.SurveySortByEnum;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/showCase")
public class ShowCaseController {

	@Autowired
	ShowCaseService service;
	
	@ApiOperation( value = "This is used to add new showcase", response = ShowCaseDto.class)
	@PostMapping("/add")
	public ResponseEntity<?> saveNewShowCase(@RequestBody ShowCaseDto dto, Authentication authentication) {
		

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ShowCaseDto result = service.addNewShowCase(dto, userPrincipal.getId(), userPrincipal.getRegionData().getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to update showcase", response = ShowCaseDto.class)
	@PostMapping("/{showCaseId}/update")
	public ResponseEntity<?> uppdateShowcase(@PathVariable("showCaseId") Long showCaseId,@RequestBody ShowCaseDto dto, Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ShowCaseDto result = service.updateShowCase(showCaseId, dto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to delete specified showcase", response = ShowCaseDto.class)
	@DeleteMapping("/{showCaseId}/delete")
	public ResponseEntity<?> deleteShowcase(@PathVariable("showCaseId") Long showCaseId,@RequestBody ShowCaseDto dto, Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		String result = service.deleteShowCase(showCaseId, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	
	@ApiOperation( value = "This is used to get list of showcase created by logged in user. Can be used to edit the showcase before Upvote. "
			+ "After upvote SHowcase will not be listed here.", response = ShowCaseDto.class, responseContainer = "List")
	@GetMapping("/get/createdbyloggedinuser")
	public ResponseEntity<?> getOwnShowCase(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdDateTime") String sortBy, 
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Page<ShowCaseDto> result = service.getOwnShowCase(pageNo, pageSize, userPrincipal.getId(), sortBy, sortDirection);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result));
    } 
	
	@ApiOperation( value = "This is used to get list of showcase based on filter. TO be used for View SHowcase list.", response = ShowCaseDto.class, responseContainer = "List")
	@GetMapping("/get/all")
	public ResponseEntity<?> getShowCase(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchStr, @RequestParam(defaultValue = "createdDateTime") String sortBy, 
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Page<ShowCaseDto> result = service.getAllShowCase(pageNo, pageSize, searchStr, sortBy, sortDirection, userPrincipal.getRegionData().getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 	

	
	@ApiOperation( value = "This is used to get list of showcase which are waiting for Approval / Upvote", response = ShowCaseDto.class, responseContainer = "List")
	@GetMapping("/get/waitingforupvote")
	public ResponseEntity<?> getShowCasewatingforUpvote(
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		List<ShowCaseDto> result = service.getAllShowCaseWaitingForUpVote(userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to add new survey", response = ShowCaseDto.class)
	@PostMapping("/{showCaseId}/upvote")
	public ResponseEntity<?> upvoteShowcase(@PathVariable("showCaseId") Long showCaseId, Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ShowCaseDto result = service.upVoteShowCase(showCaseId, userPrincipal.getId(), userPrincipal.getRegionData().getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	
	
	
	/**
	 * This API is used add files to a ShowCase
	 * 
	 * @param showCaseId
	 * @return ResponseEntity<showCaseId>
	 */
	@ApiOperation(value = "This API is used add files to a showcase", response = ShowCaseDto.class)
	@PostMapping(value = "/{showCaseId}/addfile")
	public ResponseEntity<?> addFiles(@PathVariable("showCaseId") Long showCaseId,
			@RequestPart(name = "files", required = false) List<MultipartFile> files, Authentication authentication)
			throws Exception {
		List<SurveyAppFileDto> fileDtLst = new ArrayList<>();
		for(MultipartFile f : files) {
			SurveyAppFileDto dto = new SurveyAppFileDto();
			dto.setFileData(f.getBytes());
			dto.setOriginalFileName(f.getOriginalFilename());
			fileDtLst.add(dto);
		}
		
		ShowCaseDto result = service.addShhowCaseFiles(showCaseId, fileDtLst, ((UserAuthDetails) authentication.getPrincipal()).getId());
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
	}
	
	@ApiOperation(value = "This API is used delete files from a showcase", response = ShowCaseDto.class)
	@DeleteMapping(value = "/{showCaseId}/deletefile/{fileUUID}")
	public ResponseEntity<?> deleteFile(@PathVariable("showCaseId") Long showCaseId, @PathVariable("fileUUID") String fileUUID,Authentication authentication)
			throws Exception {
		
		
		ShowCaseDto result = service.deleteShowCaseFile(showCaseId, fileUUID, ((UserAuthDetails) authentication.getPrincipal()).getId());
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
	}
}
