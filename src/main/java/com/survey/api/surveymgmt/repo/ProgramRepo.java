package com.survey.api.surveymgmt.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.surveymgmt.entity.Program;

@Repository
public interface ProgramRepo extends JpaRepository<Program, Long> {
	Page<Program> findByTitleIgnoreCaseContaining(String title, Pageable page);
	

}
