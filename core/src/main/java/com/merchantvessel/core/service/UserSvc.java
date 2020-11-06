package com.merchantvessel.core.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.merchantvessel.core.model.enumeration.ERole;
import com.merchantvessel.core.model.enumeration.EUser;
import com.merchantvessel.core.model.jpa.ObjUser;

public interface UserSvc {
	public void save(ObjUser user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);

	ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles);

	public ObjUser getByAuthentication(Authentication authentication);

	boolean hasRole(ObjUser user, ERole eRole);
	
	public void createUsers();
}
