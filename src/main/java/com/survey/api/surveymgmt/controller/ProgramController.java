package com.survey.api.surveymgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.Response;
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.surveymgmt.dto.ProgramDtoSmall;
import com.survey.api.surveymgmt.service.ProgramService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/program")
public class ProgramController {
	
	@Autowired
	ProgramService serv;
	
	
	@ApiOperation( value = "This is used to add new program", response = ProgramDtoSmall.class)
	@PostMapping("/add")
	public ResponseEntity<?> saveNewProgram(@RequestBody ProgramDtoSmall pgmDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ProgramDtoSmall result = serv.addNewProgram(pgmDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value =  "This is used to update program", response = ProgramDtoSmall.class)
	@PostMapping("/{programId}/update")
	public ResponseEntity<?> updateSurvey(@PathVariable("programId") Long pgmId, @RequestBody ProgramDtoSmall pgmDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ProgramDtoSmall result = serv.updateProgram(pgmId, pgmDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to get list of program based on filter", response = ShowCaseDto.class, responseContainer = "List")
	@GetMapping("/get")
	public ResponseEntity<?> getShowCase(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchStr, @RequestParam(defaultValue = "updatedDateTime") String sortBy, 
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Page<ProgramDtoSmall> result = serv.getAllProgram(pageNo, pageSize, searchStr, sortBy, sortDirection);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	

}
