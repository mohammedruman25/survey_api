package com.survey.api.ruleengine.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataENITITY;
import com.survey.api.ruleengine.entity.SurveyReleaseMappingDataPROFILE;
import com.survey.api.ruleengine.repo.SurveyReleaseMappingRepoPROFILE.ReportCountDtoIN;

@Repository
public interface SurveyReleaseMappingRepoENTITY  extends JpaRepository<SurveyReleaseMappingDataENITITY, Long> {

	
	List<SurveyReleaseMappingDataENITITY> findBySurveyId(Long surveyId);
	
	List<SurveyReleaseMappingDataENITITY> findByTargetDesignationIdAndIsSurveyAnsweredAndIsSurveyExpired(Long designationId,Boolean isSurveyAnswered, Boolean isSurveyExpired);
	
	List<SurveyReleaseMappingDataENITITY> findBySurveyIdAndTargetDesignationIdAndTargetEntityDataId(Long surveyId, Long designationId,Long targetEntityDataId);

	/**
	 * Mobile Report
	 */
	List<SurveyReleaseMappingDataENITITY> findByTargetDesignationId(Long designationId);

	
	//Reporting totals 
	@Query(value = "select survey_id as surveyId, count(survey_id)  as releaseCount, sum(case is_survey_answered  when true then 1 else 0 end) as responseCount from SURVEY_RELEASE_MAPPING_DATA_ENTITY where survey_id IN ?1 group by survey_id", nativeQuery = true)
	List<ReportCountDtoIN> findreportMetrix(List<Long> surveyIds);
	
	
	
	
}
