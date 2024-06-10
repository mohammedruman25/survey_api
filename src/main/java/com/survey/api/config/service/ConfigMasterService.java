package com.survey.api.config.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.DesignationTypeRepo;
import com.survey.api.config.repo.RegionTypeRepo;

@Service
public class ConfigMasterService {
	
	@Autowired
	RegionTypeRepo regionTypeRepo;
	
	@Autowired
	DesignationTypeRepo designationTypeRepo;

	public List<RegionTypeMaster> getRegionTypeEntity() {
		return regionTypeRepo.findAllByOrderByIdAsc();
	}

	public List<DesignationTypeMaster> getDesignationTypeEntity() {
		return designationTypeRepo.findAll();
	}

	public List<RegionTypeMaster> uploadRegionTypeData(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DesignationTypeMaster> uploadDesignationData(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
