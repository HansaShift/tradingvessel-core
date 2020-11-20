package com.merchantvessel.core.business.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EOrderType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.intf.dto.MsgResponse;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjRole;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.OrderObjUser;
import com.merchantvessel.core.persistence.repository.RoleRepo;
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
	OrderObjUserSvc orderUserSvc;

	@Autowired
	ObjSvc objSvc;

	@Autowired
	RoleRepo roleRepo;

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
		for (ObjRole role : user.getRoles()) {
			if (role.getName().equals(eRole.toString())) {
				return true;
			}
		}
		return false;
	};

	@Override
	public void registerUser(EUser eUser) {

		Set<String> roles = new HashSet<>();
		roles.add(eUser.getRole().toString());
		userSvc.registerUser(eUser.toString(), eUser.getName(), eUser.toString(), roles);
	}

	@Override
	public ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles) {

		// ---------------------------------------------------------------------
		// ENSURE USER NAME IS UNIQUE
		// ---------------------------------------------------------------------
		if (userRepo.existsByUsername(userName)) {
			return ResponseEntity.badRequest().body(new MsgResponse("Error: Username is already taken!"));
		}

		// ---------------------------------------------------------------------
		// PERSIST USER
		// ---------------------------------------------------------------------
		ObjUser technicalUser = userRepo.findByUsername(EUser.TECHNICAL_USER.toString());
		if (technicalUser == null && userName == EUser.TECHNICAL_USER.toString()) {
			ObjUser user = new ObjUser(userName, name, encoder.encode(password));
			userRepo.save(user);
		} else {
			OrderObjUser order = orderUserSvc.createOrder(EOrderType.MASTER_DATA, EBusinessType.OBJ_USER,
					EPrcAction.OBJ_BASE_INIT_CREATE, technicalUser, null, OrderObjUser.class, null);
			order.setObjName(name);
			order.setUserName(userName);
			order.setPassword(encoder.encode(password));
			orderUserSvc.execAction(order, EPrcAction.OBJ_BASE_CREATE_VFY, ObjUser.class);
		}

		// ---------------------------------------------------------------------
		// INSTANTIATE USER
		// ---------------------------------------------------------------------
		ObjUser user = new ObjUser(userName, name, encoder.encode(password));

		// ---------------------------------------------------------------------
		// ADD ROLES
		// ---------------------------------------------------------------------
		Set<String> strRoles = eRoles;
		Set<ObjRole> roles = new HashSet<>();

		if (strRoles == null) {
			ObjRole userRole = roleRepo.findByName(ERole.ROLE_TRADER.getName());
			roles.add(userRole);
		} else {
			strRoles.forEach(strRole -> {
				ObjRole role = roleRepo.findByName(strRole);
				if (role != null) {
					roles.add(role);
				}
			});
		}

		user.setRoles(roles);

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

		return ResponseEntity.ok(new MsgResponse("User registered successfully!"));
	}

	@Override
	public ResponseEntity<?> registerUser(String userName, String password, Set<String> eRoles) {
		return registerUser(userName, userName, password, eRoles);

	}

	/*
	 * Demo Data creation
	 */
	public void createUsers() {

		registerUser(EUser.TECHNICAL_USER);

		for (EUser eUser : EUser.values()) {
			registerUser(eUser);
		}

		// CREATE USER USING ORDER
		// GET USER
		ObjUser objUser = userRepo.findByUsername(EUser.TECHNICAL_USER.toString());

		System.err.println(objUser.getName());

		// CREATE ORDER
		OrderObjUser orderUser = orderUserSvc.createOrder(EOrderType.MASTER_DATA, EBusinessType.OBJ_USER,
				EPrcAction.OBJ_BASE_INIT_CREATE, objUser, null, OrderObjUser.class, null);
		orderUser.setAdvText("Create new user called James Madison");
		orderUser.setObjName("James Madison");
		orderUser.setUserName("JAMES_MADISON");
		orderUser.setPassword(encoder.encode("JAMES_MADISON"));
		orderUser.setValueDate(controlSvc.getMinDateLocalDateTime());
		// VFY ORDER (persisting object
		orderUser = orderUserSvc.<ObjUser, OrderObjUser>execAction(orderUser, EPrcAction.OBJ_BASE_CREATE_VFY,
				ObjUser.class);
		Obj createdUser = orderUser.getObj();
		orderUser = null;

		// OPEN USER AND MODIFY HIS NAME
		orderUser = orderUserSvc.createOrder(EOrderType.MASTER_DATA, EBusinessType.OBJ_USER,
				EPrcAction.OBJ_BASE_INIT_MDF, objUser, null, OrderObjUser.class, createdUser);
		orderUser.setAdvText("Change name of user 'James Madison' to 'James Miller'");
		orderUser.setObjName("James Miller");
		orderUser.setUserName("JAMES_MILLER");
		orderUserSvc.execAction(orderUser, EPrcAction.OBJ_BASE_MDF_HOLD, ObjUser.class);
	}
}
