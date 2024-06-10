package com.survey.api.surveymgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.surveymgmt.entity.QuestionType;

@Repository
public interface QuestionTypeRepo extends JpaRepository<QuestionType, Long> {
	

}
