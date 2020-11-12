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
import com.merchantvessel.core.persistence.model.ObjRole;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.repository.RoleRepo;
import com.merchantvessel.core.persistence.repository.UserRepo;

@Service
public class UserSvcImpl implements UserSvc {

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserSvc userSvc;
	
	@Autowired
	OrderSvc orderSvc;

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
		// PERSIST USER
		// ---------------------------------------------------------------------
		user = userRepo.save(user);

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

	public void createUsers() {
		for (EUser eUser : EUser.values()) {
			registerUser(eUser);
		}
		
		// Create user using order
		ObjUser objUser = userRepo.findByUsername(EUser.ADMIN_BARACK.toString());
		Order order = orderSvc.createOrder(EOrderType.MASTER_DATA, EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_CREATE, objUser);
		System.out.println("Order ID: " + order.getId());
		System.out.println("Order Status: " + order.getPrcStatus().getName());
		
	}
}
