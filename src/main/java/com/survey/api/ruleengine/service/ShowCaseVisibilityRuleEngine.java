package com.survey.api.ruleengine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.ruleengine.entity.ShowCaseReleaseMappingData;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
import com.survey.api.ruleengine.repo.ShowCaseReleaseMappingDataRepo;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.user.entity.User;
import com.survey.api.user.repo.UserRepo;
import com.survey.api.user.service.UserService;

@Service
public class ShowCaseVisibilityRuleEngine {
	
	
	@Autowired
	ShowCaseReleaseMappingDataRepo showcaseRuleEngineRepo;
	
	@Autowired
	RegionDataRepo regionDatarepo;
	
	@Autowired	
	UserRepo userRepo;
	
	
	/**
	 * Create Showcase Entry and set the visibility
	 * @param showcaseId
	 * @param userId - who careted the survey
	 * @param regionDataId - of User who created this survey
	 */
	@Transactional
	public void createShowcaseMapping(Long showcaseId, Long regionDataId) {
		
		//Get the parent region Data of User regionData
		RegionData regionData = regionDatarepo.findById(regionDataId).orElseThrow(() -> new BadRequestException("Region Data not found"));		
		RegionData parentRegionData = regionData.getParentRegion();
		System.out.println("parentRegionData: "+parentRegionData.getId());
		//This parent region become the root of the visibility and need to add all the region Data which are child of this Region
		List<ShowCaseReleaseMappingData> entityMappingList = new ArrayList<ShowCaseReleaseMappingData>();
		entityMappingList.addAll(getChildRegions(showcaseId,parentRegionData,parentRegionData.getId()));
		
//		for (ShowCaseReleaseMappingData showCaseReleaseMappingData : entityMappingList) {
//			System.out.println(showCaseReleaseMappingData.toString());
//		}
	
		showcaseRuleEngineRepo.saveAll(entityMappingList);
	}
	

	private List<ShowCaseReleaseMappingData> getChildRegions(Long showcaseId, RegionData regionData,Long idOfParent) {
		List<ShowCaseReleaseMappingData> matchedNodes = new ArrayList<>();
		if (checkIfChildEntity(regionData,idOfParent)) {
			// this means survey is released to same level user and selected region of that
			// designation
			ShowCaseReleaseMappingData entity = new ShowCaseReleaseMappingData();
			entity.setShowcaseId(showcaseId);
			entity.setRegionDataId(regionData.getId());
			matchedNodes.add(entity);
		}else {
			
		}
		List<RegionData> childList = regionDatarepo.findAllChildForParentId(regionData.getId());
		for (RegionData child : childList) {
			matchedNodes.addAll(getChildRegions(showcaseId,child,idOfParent ));
		}
		return matchedNodes;
	}


	private boolean checkIfChildEntity(RegionData regionData, Long idOfParent) {
		if(regionData.getId() != idOfParent)	{
			return true;
		}
		return false;
	}


	/**
	 * Get approver Id during creation and Upvote of Showcase
	 * @param UserId
	 * @param regionDataId
	 * @return
	 */
	public Long getApproverId(Long userId, Long regionDataId) {
		User user =userRepo.findById(userId).orElseThrow(() -> new BadRequestException("No user Id found"));
		
		RegionData parentRegionData = user.getRegionData().getParentRegion();
		User manager = userRepo.findByRegionData(parentRegionData).orElseThrow(() -> new BadRequestException("Parent Region is not assigned to any user"));
		
		return manager.getId();
		
	}
	
	
	/**
	 * Delete hard from table 
	 * @param showcaseId
	 */
	@Transactional
	public void deleteShowcaseReleaseMapping(Long showcaseId) {
		
		List<ShowCaseReleaseMappingData> enityList = showcaseRuleEngineRepo.findAllByShowcaseId(showcaseId);
		
		showcaseRuleEngineRepo.deleteAll(enityList);
	}
	
	/**
	 * Upvote Method to change visiblity 
	 * @param showcaseId
	 * @param userId - who is upvotinh 
	 * @param regionDataId - region Data Id of user who is upvoting. 
	 */
	@Transactional
	public void upvoteShowcase(Long showcaseId,  Long regionDataId) {
		/**
		 * deletePrevious mapping
		 */
		deleteShowcaseReleaseMapping(showcaseId);
		createShowcaseMapping(showcaseId, regionDataId);
	}
	
	/**
	 * get list of showcase which your has access to for visibility
	 * @param regionDataId - loggedIn user Region Data Id
	 */
	public List<Long> getShowcaseList(Long regionDataId) {
		List<ShowCaseReleaseMappingData> entityList = showcaseRuleEngineRepo.findAllByRegionDataId(regionDataId);
		
		return entityList.parallelStream().map(e -> e.getShowcaseId()).collect(Collectors.toList());
		
	}
	
	
	

	

}
