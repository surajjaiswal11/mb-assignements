package com.mindbowser.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mindbowser.constant.ExceptionConstant;
import com.mindbowser.entity.Manager;
import com.mindbowser.repo.ManagerRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	ManagerRepo managerRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Manager user = managerRepo.findByEmailAndIsDeleted(username, false)
				.orElseThrow(() -> new UsernameNotFoundException(ExceptionConstant.EXC_USER_NOT_FOUND));

		return UserDetailsImpl.build(user);
	}

}