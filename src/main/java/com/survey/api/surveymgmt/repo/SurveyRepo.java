package com.survey.api.surveymgmt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.entity.SurveyStatus;

@Repository
public interface SurveyRepo extends JpaRepository<Survey, Long> {
	
	Optional<Survey> findById(Long id);
	
	Page<Survey> findAll(Pageable page);
	

	Page<Survey> findAll(Specification<Survey> surveySpec, Pageable page);
	
	Page<Survey> findByTitleIgnoreCaseContainingAndStatusNot(String title, SurveyStatus status , Pageable page);
	
	Page<Survey> findByTitleIgnoreCaseContainingAndStatusAndIdIn(String title, SurveyStatus status , List<Long> ids, Pageable page);
	
	/**
	 * Methods for activity
	 */
	
	List<Survey> findByIdIn(List<Long> idList);
	
	@Modifying
	@Query(value = "update Survey s set status = :newstatus where current_date > s.expire_date_time and s.status = :curstatus", nativeQuery = true)
	void setSurveyExpired(@Param(value = "curstatus")String curstatus, @Param(value = "newstatus") String newstatus);
	

	@Query("select s from Survey s where current_date > s.expireDateTime and s.status = :curstatus")
	List<Survey> getAllExpiredSurvey(@Param(value = "curstatus")SurveyStatus curstatus);
}
