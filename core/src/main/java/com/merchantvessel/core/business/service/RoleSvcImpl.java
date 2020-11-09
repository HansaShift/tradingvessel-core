package com.merchantvessel.core.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.persistence.model.ObjRole;
import com.merchantvessel.core.persistence.repository.RoleRepo;

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
