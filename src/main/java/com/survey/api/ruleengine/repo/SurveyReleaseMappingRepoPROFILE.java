package com.survey.api.ruleengine.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;

@Repository
public interface SurveyReleaseMappingRepoPROFILE extends JpaRepository<SurveyReleaseMappingDataPROFILE, Long> {

	
	
	
	
	
	@Query(value = "SELECT survey_id FROM SURVEY_RELEASE_MAPPING_DATA_PROFILE r WHERE r.target_designation_id = ?1 and r.target_region_data_id =?2 and r.is_survey_answered = ?3 and r.is_survey_expired = false",  nativeQuery = true)
	List<Long> findAllIncompleteSurveyIdByDesignationAndRegion(Long designationId, Long regionDataId, Boolean isSurveyAnswered);

	
	List<SurveyReleaseMappingDataPROFILE> findByTargetDesignationIdAndTargetRegionDataIdAndIsSurveyExpired(Long targetDesignationId,
			Long targetRegionDataId,Boolean isSurveyExpired);

	List<SurveyReleaseMappingDataPROFILE> findBySurveyId(Long surveyId);

	List<SurveyReleaseMappingDataPROFILE> findBySurveyIdAndTargetDesignationIdAndTargetRegionDataId(Long surveyId, Long designationId,
			Long regionDataId);

	
	/**
	 * Mobile Reporting Methods 
	 */
	List<SurveyReleaseMappingDataPROFILE> findByTargetDesignationIdAndTargetRegionDataId(Long designationId,
			Long targetRegionDataId);
	
	
	//Reporting totals 
	@Query(value = "select survey_id as surveyId, count(survey_id)  as releaseCount, sum(case is_survey_answered  when true then 1 else 0 end) as responseCount from survey_release_mapping_data_profile where survey_id IN ?1 group by survey_id", nativeQuery = true)
	List<ReportCountDtoIN> findreportMetrix(List<Long> surveyIds);
	
	
    public static interface ReportCountDtoIN {
		Long getSurveyId();
		Long getReleaseCount();
		Long getResponseCount();

	  }

}
