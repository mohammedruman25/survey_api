package com.survey.api.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.survey.api.user.entity.ERole;
import com.survey.api.user.entity.Role;


@Repository
public interface UserRoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}
