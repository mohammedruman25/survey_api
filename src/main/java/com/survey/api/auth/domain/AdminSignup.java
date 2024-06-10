package com.survey.api.auth.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@ApiModel(description = "All details about the Admin. ")
@Getter @Setter @NoArgsConstructor
public class AdminSignup {
	
    @NotBlank
    @Size(max = 20)
    @ApiModelProperty(notes = "Required")
    private String phoneNo;
    
	@NotBlank
    @ApiModelProperty(notes = "Required")
	private String countryCode;
    
//    @NotBlank
//    @Size(min = 6, max = 40)
//    @ApiModelProperty(notes = "Required")
//    private String password;
	
    
    @NotBlank
    @Size(min = 3, max = 20)
    @ApiModelProperty(notes = "Required")
    private String firstName;
    
    @NotBlank
    @Size(min = 3, max = 20)
    @ApiModelProperty(notes = "Required")
    private String lastName;
 
    @Email
    private String email;

}
