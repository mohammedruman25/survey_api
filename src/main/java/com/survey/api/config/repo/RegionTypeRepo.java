package com.survey.api.config.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;

@Repository
public interface RegionTypeRepo extends JpaRepository<RegionTypeMaster, Long> {

	
	 public List<RegionTypeMaster> findAllByOrderByIdAsc();
	 
	@Query(value = "SELECT * FROM config_master_region_type r WHERE r.parent_id = ?1",  nativeQuery = true)
	public List<RegionTypeMaster> findAllChildForParentId(Long parentId);
	 
	 
}
