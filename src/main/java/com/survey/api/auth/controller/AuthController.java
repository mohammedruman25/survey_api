package com.survey.api.auth.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.AdminSignup;
import com.survey.api.auth.domain.JwtResponse;
import com.survey.api.auth.domain.LoginRequest;
import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.auth.service.JwtUtils;
import com.survey.api.auth.service.UserAuthService;
import com.survey.api.common.dto.Response;
import com.survey.api.user.entity.User;
import com.survey.api.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${app.version}")
	private String version;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserAuthService authService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ApiOperation(value = "Version API", response = String.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered list"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<String> ping(Authentication authentication) {
		logger.info("Pinging back application version");
		return new ResponseEntity<String>(version, HttpStatus.OK);
	}
	
	@PostMapping("/createAdmin")
	@ApiOperation(value = "This api can only be used once. it will create admin of the system.")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully Registered"),
	        @ApiResponse(code = 400, message = "Bad Request"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminSignup signUpRequest) {
		
		userService.createAdmin(signUpRequest);
		return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "ADMIN registered successfully!"));
	}
	

	
	@ApiOperation("This is used to generate OTP for signin or signup. Response : Boolean firstTimeUser")
	@PostMapping("/otp/generate")
	public ResponseEntity<?> generateOtp(@RequestBody LoginRequest loginRequest) {
		
		String result = userService.generateOtp(loginRequest.getPhoneNo());
    	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), result) );
    	
	}
	
	@ApiOperation("This is used to validate the OTP")
	@PostMapping("/otp/validate")
	public ResponseEntity<?> verifyOtp(@RequestBody LoginRequest loginRequest) {
		
		//After OPT is validate Generate User Context in Token and Return Object
		Authentication authentication = null;
		authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNo(), loginRequest.getOtp()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		User user = userService.getById(userDetails.getId());
		
		JwtResponse result = new JwtResponse(jwt,"Bearer", 
				 userDetails.getId(),
				 userDetails.getPhoneNo(),
				 userDetails.getFirstName(), user.getLastName(),
				 roles);
		
		return ResponseEntity.ok(result);

	}
	

}
