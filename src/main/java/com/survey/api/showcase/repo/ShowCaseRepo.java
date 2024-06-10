package com.survey.api.showcase.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.survey.api.showcase.entity.ShowCase;
import com.survey.api.user.entity.User;

@Repository
public interface ShowCaseRepo extends JpaRepository<ShowCase, Long> {
	Page<ShowCase> findByTitleIgnoreCaseContainingAndIdIn(String title, List<Long> ids, Pageable page);
	Page<ShowCase> findByCreatedByAndIsUpVoted(User u, Boolean isUpVoted, Pageable page);
	
	@Modifying
	@Query(value = "update ShowCase s set isExpired = true where current_date > s.expire_date_time", nativeQuery = true)
	void setShowCaseExpired();
	
	List<ShowCase> findByCurrentApproverId(Long approverId);
}
