package com.survey.api.config.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.config.entity.DesignationTypeMaster;

@Repository
public interface DesignationTypeRepo extends JpaRepository<DesignationTypeMaster, Long> {

	public List<DesignationTypeMaster> findAllByOrderByIdAsc();

	Optional<DesignationTypeMaster> findByImportCode(String importCode);

}
