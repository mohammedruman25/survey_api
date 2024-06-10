package com.survey.api.showcase.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.showcase.entity.ShowcaseCategory;

@Repository
public interface ShowcaseCategoryRepo extends JpaRepository<ShowcaseCategory, Long> {
	Page<ShowcaseCategory> findByTitleIgnoreCaseContaining(String title, Pageable page);
	

}
