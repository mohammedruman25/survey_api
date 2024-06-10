package com.survey.api.config.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.repo.DesignationTypeRepo;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.user.entity.ERole;
import com.survey.api.user.entity.Role;
import com.survey.api.user.entity.User;
import com.survey.api.user.repo.UserRepo;
import com.survey.api.user.repo.UserRoleRepo;

@Service
public class ConfigUserDataService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	DesignationTypeRepo designationTypeRepo;

	@Autowired
	RegionDataRepo regionDataRepo;

	@Autowired
	UserRoleRepo roleRepo;

	final Integer NUMBER_OF_COLUMN_CSV = 6;

	public Boolean uploadUserData(MultipartFile file) {

		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);) {

			List<CSVRecord> csvRecords = csvParser.getRecords();

			List<DesignationTypeMaster> designationTypesList = designationTypeRepo.findAllByOrderByIdAsc();
			for (DesignationTypeMaster designationTypeMaster : designationTypesList) {
				System.out.println(designationTypeMaster.getName());
			}
			// Assumption is there will only be CODE and NAME in sequence
			loadUserData(csvRecords, designationTypesList);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void loadUserData(List<CSVRecord> csvRecords, List<DesignationTypeMaster> designationTypesList) {
		// One CSV record will have commit equal to herirachy
		CSVRecord csvRecord = null;
		try {
			// skip first two records
			int titleRecordIndex = 0;
			for (int j = 0; j < csvRecords.size(); j++) {
				// Skip first two records as they are headers
				if (titleRecordIndex < 1) {
					titleRecordIndex++;
					continue;
				}
				// load the Record;
				csvRecord = csvRecords.get(j);
				System.out.println("*****" + csvRecord);
				insertUserData(csvRecord, designationTypesList);
			}
		} catch (BadRequestException eb) {
			throw eb;
		} catch (Exception e) {
			throw new BadRequestException("Issue in importing the File with Record " + csvRecord);
		}

	}

	private void insertUserData(CSVRecord csvRecord, List<DesignationTypeMaster> designationTypesList) {

		// Check if Records is of expected length
		if (csvRecord.size() != NUMBER_OF_COLUMN_CSV) {
			throw new BadRequestException("Issue in size of record please check " + csvRecord);
		}
		// Prepare Entity
		User userentity = new User();
		userentity.setFirstName(csvRecord.get(0));
		userentity.setLastName(csvRecord.get(1));
		// setPhoneNumber
		String phoneNumber = csvRecord.get(2);
		Boolean isAccountExist = userRepo.existsByPhoneNumber(phoneNumber);
		if (isAccountExist) {
			throw new BadRequestException("Error: User phone Number already exist " + csvRecord);
		}
		userentity.setPhoneNumber(phoneNumber);
		userentity.setCountryCode(csvRecord.get(3));
		
		// Set Designation
		String designationCode = csvRecord.get(4);
		DesignationTypeMaster designation = designationTypeRepo.findByImportCode(designationCode)
				.orElseThrow(() -> new BadRequestException("Error: DesignationCode not Found " + csvRecord));
		userentity.setDesignation(designation);
		// Set RegionData
		String regionCode = csvRecord.get(5);
		if(!(regionCode ==null || regionCode.equalsIgnoreCase("")||regionCode.equalsIgnoreCase("NA"))) {
			RegionData regionData = regionDataRepo.findByCode(regionCode)
					.orElseThrow(() -> new BadRequestException("Error: RegionData Code not Found " + csvRecord));
			userentity.setRegionData(regionData);
		}else {
			//check to make sure it is parent user
			if(userentity.getDesignation().getManagerOfRegionType().getParentRegionType()!=null) {
				throw new BadRequestException("Error: Designation of Highest Level can have null Region. Record: " + csvRecord);

			}
		}
		//Check if user can belong to that Designation and Region 
		//eg. Principal of school can not be Region District. 
		
		if((userentity.getRegionData()!=null) && (userentity.getDesignation().getManagerOfRegionType().getId()!= userentity.getRegionData().getRegionType().getId())) {
			throw new BadRequestException("Error: Designation and RegionData Code not does not match. They should belond to same RegionType Level. Record: " + csvRecord);
		}
		
		
		// Add Role of Imported user
		Set<Role> roles = new HashSet<>();
		Role role = roleRepo.findByName(ERole.USER)
				.orElseThrow(() -> new BadRequestException("Error: Role is not found."));
		roles.add(role);
		userentity.setRoles(roles);
		userRepo.save(userentity);
	}

}
