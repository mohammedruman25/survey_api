package com.survey.api.user.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.user.entity.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is user DTO class
 * 
 * @author Wearout
 * @version 1.0
 */

@Setter
@Getter
@NoArgsConstructor
public class UserDTO implements Serializable {

	private static final long serialVersionUID = -738054256908908438L;
	
	private Long id;
//	private String userUuid;
//	private String userName;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String email;
	@JsonIgnore
	private String otp;
	@JsonIgnore
	private int otpIncorrectAttempt;
	private boolean accountLocked;
// 	private String password;
 	private Role role;
 	private DesignationTypeMaster designation;
	private RegionData regionData;
	private Boolean isEntitymanager;
	

}
