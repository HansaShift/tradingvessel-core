package com.merchantvessel.core.business.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.persistence.model.ObjUser;

public interface UserSvc {

	public void save(ObjUser user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);

	ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles);

	public ObjUser getByAuthentication(Authentication authentication);

	boolean hasRole(ObjUser user, ERole eRole);

	public void createUsers();

	void registerUser(EUser eUser);
}
