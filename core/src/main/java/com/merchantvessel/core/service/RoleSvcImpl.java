package com.merchantvessel.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.model.enumeration.ERole;
import com.merchantvessel.core.model.jpa.ObjRole;
import com.merchantvessel.core.repository.RoleRepo;

@Service
public class RoleSvcImpl implements RoleSvc {

	@Autowired
	RoleRepo roleRepo;

	@Override
	public void save(ObjRole role) {
		roleRepo.save(role);
	}

	private void createRole(ERole eRole) {

		if (!roleRepo.existsByName(eRole.getName())) {
			ObjRole role = new ObjRole(eRole.getName());
			roleRepo.save(role);
		}
	}

	@Override
	public void createRoles() {
		for (ERole role : ERole.values()) {
			createRole(role);
		}

	}

}
