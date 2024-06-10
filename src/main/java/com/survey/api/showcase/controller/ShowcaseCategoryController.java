package com.survey.api.showcase.controller;

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
import com.survey.api.showcase.dto.ShowcaseCategoryDto;
import com.survey.api.showcase.service.ShowcaseCategoryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/showcasecategory")
public class ShowcaseCategoryController {
	
	@Autowired
	ShowcaseCategoryService serv;
	
	
	@ApiOperation( value = "This is used to add new ShowcaseCategory", response = ShowcaseCategoryDto.class)
	@PostMapping("/add")
	public ResponseEntity<?> saveNewShowcaseCategory(@RequestBody ShowcaseCategoryDto pgmDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ShowcaseCategoryDto result = serv.addNewShowcaseCategory(pgmDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value =  "This is used to update ShowcaseCategory", response = ShowcaseCategoryDto.class)
	@PostMapping("/{ShowcaseCategoryId}/update")
	public ResponseEntity<?> updateSurvey(@PathVariable("ShowcaseCategoryId") Long pgmId, @RequestBody ShowcaseCategoryDto pgmDto, Authentication authentication) {

		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		ShowcaseCategoryDto result = serv.updateShowcaseCategory(pgmId, pgmDto, userPrincipal.getId());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation( value = "This is used to get list of ShowcaseCategory based on filter", response = ShowCaseDto.class, responseContainer = "List")
	@GetMapping("/get")
	public ResponseEntity<?> getShowCase(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchStr, @RequestParam(defaultValue = "updatedDateTime") String sortBy, 
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Authentication authentication) {
		
		UserAuthDetails userPrincipal = (UserAuthDetails) authentication.getPrincipal();
		Page<ShowcaseCategoryDto> result = serv.getAllShowcaseCategory(pageNo, pageSize, searchStr, sortBy, sortDirection);
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	} 
	

}
