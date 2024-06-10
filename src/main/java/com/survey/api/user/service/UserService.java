package com.survey.api.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.survey.api.auth.domain.AdminSignup;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.config.repo.RegionTypeRepo;
import com.survey.api.user.dto.UserDTO;
import com.survey.api.user.entity.ERole;
import com.survey.api.user.entity.Role;
import com.survey.api.user.entity.User;
import com.survey.api.user.repo.UserRepo;
import com.survey.api.user.repo.UserRoleRepo;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserRoleRepo roleRepo;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	RegionTypeRepo regionTypeRepo;

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public String generateOtp(String phoneNumber) {

		User user = userRepo.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new BadRequestException("Error: User not found. Please contact Support"));


//		String otpGenerated = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
//				.substring(32, 36);+
		String otpGenerated = "1234";
		user.setOtp(encoder.encode(otpGenerated));
		user.setOtpIncorrectAttempt(0);

		user = userRepo.save(user);

//		sendMessage.sendSMSMessage("Your Wearout login OTP is: " + otpGenerated, env.getProperty("appId"),
//				env.getProperty("originationNumber"), phoneNumber);
		logger.info("PhoneNumber :" + phoneNumber + " otp:" + otpGenerated);
		return new String("Otp Generated Successfully");
	}

	@Transactional
	public UserDTO addUser(UserDTO userDTO) {

		Boolean isAccountExist = userRepo.existsByPhoneNumber(userDTO.getPhoneNumber());
		if (isAccountExist) {
			throw new BadRequestException("Error: User already exist");
		}
	
		User entity = userRepo.save(getEntityfromDTO(userDTO));

		return getDTOFromEntity(entity);
	}
	
	@Transactional
	public UserDTO updateUser(UserDTO userDTO) {
		
		User user = userRepo.findById(userDTO.getId())
				.orElseThrow(() -> new BadRequestException("Given ID not found"));

		
		User entity = userRepo.save(getEntityfromDTO(userDTO));
		return getDTOFromEntity(entity);
	}
	
	public UserDTO getUser(Long userid) {
		User user = userRepo.findById(userid)
				.orElseThrow(() -> new BadRequestException("Given ID not found"));
		UserDTO userDto=  getDTOFromEntity(user);
		if(user.getRegionData()!=null) {
			
			//check if region data type is bottom one 
			List<RegionTypeMaster> regionDataMasterList =regionTypeRepo.findAllChildForParentId(user.getRegionData().getRegionType().getId());
			if(regionDataMasterList.size()==0) {
				userDto.setIsEntitymanager(true);
			}
		}
		
		
		return userDto;
	}

	
	private User getEntityfromDTO(@Valid UserDTO dto) {

		User entity = new User();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		entity = mapper.convertValue(dto, User.class);

		// Add Roles
		Set<Role> roles = new HashSet<>();
		
		if (dto.getRole() == null) {
			throw new BadRequestException("Error: Role is not found.");
		}
		
		Role role = new Role();
		role = roleRepo.findByName((dto.getRole().getName())).orElseThrow(() -> new BadRequestException("Error: Role is not found."));
		roles.add(role);
		entity.setRoles(roles);
		return entity;

	}
	
	private UserDTO getDTOFromEntity(User entity) {

		UserDTO dto = new UserDTO();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		dto = mapper.convertValue(entity, UserDTO.class);
		Role role = entity.getRoles().iterator().next();
		Role dtorole= new Role();
		dtorole.setId(role.getId());
		dtorole.setName(role.getName());
		dto.setRole(dtorole);
		return dto;
	}
	
	public User getById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new BadRequestException("Error: User Id is not found."));
	}

//	public UserDTO getUserbyUserName(String userUuid) {
//		User user = userRepo.findByUserUuid(userUuid)
//				.orElseThrow(() -> new BadRequestException("Error: User uuid not found."));
//		UserDTO dto = getBoFromEntity(user);
//		return dto;
//	}

	
	@Transactional
	public Boolean createAdmin(@Valid AdminSignup signUpRequest) {

		if (userRepo.existsByPhoneNumber(signUpRequest.getPhoneNo())) {
			throw new BadRequestException("Error: Admin Already Exist for the system !");
		}

		User admin = new User();

		admin.setFirstName(signUpRequest.getFirstName());
		admin.setLastName(signUpRequest.getLastName());

//		admin.setPassword(encoder.encode(signUpRequest.getPassword()));
		admin.setPhoneNumber(signUpRequest.getPhoneNo());
		admin.setEmail(signUpRequest.getEmail());

		Set<Role> roles = new HashSet<>();
		// SignUp Role will be ADMIN
		Role adminRole = roleRepo.findByName(ERole.ADMIN)
				.orElseThrow(() -> new BadRequestException("Error: Role is not found in Lookup table"));
		roles.add(adminRole);
		admin.setRoles(roles);
		admin = userRepo.save(admin);

		if (admin.getId() == null) {
			throw new BadRequestException("Issue while creating the Admin");
		}

		return true;

	}
	
	
	


}
