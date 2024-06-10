package com.survey.api.ruleengine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.DesignationTypeRepo;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.config.repo.RegionTypeRepo;
import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.lookup.dto.RegionDataDTO;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataENITITY;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataOPEN;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoENTITY;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoOPEN;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoPROFILE;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoPROFILE.ReportCountDtoIN;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.surveyreport.dto.ReportCountDTO;

@Service
public class SurveyReleaseRuleEngineService {

	@Autowired
	DesignationTypeRepo designationTypeRepo;

	@Autowired
	RegionTypeRepo regionTypeRepo;

	@Autowired
	RegionDataRepo regionDataRepo;

	@Autowired
	SurveyReleaseMappingRepoPROFILE surveyReleaseMappingRepoProfile;

	@Autowired
	SurveyReleaseMappingRepoOPEN surveyReleaseMappingRepoOpen;

	@Autowired
	SurveyReleaseMappingRepoENTITY surveyReleaseMappingEntityRepo;

	// METHOD to be called when publishing survey
	@Deprecated
	public void generateSurvetReleasemappingforActivityList(Long surveyId, List<SurveyReleaseData> surveyReleseDtoList, Boolean isEntitySurvey) {

		if (isEntitySurvey) {

			generateSurveyReleaseENTITY(surveyId, surveyReleseDtoList);
		} else {
			generateSurveyReleasePROFILE(surveyId, surveyReleseDtoList);

		}

	}

	public void generateSurvetReleasemapping(Long surveyId, List<SurveyReleaseData> surveyReleseDtoList, SurveyTypeEnum surveyType) {
		switch (surveyType) {
		case ENTITY:
			generateSurveyReleaseENTITY(surveyId, surveyReleseDtoList);
			break;
		case PROFILE:
			generateSurveyReleasePROFILE(surveyId, surveyReleseDtoList);
			break;
		case OPEN:
			generateSurveyReleaseOPEN(surveyId, surveyReleseDtoList);
		}
	}

	/**
	 * SEction 1 : Entity type Survey
	 */
	private void generateSurveyReleaseENTITY(Long surveyId, List<SurveyReleaseData> surveyReleseDtoList) {
		List<SurveyReleaseMappingDataENITITY> entryList = new ArrayList<>();

		for (SurveyReleaseData surveyReleaseDto : surveyReleseDtoList) {

			DesignationTypeDTO designationTypeDto = surveyReleaseDto.getDesignationTypeDto();
			List<RegionDataDTO> RegionDataDTOList = surveyReleaseDto.getRegionDataDtoList();

			DesignationTypeMaster targetDesignation = designationTypeRepo.findById(designationTypeDto.getId())
					.orElseThrow(() -> new BadRequestException("Designation not found with Id " + designationTypeDto.getId()));

			List<RegionTypeMaster> regionTypeAll = regionTypeRepo.findAllByOrderByIdAsc();
			RegionTypeMaster regionTypeOfEntity = regionTypeAll.get(regionTypeAll.size() - 1);

			for (RegionDataDTO regionDataDTO : RegionDataDTOList) {
				RegionData regionData = regionDataRepo.findById(regionDataDTO.getId())
						.orElseThrow(() -> new BadRequestException("Region Data not found with Id " + regionDataDTO.getId()));

				entryList.addAll(getChildRegionsENTITY(regionData, regionTypeOfEntity, surveyId, targetDesignation.getId()));
			}
		}
//		for (SurveyReleaseMappingEntity surveyReleaseMappingEntity : entryList) {
//			System.out.println(surveyReleaseMappingEntity);
//		}
		surveyReleaseMappingEntityRepo.saveAll(entryList);

	}

	private List<SurveyReleaseMappingDataENITITY> getChildRegionsENTITY(RegionData node, RegionTypeMaster regionTypeOfEntity, Long surveyId,
			Long targetDesignationId) {

		List<SurveyReleaseMappingDataENITITY> matchedNodes = new ArrayList<>();

		if (checkIfBaseNodeEntity(node, regionTypeOfEntity)) {
			// this means survey is released to same level user and selected region of that
			// designation
			SurveyReleaseMappingDataENITITY entity = new SurveyReleaseMappingDataENITITY();
			entity.setSurveyId(surveyId);
			entity.setTargetDesignationId(targetDesignationId);
			entity.setTargetEntityDataId(node.getId());
//			entity.setIsEntityTypeSurvey(isEntitySurvey);
			matchedNodes.add(entity);
		}
		List<RegionData> childList = regionDataRepo.findAllChildForParentId(node.getId());
		for (RegionData child : childList) {
//	    	System.out.println("Child Object analysing: "+child.getId());
			matchedNodes.addAll(getChildRegionsENTITY(child, regionTypeOfEntity, surveyId, targetDesignationId));
		}

		return matchedNodes;
	}

	private static boolean checkIfBaseNodeEntity(RegionData node, RegionTypeMaster regionTypeOfEntity) {

		return regionTypeOfEntity.getId() == node.getRegionType().getId();
	}

	/**
	 * SECTION 2 : PROFILE
	 */
	private void generateSurveyReleasePROFILE(Long surveyId, List<SurveyReleaseData> surveyReleseDtoList) {
		List<SurveyReleaseMappingDataPROFILE> entryList = new ArrayList<SurveyReleaseMappingDataPROFILE>();

		for (SurveyReleaseData surveyReleaseDto : surveyReleseDtoList) {

			DesignationTypeDTO designationTypeDto = surveyReleaseDto.getDesignationTypeDto();
			List<RegionDataDTO> RegionDataDTOList = surveyReleaseDto.getRegionDataDtoList();

			DesignationTypeMaster targetDesignation = designationTypeRepo.findById(designationTypeDto.getId())
					.orElseThrow(() -> new BadRequestException("Designation not found with Id " + designationTypeDto.getId()));
			RegionTypeMaster regionTypeOfDesignation = targetDesignation.getManagerOfRegionType();
			// drop down list of region will always be equal or parent of this region type

			for (RegionDataDTO regionDataDTO : RegionDataDTOList) {
				RegionData regionData = regionDataRepo.findById(regionDataDTO.getId())
						.orElseThrow(() -> new BadRequestException("Region Data not found with Id " + regionDataDTO.getId()));

				entryList.addAll(getChildRegionsPROFILE(regionData, regionTypeOfDesignation, surveyId, targetDesignation.getId()));
			}
		}
//		for (SurveyReleaseMappingEntity surveyReleaseMappingEntity : entryList) {
//			System.out.println(surveyReleaseMappingEntity);
//		}
		surveyReleaseMappingRepoProfile.saveAll(entryList);

	}

	private List<SurveyReleaseMappingDataPROFILE> getChildRegionsPROFILE(RegionData node, RegionTypeMaster regionTypeOfDesignation, Long surveyId,
			Long targetDesignationId) {

		List<SurveyReleaseMappingDataPROFILE> matchedNodes = new ArrayList<>();

		if (checkIfBaseNodePROFILE_OPEN(node, regionTypeOfDesignation)) {
			// this means survey is released to same level user and selected region of that
			// designation
			SurveyReleaseMappingDataPROFILE entity = new SurveyReleaseMappingDataPROFILE();
			entity.setSurveyId(surveyId);
			entity.setTargetDesignationId(targetDesignationId);
			entity.setTargetRegionDataId(node.getId());
//			entity.setIsEntityTypeSurvey(isEntitySurvey);
			matchedNodes.add(entity);
		}
		List<RegionData> childList = regionDataRepo.findAllChildForParentId(node.getId());
		for (RegionData child : childList) {
			System.out.println("Child Object analysing: " + child.getId());
			matchedNodes.addAll(getChildRegionsPROFILE(child, regionTypeOfDesignation, surveyId, targetDesignationId));
		}

		return matchedNodes;
	}

	private static boolean checkIfBaseNodePROFILE_OPEN(RegionData node, RegionTypeMaster regionTypeOfDesignation) {

		return regionTypeOfDesignation.getId() == node.getRegionType().getId();
	}

	/**
	 * OPEN TYPE SURVEY
	 */

	private void generateSurveyReleaseOPEN(Long surveyId, List<SurveyReleaseData> surveyReleseDtoList) {
		List<SurveyReleaseMappingDataOPEN> entryList = new ArrayList<SurveyReleaseMappingDataOPEN>();

		for (SurveyReleaseData surveyReleaseDto : surveyReleseDtoList) {

			DesignationTypeDTO designationTypeDto = surveyReleaseDto.getDesignationTypeDto();
			List<RegionDataDTO> RegionDataDTOList = surveyReleaseDto.getRegionDataDtoList();

			DesignationTypeMaster targetDesignation = designationTypeRepo.findById(designationTypeDto.getId())
					.orElseThrow(() -> new BadRequestException("Designation not found with Id " + designationTypeDto.getId()));
			RegionTypeMaster regionTypeOfDesignation = targetDesignation.getManagerOfRegionType();
			// drop down list of region will always be equal or parent of this region type

			for (RegionDataDTO regionDataDTO : RegionDataDTOList) {
				RegionData regionData = regionDataRepo.findById(regionDataDTO.getId())
						.orElseThrow(() -> new BadRequestException("Region Data not found with Id " + regionDataDTO.getId()));

				entryList.addAll(getChildRegionsOPEN(regionData, regionTypeOfDesignation, surveyId, targetDesignation.getId()));
			}
		}
		surveyReleaseMappingRepoOpen.saveAll(entryList);

	}

	private List<SurveyReleaseMappingDataOPEN> getChildRegionsOPEN(RegionData node, RegionTypeMaster regionTypeOfDesignation, Long surveyId,
			Long targetDesignationId) {

		List<SurveyReleaseMappingDataOPEN> matchedNodes = new ArrayList<>();

		if (checkIfBaseNodePROFILE_OPEN(node, regionTypeOfDesignation)) {
			// this means survey is released to same level user and selected region of that
			// designation
			SurveyReleaseMappingDataOPEN entity = new SurveyReleaseMappingDataOPEN();
			entity.setSurveyId(surveyId);
			entity.setTargetDesignationId(targetDesignationId);
			entity.setTargetRegionDataId(node.getId());
//			entity.setIsEntityTypeSurvey(isEntitySurvey);
			matchedNodes.add(entity);
		}
		List<RegionData> childList = regionDataRepo.findAllChildForParentId(node.getId());
		for (RegionData child : childList) {
			System.out.println("Child Object analysing: " + child.getId());
			matchedNodes.addAll(getChildRegionsOPEN(child, regionTypeOfDesignation, surveyId, targetDesignationId));
		}

		return matchedNodes;
	}

	/**
	 * Section 3 : Survey Id for Activity THis method return survey activity for
	 * Logged in User's region Id and Designation
	 * 
	 */

//	public List<Long> getSurveyIdForActivityPROFILE(Long userDesignationTypeId, Long userRegionDataId){
//		List<Long> surveyIdList = surveyReleaseMappingRepoProfile.findAllIncompleteSurveyIdByDesignationAndRegion(userDesignationTypeId,userRegionDataId,false);
//		return surveyIdList;
//		
//	}

	public List<SurveyReleaseMappingDataPROFILE> getReleaseMappingForActivityPROFILE(Long userDesignationTypeId, Long userRegionDataId) {
		List<SurveyReleaseMappingDataPROFILE> releasemappingList = surveyReleaseMappingRepoProfile
				.findByTargetDesignationIdAndTargetRegionDataIdAndIsSurveyExpired(userDesignationTypeId, userRegionDataId,
						false);
		return releasemappingList;

	}

	public List<SurveyReleaseMappingDataENITITY> getReleaseMappingForActivityENTITY(Long userDesignationTypeId) {
		List<SurveyReleaseMappingDataENITITY> releasemappingList = surveyReleaseMappingEntityRepo
				.findByTargetDesignationIdAndIsSurveyAnsweredAndIsSurveyExpired(userDesignationTypeId, false, false);
		return releasemappingList;

	}

	public List<SurveyReleaseMappingDataOPEN> getReleaseMappingForActivityOPEN(Long userDesignationTypeId, Long userRegionDataId) {
		List<SurveyReleaseMappingDataOPEN> releasemappingList = surveyReleaseMappingRepoOpen
				.findByTargetDesignationIdAndTargetRegionDataIdAndIsSurveyAnsweredAndIsSurveyExpired(userDesignationTypeId, userRegionDataId,false, false);
		return releasemappingList;

	}

	/**
	 * Section 4 Mark SUrvey completed so that it will not be returned next time
	 * activity list if fetched. TODO : batch job daily to delete completed survey's
	 * of user
	 * 
	 */

	public Boolean updateSurveyAnsweredPROFILE(Long surveyId, Long userDesignationID, Long userRegionId, Boolean isSurveyAnsweredCompletly,
			Boolean isSurveyInProgress) {

		List<SurveyReleaseMappingDataPROFILE> entities = surveyReleaseMappingRepoProfile
				.findBySurveyIdAndTargetDesignationIdAndTargetRegionDataId(surveyId, userDesignationID, userRegionId);
		if (entities.size() == 1) {
			SurveyReleaseMappingDataPROFILE entity = entities.get(0);
			entity.setIsSurveyAnswered(isSurveyAnsweredCompletly);
			entity.setIsSurveyInProgress(isSurveyInProgress);
			surveyReleaseMappingRepoProfile.save(entity);
		} else {
			throw new BadRequestException("Issue with QUery. THere is not mapping exist in Release Rule Engine for PROFILE Survey");
		}

		return true;

	}
	
	public Boolean updateSurveyAnsweredOPEN(Long surveyId, Long userDesignationID, Long userRegionId, Boolean isSurveyAnsweredCompletly,
			Boolean isSurveyInProgress) {

		List<SurveyReleaseMappingDataOPEN> entities = surveyReleaseMappingRepoOpen
				.findBySurveyIdAndTargetDesignationIdAndTargetRegionDataId(surveyId, userDesignationID, userRegionId);
		if (entities.size() == 1) {
			SurveyReleaseMappingDataOPEN entity = entities.get(0);
			entity.setIsSurveyAnswered(isSurveyAnsweredCompletly);
			entity.setIsSurveyInProgress(isSurveyInProgress);
			surveyReleaseMappingRepoOpen.save(entity);
		} else {
			throw new BadRequestException("Issue with QUery. THere is not mapping exist in Release Rule Engine for PROFILE Survey");
		}

		return true;

	}

	public Boolean updateSurveyAnsweredENTITY(Long surveyId, Long userDesignationID, Long entityId, Boolean isSurveyAnsweredCompletly,
			Boolean isSurveyInProgress) {

		List<SurveyReleaseMappingDataENITITY> entities = surveyReleaseMappingEntityRepo
				.findBySurveyIdAndTargetDesignationIdAndTargetEntityDataId(surveyId, userDesignationID, entityId);
		if (entities.size() == 1) {
			SurveyReleaseMappingDataENITITY entity = entities.get(0);
			entity.setIsSurveyAnswered(isSurveyAnsweredCompletly);
			entity.setIsSurveyInProgress(isSurveyInProgress);
			surveyReleaseMappingEntityRepo.save(entity);
		} else {
			throw new BadRequestException("Issue with Query. THere is not mapping exist in Release Rule Engine for ENTITY Survey");
		}

		return true;

	}
	
	

	@Deprecated
	public Boolean updateSurveyExpired(Long surveyId, Boolean isEntitySurvey) {

		if (isEntitySurvey) {

			List<SurveyReleaseMappingDataENITITY> entities = surveyReleaseMappingEntityRepo.findBySurveyId(surveyId);
			for (SurveyReleaseMappingDataENITITY entity : entities) {
				entity.setIsSurveyExpired(true);
			}
			surveyReleaseMappingEntityRepo.saveAll(entities);

		} else {
			List<SurveyReleaseMappingDataPROFILE> entities = surveyReleaseMappingRepoProfile.findBySurveyId(surveyId);
			for (SurveyReleaseMappingDataPROFILE entity : entities) {
				entity.setIsSurveyExpired(true);
			}
			surveyReleaseMappingRepoProfile.saveAll(entities);
		}
		return true;

	}

	public Boolean updateSurveyExpired(Long surveyId, SurveyTypeEnum surveyType) {
		switch (surveyType) {
		case ENTITY:
			List<SurveyReleaseMappingDataENITITY> entities = surveyReleaseMappingEntityRepo.findBySurveyId(surveyId);
			for (SurveyReleaseMappingDataENITITY entity : entities) {
				entity.setIsSurveyExpired(true);
			}
			surveyReleaseMappingEntityRepo.saveAll(entities);
			break;
		case PROFILE:
			List<SurveyReleaseMappingDataPROFILE> entitiesProfile = surveyReleaseMappingRepoProfile.findBySurveyId(surveyId);
			for (SurveyReleaseMappingDataPROFILE entity : entitiesProfile) {
				entity.setIsSurveyExpired(true);
			}
			surveyReleaseMappingRepoProfile.saveAll(entitiesProfile);
			break;
		case OPEN:
			List<SurveyReleaseMappingDataOPEN> entitiesOpen = surveyReleaseMappingRepoOpen.findBySurveyId(surveyId);
			for (SurveyReleaseMappingDataOPEN entity : entitiesOpen) {
				entity.setIsSurveyExpired(true);
			}
			surveyReleaseMappingRepoOpen.saveAll(entitiesOpen);
			break;
		}

		return true;

	}

	/**
	 * Count of the Users
	 */
	public List<ReportCountDTO> getSurveyCount(List<Long> surveyIdList) {

		List<ReportCountDTO> resultList = new ArrayList<ReportCountDTO>();
		// get survey for profile type surveys

		List<ReportCountDtoIN> profileSurveyList = surveyReleaseMappingRepoProfile.findreportMetrix(surveyIdList);

		for (ReportCountDtoIN in : profileSurveyList) {
			ReportCountDTO dto = new ReportCountDTO();
			dto.setSurveyId(in.getSurveyId());
			dto.setReleaseCount(in.getReleaseCount());
			dto.setResponseCount(in.getResponseCount());
			dto.setIsEntityType(false);
			resultList.add(dto);
		}

		// get survey report for eneity type surveys
		List<ReportCountDtoIN> entitySurveyList = surveyReleaseMappingEntityRepo.findreportMetrix(surveyIdList);
		for (ReportCountDtoIN in : entitySurveyList) {
			ReportCountDTO dto = new ReportCountDTO();
			dto.setSurveyId(in.getSurveyId());
			dto.setReleaseCount(in.getReleaseCount());
			dto.setResponseCount(in.getResponseCount());
			dto.setIsEntityType(false);
			resultList.add(dto);
		}
		
		// get survey report for OPEN type surveys
		List<ReportCountDtoIN> OpenSurveyList = surveyReleaseMappingRepoOpen.findreportMetrix(surveyIdList);
		for (ReportCountDtoIN in : OpenSurveyList) {
			ReportCountDTO dto = new ReportCountDTO();
			dto.setSurveyId(in.getSurveyId());
			dto.setReleaseCount(in.getReleaseCount());
			dto.setResponseCount(in.getResponseCount());
			dto.setIsEntityType(false);
			resultList.add(dto);
		}

		return resultList;
	}

}
