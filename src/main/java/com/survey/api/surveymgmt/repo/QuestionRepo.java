package com.survey.api.surveymgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.surveymgmt.entity.Question;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
	
}
