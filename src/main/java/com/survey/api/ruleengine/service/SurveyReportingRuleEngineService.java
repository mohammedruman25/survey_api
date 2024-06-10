//package com.survey.api.ruleengine.service;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.survey.api.common.exception.BadRequestException;
//import com.survey.api.config.entity.RegionData;
//import com.survey.api.config.repo.DesignationTypeRepo;
//import com.survey.api.config.repo.RegionDataRepo;
//import com.survey.api.config.repo.RegionTypeRepo;
//import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
//import com.survey.api.ruleengine.entity.SurveyReportMappingEntity;
//import com.survey.api.ruleengine.repo.SurveyReleaseMappingPROFILERepo;
//import com.survey.api.ruleengine.repo.SurveyReportMappingRepo;
//
//@Service
//public class SurveyReportingRuleEngineService {
//
//	@Autowired
//	DesignationTypeRepo designationTypeRepo;
//
//	@Autowired
//	RegionTypeRepo regionTypeRepo;
//
//	@Autowired
//	RegionDataRepo regionDataRepo;
//
//	@Autowired
//	SurveyReportMappingRepo surveyReportMappingRepo;
//	
//	@Autowired
//	SurveyReleaseMappingPROFILERepo surveyReleaseMappingRepo;
//	
//	public Boolean populateReportVisibilityDataForSurvey(Long surveyId) {
//
//		List<SurveyReleaseMappingDataPROFILE> surveyReleaseEntity = surveyReleaseMappingRepo.findBySurveyIdAndIsSurveyCompleted(surveyId,true);
//		Set<Long> regionDataIds = new HashSet<Long>();
//		//Extract Unique Parent Ids of Region where survey is released
//		for (SurveyReleaseMappingDataPROFILE surveyReleaseMappingEntity : surveyReleaseEntity) {
//			//Find Parent of region Data 
//			RegionData regionData = regionDataRepo.findById(surveyReleaseMappingEntity.getTargetRegionDataId())
//					.orElseThrow(() -> new BadRequestException("Region Data not found with Id " + surveyReleaseMappingEntity.getTargetRegionDataId()));
//			
//			while(regionData.getParentRegion()!=null) {
//				regionDataIds.add(regionData.getParentRegion().getId());
//				regionData = regionData.getParentRegion();
//			}
//		}
//		List<SurveyReportMappingEntity>  reportEntityList = new ArrayList<>();
//		for (Long rDataId : regionDataIds) {
//			SurveyReportMappingEntity entity = new SurveyReportMappingEntity();
//			entity.setSurveyId(surveyId);
//			entity.setParentRegionDataId(rDataId);
//			reportEntityList.add(entity);
//		}
//		surveyReportMappingRepo.saveAll(reportEntityList);
//		
//		return true;
//	}
//	
//	
//	
//	
//	
//	
//}
