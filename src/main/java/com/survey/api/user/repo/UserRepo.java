package com.survey.api.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.config.entity.RegionData;
import com.survey.api.user.entity.User;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByPhoneNumber(String phoneNumber);
	
	
	Optional<User> findByUserUuid(String userUuid);
	
	Boolean existsByPhoneNumber(String phoneNumber);

	Optional<User> findByRegionData(RegionData regionData);

}
