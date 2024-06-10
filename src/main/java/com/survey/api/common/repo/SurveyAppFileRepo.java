package com.survey.api.common.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.common.entity.SurveyAppFile;

@Repository
public interface SurveyAppFileRepo extends JpaRepository<SurveyAppFile, Long> {
	
	public SurveyAppFile findOneByFileUuid(UUID uuid);
	
	public void deleteByFileUuid(UUID uuid);
}
