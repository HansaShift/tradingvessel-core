package com.merchantvessel.core.business.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.OrderObjUser;
import com.merchantvessel.core.persistence.repository.UserRepo;

@Service
public class UserSvcImpl implements UserSvc {

	@Autowired
	ControlSvc controlSvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserSvc userSvc;

	@Autowired
	LogSvc logSvc;

	@Autowired
	OrderObjUserSvc orderUserSvc;

	@Autowired
	ObjSvc objSvc;


	@Autowired
	PasswordEncoder encoder;

	@Override
	public void save(ObjUser user) {
		userRepo.save(user);
	}

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	@Override
	public ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	@Override
	public boolean hasRole(ObjUser user, ERole eRole) {
		for (ERole role : user.getRoleSet()) {
			if (role.equals(eRole)) {
				return true;
			}
		}
		return false;
	};

	@Override
	public void registerUser(EUser eUser) {

		Set<ERole> roles = new HashSet<>();
		roles.add(eUser.getRole());
		userSvc.registerUser(eUser.toString(), eUser.getName(), eUser.toString(), roles);
	}

	@Override
	public ObjUser registerUser(String userName, String name, String password, Set<ERole> enumRoles) {

		// ---------------------------------------------------------------------
		// ENSURE USER NAME IS UNIQUE
		// ---------------------------------------------------------------------
		if (userRepo.existsByUsername(userName)) {
			logSvc.write("UserSvcImpl.registerUser()", "User already exists: " + userName);
			return null;
		}

		// ---------------------------------------------------------------------
		// PERSIST USER
		// ---------------------------------------------------------------------
		ObjUser technicalUser = userRepo.findByUsername(EUser.TECHNICAL_USER.toString());
		ObjUser registeredUser = null;
		if (technicalUser == null && userName == EUser.TECHNICAL_USER.toString()) {
			ObjUser user = new ObjUser(userName, name, encoder.encode(password));
			registeredUser = userRepo.save(user);
		} else {

			// ---------------------------------------------------------------------
			// INITIALISE ORDER
			// ---------------------------------------------------------------------
			OrderObjUser order = orderUserSvc.createOrder(EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_CREATE,
					technicalUser, null, null);
			// ---------------------------------------------------------------------
			// SET ATTRIBUTES
			// ---------------------------------------------------------------------
			order.setObjName(name);
			order.setUsername(userName);
			order.setPassword(encoder.encode(password));
			order.setObjRoleSet(enumRoles);
			order = orderUserSvc.execAction(order, EPrcAction.OBJ_BASE_CREATE_VFY);
			registeredUser = (ObjUser) order.getObj();
		}

		if (registeredUser.getId() == null) {
			logSvc.write("UserSvcImpl.registerUser()", "User could not be persisted!");
			return null;
		}

		// ---------------------------------------------------------------------
		// ADD NATURAL PERSON WITH MACC
		// ---------------------------------------------------------------------
//		for (ObjRole role : user.getRoles()) {
//			String roleName = role.getName();
//			if (roleName.equals(ERole.ROLE_MGR.getName())) {
//				partySvc.createNaturalPerson(user);
//				break;
//			}
//		}

		return null;
	}

	@Override
	public ObjUser registerUser(String userName, String password, Set<ERole> eRoles) {
		return registerUser(userName, userName, password, eRoles);

	}

	/*
	 * Demo Data creation
	 */
	public void createUsersFromEnum() {
		for (EUser eUser : EUser.values()) {
			if (eUser != EUser.TECHNICAL_USER) {
				registerUser(eUser);
			}
		}

	}

	@Override
	public ObjUser getByEnum(EUser eUser) {
		return userRepo.findByUsername(eUser.toString());
	}
}
