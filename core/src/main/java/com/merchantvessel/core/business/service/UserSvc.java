package com.merchantvessel.core.business.service;

import java.util.Set;

import org.springframework.security.core.Authentication;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.persistence.model.ObjUser;

public interface UserSvc {

	public void save(ObjUser user);

	ObjUser registerUser(String userName, String password, Set<String> strRoles);

	ObjUser registerUser(String userName, String name, String password, Set<String> eRoles);

	public ObjUser getByAuthentication(Authentication authentication);

	boolean hasRole(ObjUser user, ERole eRole);

	public void createUsersFromEnum();

	void registerUser(EUser eUser);

	ObjUser getByEnum(EUser eUser);
}
