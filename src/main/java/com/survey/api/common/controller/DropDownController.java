package com.survey.api.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.lookup.dto.RegionDataDTO;
import com.survey.api.lookup.dto.RegionTypeDTO;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.common.service.DropDownService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/dropdown")
@Api(value=" DropDown data", description="Data for Region and Designation Dropdowns for Publish survey.")
public class DropDownController {
	
	@Autowired
	DropDownService dropDownService;
	
	@GetMapping(value = "/designation/type")
	@ApiOperation(value = "Get Designation Drop Down . Single Option selected Preferred" , response = DesignationTypeDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<DesignationTypeDTO>> getDesignation(Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

		List<DesignationTypeDTO> result = dropDownService.getAllDesignationType();
		
		return new ResponseEntity<List<DesignationTypeDTO>>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/survey/type")
	@ApiOperation(value = "GetSurvey Type in DROP Down" , response = String.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<String>> getSurveyType(Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

		List<String> result = new ArrayList<String>();
		result.add(SurveyTypeEnum.PROFILE.toString());
		result.add(SurveyTypeEnum.ENTITY.toString());
		result.add(SurveyTypeEnum.OPEN.toString());
		
		return new ResponseEntity<List<String>>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/region/type/{designationTypeId}")
	@ApiOperation(value = "Get Region Type DropDown where we can release the survey" , response = RegionTypeDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<RegionTypeDTO>> getSuperRegionsType(@PathVariable(name = "designationTypeId")Long designationTypeId,Authentication authentication) {
		
//		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
		List<RegionTypeDTO> result = dropDownService.getAllSuperRegionType(designationTypeId);
		return new ResponseEntity<List<RegionTypeDTO>>(result, HttpStatus.OK);
	}
	
//	@GetMapping(value = "/region/type")
//	@ApiOperation(value = "Get Region Data Drop Down" , response = RegionTypeDTO.class)
//	@ApiResponses(value = {
//	        @ApiResponse(code = 200, message = "Successfully Completed"),
//	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
//	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
//	})
//	public ResponseEntity<List<RegionTypeDTO>> getRegionsType(Authentication authentication) {
//		
////		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
//		List<RegionTypeDTO> result = dropDownService.getAllRegionType();
//		return new ResponseEntity<List<RegionTypeDTO>>(result, HttpStatus.OK);
//	}
	
	@GetMapping(value = "/region/data/{regionTypeId}")
	@ApiOperation(value = "Get Region Drop Down" , response = RegionDataDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<List<RegionDataDTO>> getRegions(@PathVariable(name = "regionTypeId")Long regionTypeId,Authentication authentication) {
		
		List<RegionDataDTO> result = dropDownService.getRegionListByRegionTypeId(regionTypeId);
		return new ResponseEntity<List<RegionDataDTO>>(result, HttpStatus.OK);
	}

//	@GetMapping(value = "/designation/type/regionTypeId")
//	@ApiOperation(value = "Get Designation Drop Down" , response = DesignationTypeDTO.class)
//	@ApiResponses(value = {
//	        @ApiResponse(code = 200, message = "Successfully Completed"),
//	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
//	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
//	})
//	public ResponseEntity<List<DesignationTypeDTO>> getDesignation(@PathVariable(name = "regionTypeId")Long regionTypeId,Authentication authentication) {
//		
////		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();
//
//		List<DesignationTypeDTO> result = dropDownService.getAllChildDesignationTypeforRegionType();
//		
//		return new ResponseEntity<List<DesignationTypeDTO>>(result, HttpStatus.OK);
//	}

}
