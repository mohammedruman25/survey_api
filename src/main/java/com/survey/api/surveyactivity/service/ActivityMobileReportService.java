package com.survey.api.surveyactivity.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataENITITY;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoENTITY;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoPROFILE;
import com.survey.api.ruleengine.service.SurveyReleaseRuleEngineService;
import com.survey.api.surveyactivity.dto.ActivityDto;
import com.survey.api.surveyactivity.dto.ActivityReportDto;
import com.survey.api.surveyactivity.dto.ActivityReportSurveyAnswerDTO;
import com.survey.api.surveyanswer.dto.SurveyAnswerDto;
import com.survey.api.surveyanswer.service.SurveyAnswerService;
import com.survey.api.surveymgmt.dto.ProgramDtoSmall;
import com.survey.api.surveymgmt.dto.SurveyDto;
import com.survey.api.surveymgmt.entity.Program;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.repo.SurveyRepo;
import com.survey.api.surveymgmt.service.SurveyService;

@Service
public class ActivityMobileReportService {

	@Autowired
	SurveyReleaseMappingRepoPROFILE surveyReleaseMappingProfileRepo;

	@Autowired
	SurveyReleaseMappingRepoENTITY surveyReleaseMappingEntityRepo;

	@Autowired
	SurveyRepo surveyRepo;

	@Autowired
	private RegionDataRepo regionDataRepo;
	
	@Autowired
	SurveyService surveyservice;
	
	@Autowired
	SurveyAnswerService answerservice;

	/**
	 * Section 1 : PROFILE report for Mobile Return survey which are answered or
	 * expired.
	 */

	public List<ActivityReportDto> getAllProfileActivityReport(Long userDesignationId, Long userRegionId) {

		if (userDesignationId == null) {
			throw new BadRequestException("Designation of User is Null");
		}
		if (userRegionId == null) {
			throw new BadRequestException("No Region Data is assigned to user");
		}

		List<ActivityReportDto> resultList = new ArrayList<ActivityReportDto>();

		List<SurveyReleaseMappingDataPROFILE> releaseMapList = surveyReleaseMappingProfileRepo
				.findByTargetDesignationIdAndTargetRegionDataId(userDesignationId, userRegionId);

		for (SurveyReleaseMappingDataPROFILE surveyReleaseMappingDataPROFILE : releaseMapList) {
			if (surveyReleaseMappingDataPROFILE.getIsSurveyAnswered() || surveyReleaseMappingDataPROFILE.getIsSurveyExpired()) {
				Survey survey = surveyRepo.findById(surveyReleaseMappingDataPROFILE.getSurveyId()).get();
				ActivityReportDto activityReport = getActivityReportFromSurvey(survey);
				activityReport.setIsSurveyAnswered(surveyReleaseMappingDataPROFILE.getIsSurveyAnswered());
				activityReport.setIsSurveyExpired(surveyReleaseMappingDataPROFILE.getIsSurveyExpired());
				resultList.add(activityReport);
			}

		}

		return resultList;
	}

	/**
	 * SECTION 2 : ENTITY survey report
	 */

	public List<ActivityReportDto> getAllEntityActivityReport(Long userDesignationId, Long userRegionId) {

		List<ActivityReportDto> resultList = new ArrayList<ActivityReportDto>();

		List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList = new ArrayList<SurveyReleaseMappingDataENITITY>();
		// filter the answered and expired
		for (SurveyReleaseMappingDataENITITY surveyReleaseMappingDataENITITY : surveyReleaseMappingEntityRepo
				.findByTargetDesignationId(userDesignationId)) {
			if (surveyReleaseMappingDataENITITY.getIsSurveyAnswered() || surveyReleaseMappingDataENITITY.getIsSurveyExpired()) {
				surveyReleaseMappingList.add(surveyReleaseMappingDataENITITY);
			}
		}

		// Step 1 : Find the List of Region of the User
		List<RegionData> regionDataList = regionDataRepo.findAllChildForParentId(userRegionId);
		if (regionDataList.size() == 0) {
			return new ArrayList<ActivityReportDto>();
		}
		
		// Iterate through it.
		for (RegionData regionData : regionDataList) {
			ActivityReportDto activity = getActivityReportFromRegionData(regionData);
			activity.setChildren(getChildrenActivityReportForRegion(regionData, surveyReleaseMappingList));
			resultList.add(activity);
		}
		return resultList;
	}

	private List<ActivityReportDto> getChildrenActivityReportForRegion(RegionData node, List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList) {

//		System.out.println("Adding Children for" + node.getName()+":"+node.getType() );

		List<ActivityReportDto> activityList = new ArrayList<ActivityReportDto>();
		// Find the Child of RegionDataList
		List<RegionData> childList = regionDataRepo.findAllChildForParentId(node.getId());
		if (childList.isEmpty()) {
			// Meaning this is School Region
			// ADD surveys if this belong to user
			activityList.addAll(addSurveyReportForEntity(node, surveyReleaseMappingList));

		} else {
			for (RegionData child : childList) {
				ActivityReportDto activity = getActivityReportFromRegionData(child);
				activity.setChildren(getChildrenActivityReportForRegion(child, surveyReleaseMappingList));
				activityList.add(activity);
			}
		}

		return activityList;

	}

	private List<ActivityReportDto> addSurveyReportForEntity(RegionData entity, List<SurveyReleaseMappingDataENITITY> surveyReleaseMappingList) {

		List<ActivityReportDto> activityList = new ArrayList<ActivityReportDto>();

		for (SurveyReleaseMappingDataENITITY surveyReleaseMappingDataENITITY : surveyReleaseMappingList) {
			if (surveyReleaseMappingDataENITITY.getTargetEntityDataId() == entity.getId()) {
				
				Survey survey = surveyRepo.getOne(surveyReleaseMappingDataENITITY.getSurveyId());
				ActivityReportDto dto = getActivityReportFromSurvey(survey);
				dto.setIsSurveyAnswered(surveyReleaseMappingDataENITITY.getIsSurveyAnswered());
				dto.setIsSurveyExpired(surveyReleaseMappingDataENITITY.getIsSurveyExpired());
				activityList.add(dto);
				
			}
		}
		return activityList;
	}

	/**
	 * COMMON
	 */

	private ActivityReportDto getActivityReportFromRegionData(RegionData regionData) {

		ActivityReportDto activity = new ActivityReportDto();
		activity.setId(regionData.getId());
		activity.setCode(regionData.getCode());
		activity.setTitle(regionData.getName());
		activity.setType(regionData.getType());
		activity.setProgram(null);
		return activity;
	}

	private ActivityReportDto getActivityReportFromSurvey(Survey survey) {

		ActivityReportDto activity = new ActivityReportDto();
		activity.setId(survey.getId());
		activity.setCode(null);
		activity.setTitle(survey.getTitle());
		activity.setType("SURVEY");
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

	public ActivityReportSurveyAnswerDTO getmobileSurveyReport(Long userId, ActivityReportSurveyAnswerDTO dto, Long userDesignationId, Long userRegionId) {

		ActivityReportSurveyAnswerDTO result = new ActivityReportSurveyAnswerDTO();
		SurveyDto surveyDTO = surveyservice.getOneSurvey(dto.getSurveyId());
		//In case of profile survey user Region Id will be target ENtity ID so it will be considered null 
		//but in case of entity survey we need to find target entity Id 
		result.setSurvey(surveyDTO);
		SurveyAnswerDto answer = null;
		if(dto.getEntityId()!=null || dto.getEntityId()!=0) {
			answer = answerservice.getSurveyAnswer(dto.getSurveyId(), userId, dto.getEntityId());
		}else {
			answer = answerservice.getSurveyAnswer(dto.getSurveyId(), userId, null);
		}
		result.setAnswer(answer);
		
		return result;
	}
	

}
