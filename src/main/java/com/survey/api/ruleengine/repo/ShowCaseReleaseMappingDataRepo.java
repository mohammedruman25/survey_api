package com.survey.api.ruleengine.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.survey.api.ruleengine.entity.ShowCaseReleaseMappingData;

@Repository
public interface ShowCaseReleaseMappingDataRepo  extends JpaRepository<ShowCaseReleaseMappingData, Long> {
	
	List<ShowCaseReleaseMappingData> findAllByShowcaseId(long showcaseId);

	List<ShowCaseReleaseMappingData> findAllByRegionDataId(long regionDataId);

}
