package com.survey.api.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.user.dto.UserDTO;
import com.survey.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@Api(value=" User Management", description="User Management Section. Can be used by Admin and Support team")

public class UserMgmtController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping(value = "/create")
	@ApiOperation(value = "Create Users for the App. Both Support and Normal User who will fill survey. "
			+ "Only Admin can create Support User. Normal user can be created by Support/Admin", response = UserDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPPORT')")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
		
		UserAuthDetails auth = (UserAuthDetails) authentication.getPrincipal();

		UserDTO result =userService.addUser(userDTO);
		
		return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
	}
	
	@PostMapping(value = "/update")
	@ApiOperation(value = "Update Users for the App. Both Support and normal User who will fill survey", response = UserDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPPORT')")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
		
		UserDTO result =userService.updateUser(userDTO);
		
		return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/get/{userid}")
	@ApiOperation(value = "Update Users for the App. Both Support and normal User who will fill survey", response = UserDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<UserDTO> getUser(@PathVariable(name = "userid")Long userid, Authentication authentication) {
		
		UserDTO result =userService.getUser(userid);
		
		return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
	}
	
	@PostMapping(value = "/uploadusersdata")
	@ApiOperation(value = "Import users Data. Should be Called after User Designation and Region are populated in DB", response = UserDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Completed"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPPORT')")
	public ResponseEntity<List<UserDTO>> importUser(@RequestParam("file") MultipartFile file,
			Authentication authentication) {
		
		List<UserDTO> results =new ArrayList<>();
		
		return new ResponseEntity<List<UserDTO>>(results, HttpStatus.OK);
	}
	
	
}
