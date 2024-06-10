package com.survey.api.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.user.entity.User;
import com.survey.api.user.repo.UserRepo;




@Service
public class UserAuthService implements UserDetailsService {
	
	
	@Autowired
	UserRepo userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
		User user = userRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with Phone Number: " + phoneNumber));

		return UserAuthDetails.build(user);
	}

}
