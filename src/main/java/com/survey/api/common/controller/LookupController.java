package com.survey.api.common.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.lookup.dto.DesignationTypeDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/lookup/text")
public class LookupController {

	
	@GetMapping(value = "/surveyTypeCheckbox")
	@ApiOperation(value = "this checkBox will set the value while creating a survey. Set variable isEntityType = true if selected.")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<String> getDesignation(Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
		return new ResponseEntity<String>("Is Survey Entity Type." , HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/entityActivity")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<String> entityActivity(Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

	
		return new ResponseEntity<String>("School Activity." , HttpStatus.OK);
	}
	
	@GetMapping(value = "/profileActivity")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<String> profileActivity(Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

	
		return new ResponseEntity<String>("Profile Activity" , HttpStatus.OK);
	}
}
