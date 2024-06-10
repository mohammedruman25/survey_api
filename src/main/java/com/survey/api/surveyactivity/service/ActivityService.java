package com.survey.api.surveyactivity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataENITITY;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataOPEN;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
import com.survey.api.ruleengine.service.SurveyReleaseRuleEngineService;
import com.survey.api.surveyactivity.dto.ActivityDto;
import com.survey.api.surveymgmt.dto.ProgramDtoSmall;
import com.survey.api.surveymgmt.dto.SurveyDto;
import com.survey.api.surveymgmt.dto.SurveySortByEnum;
import com.survey.api.surveymgmt.entity.Program;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.entity.SurveyStatus;
import com.survey.api.surveymgmt.repo.SurveyRepo;
import com.survey.api.surveymgmt.service.SurveyService;

@Service
public class ActivityService {

	@Autowired
	private SurveyReleaseRuleEngineService releaseRuleServ;

	@Autowired
	private SurveyRepo surveyRepo;

	@Autowired
	private RegionDataRepo regionDataRepo;

	public List<ActivityDto> getAllProfileActivity(Long userDesignationId, Long userRegionId) {

		if (userDesignationId == null) {
			throw new BadRequestException("Designation of User is Null");
		}
		if (userRegionId == null) {
			throw new BadRequestException("No Region Data is assigned to user");
		}

		List<ActivityDto> resultList = new ArrayList<ActivityDto>();
		List<SurveyReleaseMappingDataPROFILE> surveyReleaseMappingList = releaseRuleServ.getReleaseMappingForActivityPROFILE(userDesignationId, userRegionId);
		for (SurveyReleaseMappingDataPROFILE surveyReleaseMappingDataPROFILE : surveyReleaseMappingList) {
			
			Survey survey =  surveyRepo.getOne(surveyReleaseMappingDataPROFILE.getSurveyId());
			resultList.add(getActivityFromSurvey(survey, surveyReleaseMappingDataPROFILE.getIsSurveyInProgress()));
		}
		return resultList;
	}
	
	public List<ActivityDto> getAllActivityOPEN(Long userDesignationId, Long userRegionId) {

		if (userDesignationId == null) {
			throw new BadRequestException("Designation of User is Null");
		}
		if (userRegionId == null) {
			throw new BadRequestException("No Region Data is assigned to user");
		}

		List<ActivityDto> resultList = new ArrayList<ActivityDto>();
		List<SurveyReleaseMappingDataOPEN> surveyReleaseMappingList = releaseRuleServ.getReleaseMappingForActivityOPEN(userDesignationId, userRegionId);
		for (SurveyReleaseMappingDataOPEN surveyReleaseMappingDataOPEN : surveyReleaseMappingList) {
			
			Survey survey =  surveyRepo.getOne(surveyReleaseMappingDataOPEN.getSurveyId());
			resultList.add(getActivityFromSurvey(survey, surveyReleaseMappingDataOPEN.getIsSurveyInProgress()));
		}
		return resultList;
	}
	
	/**
	 * Entity Activity Section 
	 */

	public List<ActivityDto> getAllEntityActivity(Long userDesignationId, Long userRegionId) {

		List<ActivityDto> resultList = new ArrayList<ActivityDto>();
		List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList = releaseRuleServ.getReleaseMappingForActivityENTITY(userDesignationId);

		// Step 1 : Find the List of Region of the User
		List<RegionData> regionDataList = regionDataRepo.findAllChildForParentId(userRegionId);
		if (regionDataList.size() == 0) {
			return new ArrayList<ActivityDto>();
		}
		// Iterate through it.
		for (RegionData regionData : regionDataList) {
			ActivityDto activity = getActivityFromRegionData(regionData);
			activity.setChildren(getChildrenActivityForRegion(regionData,surveyReleaseMappingList));
			resultList.add(activity);
		}
		return resultList;
	}

	private List<ActivityDto> getChildrenActivityForRegion(RegionData node, List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList) {

//		System.out.println("Adding Children for" + node.getName()+":"+node.getType() );
				
		List<ActivityDto> activityList = new ArrayList<ActivityDto>();
		//Find the Child of RegionDataList
	    List<RegionData> childList= regionDataRepo.findAllChildForParentId(node.getId());
	    if(childList.isEmpty()) {
	    	//Meaning this is School Region
	    	//ADD surveys
	    	activityList.addAll(addSurveyForEntity(node,surveyReleaseMappingList));
	    	
	    }else {
	    	for (RegionData child : childList) {
	    		ActivityDto activity = getActivityFromRegionData(child);
				activity.setChildren(getChildrenActivityForRegion(child,surveyReleaseMappingList));
				activityList.add(activity);
			}
	    }

		return activityList;

	}

	private List<ActivityDto> addSurveyForEntity(RegionData entity, List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList) {
			
		List<ActivityDto> activityList = new ArrayList<ActivityDto>();
		
		for (SurveyReleaseMappingDataENITITY surveyReleaseMappingDataENITITY : surveyReleaseMappingList) {
				if(surveyReleaseMappingDataENITITY.getTargetEntityDataId() == entity.getId()) {
					Survey survey =  surveyRepo.getOne(surveyReleaseMappingDataENITITY.getSurveyId());
					activityList.add(getActivityFromSurvey(survey,surveyReleaseMappingDataENITITY.getIsSurveyInProgress()));
				}
		}
		return activityList;
	}

	/**
	 * Common
	 */

	private ActivityDto getActivityFromRegionData(RegionData regionData) {

		ActivityDto activity = new ActivityDto();
		activity.setId(regionData.getId());
		activity.setCode(regionData.getCode());
		activity.setTitle(regionData.getName());
		activity.setType(regionData.getType());
		activity.setProgram(null);
		return activity;
	}

	private ActivityDto getActivityFromSurvey(Survey survey, Boolean isSurveyInProgress) {
		ActivityDto activity = new ActivityDto();
		activity.setId(survey.getId());
		activity.setCode(null);
		activity.setTitle(survey.getTitle());
		activity.setType("SURVEY");
		if(isSurveyInProgress) {
			activity.setSurveyStatus("In-Progress");
		}else {
			activity.setSurveyStatus("Not-Started");

		}
		activity.setStartDateTime(survey.getStartDateTime());
		activity.setProgram(getProgramDtoFromEntity(survey.getProgram()));
		activity.setDescription(survey.getDescription());
		activity.setExpireDateTime(survey.getExpireDateTime());
		return activity;
	}

	private ProgramDtoSmall getProgramDtoFromEntity(Program source) {

		ProgramDtoSmall dto = new ProgramDtoSmall();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		dto = mapper.convertValue(source, ProgramDtoSmall.class);

		return dto;
	}

}
