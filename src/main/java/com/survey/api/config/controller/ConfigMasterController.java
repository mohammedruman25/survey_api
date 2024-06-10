package com.survey.api.config.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.service.ConfigMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/config/masterdata")
@Api(value=" Master Config data", description="Mastre Data / Structure for Region and Designation. API can be used by Support.")
public class ConfigMasterController {
	
	@Autowired
	ConfigMasterService configMasterService;
	
	@PostMapping(value = "/upload/regiontype")
	@ApiOperation(value = "Upload Region Type with their Herarchy", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<RegionTypeMaster>> uploadRegionType(@RequestParam("file") MultipartFile file,
			Authentication authentication) {
		
		List<RegionTypeMaster> results =configMasterService.uploadRegionTypeData(file);
		
		return new ResponseEntity<List<RegionTypeMaster>>(results, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/regiontype")
	@ApiOperation(value = "Get Region Type with their Herarchy ordered by ID ", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<RegionTypeMaster>> getRegionType(Authentication authentication) {
		
		List<RegionTypeMaster> results =configMasterService.getRegionTypeEntity();
		
		return new ResponseEntity<List<RegionTypeMaster>>(results, HttpStatus.OK);
	}
	
	
	
	@PostMapping(value = "/upload/designationtype")
	@ApiOperation(value = "Upload User Designation Type with their Herarchy", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<DesignationTypeMaster>> uploadDesignationdata(@RequestParam("file") MultipartFile file,
			Authentication authentication) {
		
		List<DesignationTypeMaster> results = configMasterService.uploadDesignationData(file);
		
		return new ResponseEntity<List<DesignationTypeMaster>>(results, HttpStatus.OK);
	}
	

	
	@GetMapping(value = "/designationtype")
	@ApiOperation(value = "Get User Designation Type with their Herarchy", response = RegionTypeMaster.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<DesignationTypeMaster>> getDesignationTypedata(Authentication authentication) {
		
		List<DesignationTypeMaster> results =configMasterService.getDesignationTypeEntity();
		
		return new ResponseEntity<List<DesignationTypeMaster>>(results, HttpStatus.OK);
	}

}
