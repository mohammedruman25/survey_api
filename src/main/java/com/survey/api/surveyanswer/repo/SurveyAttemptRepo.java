package com.survey.api.surveyanswer.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.surveyanswer.entity.SurveyAttempt;

@Repository
public interface SurveyAttemptRepo extends JpaRepository<SurveyAttempt, Long> {
	
	List<SurveyAttempt> findBySurvey_id(Long surveyId);
	
	SurveyAttempt findOneBySurvey_idAndUser_idAndTargetEntityDataId(Long surveyId, Long userId, Long targetEntityDataId);
}
