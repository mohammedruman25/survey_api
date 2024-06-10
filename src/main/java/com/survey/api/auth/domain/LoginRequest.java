package com.survey.api.auth.domain;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Login details")
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

	@NotBlank
    @ApiModelProperty(notes = "Required")
	private String phoneNo;
	
	@NotBlank
    @ApiModelProperty(notes = "Required")
	private String countryCode;

	@NotBlank
	private String otp;
	
}


