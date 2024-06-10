package com.survey.api.ruleengine.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataOPEN;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoPROFILE.ReportCountDtoIN;

@Repository
public interface SurveyReleaseMappingRepoOPEN extends JpaRepository<SurveyReleaseMappingDataOPEN, Long> {

	

	List<SurveyReleaseMappingDataOPEN> findByTargetDesignationIdAndTargetRegionDataIdAndIsSurveyAnsweredAndIsSurveyExpired(Long targetDesignationId,
			Long targetRegionDataId,Boolean isSurveyAnswered, Boolean isSurveyExpired);

	List<SurveyReleaseMappingDataOPEN> findBySurveyId(Long surveyId);

	List<SurveyReleaseMappingDataOPEN> findBySurveyIdAndTargetDesignationIdAndTargetRegionDataId(Long surveyId, Long designationId,
			Long regionDataId);

	
	/**
	 * Mobile Reporting Methods 
	 */
	List<SurveyReleaseMappingDataOPEN> findByTargetDesignationIdAndTargetRegionDataId(Long designationId,
			Long targetRegionDataId);
	
	
	//Reporting totals 
	@Query(value = "select survey_id as surveyId, count(survey_id)  as releaseCount, sum(case is_survey_answered  when true then 1 else 0 end) as responseCount from survey_release_mapping_data_profile where survey_id IN ?1 group by survey_id", nativeQuery = true)
	List<ReportCountDtoIN> findreportMetrix(List<Long> surveyIds);
	

}
