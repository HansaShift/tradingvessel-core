package com.merchantvessel.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.merchantvessel.core.model.jpa.ObjUser;
import com.merchantvessel.core.repository.UserRepo;


@Service
public class UserDetailsSvcImpl implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ObjUser user = userRepo.findByUsername(username);

		return UserDetailsImpl.build(user);
	}

}