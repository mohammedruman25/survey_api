package com.survey.api.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.DesignationTypeRepo;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.config.repo.RegionTypeRepo;
import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.lookup.dto.RegionDataDTO;
import com.survey.api.lookup.dto.RegionTypeDTO;

@Service
public class DropDownService {
	
	@Autowired 
	DesignationTypeRepo designationTypeRepo;
	
	@Autowired
	RegionTypeRepo regionTypeRepo;
	
	@Autowired
	RegionDataRepo regionDataRepo;
	


	public List<DesignationTypeDTO> getAllDesignationType() {
		
		List<DesignationTypeMaster> entityList = designationTypeRepo.findAllByOrderByIdAsc();
		List<DesignationTypeDTO> dtoList = new ArrayList<DesignationTypeDTO>();
		
		for (DesignationTypeMaster designationTypeMaster : entityList) {
			
			DesignationTypeDTO dto = new DesignationTypeDTO();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			dto = mapper.convertValue(designationTypeMaster, DesignationTypeDTO.class);
			
			dto.setIsEntityManager(designationTypeMaster.getManagerOfRegionType().getIsEnityType());
			dtoList.add(dto);
		}
		
		return dtoList;
	}

	public List<RegionTypeDTO> getAllRegionType() {
		
		List<RegionTypeMaster> entityList = regionTypeRepo.findAllByOrderByIdAsc();
		List<RegionTypeDTO> dtoList = new ArrayList<RegionTypeDTO>();

		for (RegionTypeMaster regionTypeMaster : entityList) {
			
			RegionTypeDTO dto = new RegionTypeDTO();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			
			dto = mapper.convertValue(regionTypeMaster, RegionTypeDTO.class);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public List<RegionTypeDTO> getAllSuperRegionType(Long designationTypeId) {
		
		DesignationTypeMaster designationType = designationTypeRepo.findById(designationTypeId)
				.orElseThrow(() -> new UsernameNotFoundException("Designation Type Not found with Id : " + designationTypeId));
		List<RegionTypeMaster> entityList = new ArrayList<RegionTypeMaster>();
		RegionTypeMaster baseRegionType = designationType.getManagerOfRegionType();
		entityList.add(baseRegionType);
		while(baseRegionType.getParentRegionType()!=null) {
			entityList.add(baseRegionType.getParentRegionType());
			baseRegionType = baseRegionType.getParentRegionType();
		}
		List<RegionTypeDTO> dtoList = new ArrayList<RegionTypeDTO>();
		for (RegionTypeMaster regionTypeMaster : entityList) {
			RegionTypeDTO dto = new RegionTypeDTO();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			
			dto = mapper.convertValue(regionTypeMaster, RegionTypeDTO.class);
			dtoList.add(dto);
		}
		return dtoList;
	}


	public List<RegionDataDTO> getRegionListByRegionTypeId(Long regionTypeId) {
		RegionTypeMaster regionType = regionTypeRepo.findById(regionTypeId)
				.orElseThrow(() -> new UsernameNotFoundException("Region Type Not found with Id : " + regionTypeId));
		
		List<RegionData> entityList = regionDataRepo.findAllByRegionTypeOrderByNameAsc(regionType);
		
		List<RegionDataDTO> dtoList = new ArrayList<RegionDataDTO>();

		for (RegionData entity : entityList) {
			
			RegionDataDTO dto = new RegionDataDTO();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			
			dto = mapper.convertValue(entity, RegionDataDTO.class);
			dtoList.add(dto);
		}
		return dtoList;
	}

	
	
	
}
