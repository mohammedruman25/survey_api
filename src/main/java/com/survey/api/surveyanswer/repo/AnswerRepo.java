package com.survey.api.surveyanswer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.surveyanswer.entity.Answer;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Long> {
	
}
