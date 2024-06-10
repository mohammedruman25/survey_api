package com.survey.api.config.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.service.ConfigRegionDataService;
import com.survey.api.config.service.ConfigUserDataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/config/data")
@Api(value=" Master Config data", description="Data for Region and User import. APi can be used by Support Team.")
public class ConfigDataController {
	
	@Autowired
	ConfigRegionDataService configDataService;
	
	@Autowired
	ConfigUserDataService configUserDataService;
	
	@PostMapping(value = "/upload/region_business_data")
	@ApiOperation(value = "Upload Region Type with their Herarchy. Business Entity considered lowest level Region", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<Boolean> uploadregion(@RequestParam("file") MultipartFile file,
			Authentication authentication) {
		
		Boolean result =configDataService.uploadRegionData(file);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);

	}

	@PostMapping(value = "/upload/userData")
	@ApiOperation(value = "Upload UserData with their Regions", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<Boolean> uploadUserData(@RequestParam("file") MultipartFile file,
			Authentication authentication) {
		
		Boolean result =configUserDataService.uploadUserData(file);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);

	}

	
//	@PostMapping(value = "/upload/businessEntity")
//	@ApiOperation(value = "Upload Region Type with their Herarchy", response = RegionTypeMaster.class)
//	@ApiResponses(value = {
//	        @ApiResponse(code = 200, message = "Successfully Completed"),
//	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
//	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
//	})
//	public ResponseEntity<List<BusinessObject>> uploadBusinessEntity(@RequestParam("file") MultipartFile file,
//			Authentication authentication) {
//		
//		List<BusinessObject> results =new ArrayList<>();
//		
//		return new ResponseEntity<List<BusinessObject>>(results, HttpStatus.OK);
//	}

}
