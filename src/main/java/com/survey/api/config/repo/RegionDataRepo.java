package com.survey.api.config.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;

@Repository
public interface RegionDataRepo extends JpaRepository<RegionData, Long> {
	
	 public List<RegionData> findAllByRegionTypeOrderByNameAsc(RegionTypeMaster regionType);

	 public List<RegionData> findAllByOrderByIdAsc();

	Boolean existsByCode(String code);
	Optional<RegionData> findByCode(String code);
	
	@Query(value = "SELECT * FROM config_data_region r WHERE r.region_parent_id = ?1",  nativeQuery = true)
	public List<RegionData> findAllChildForParentId(Long parentId);

}
