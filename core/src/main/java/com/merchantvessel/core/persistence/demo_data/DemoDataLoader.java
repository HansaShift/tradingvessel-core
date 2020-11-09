package com.merchantvessel.core.persistence.demo_data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.RoleSvc;
import com.merchantvessel.core.business.service.UserSvc;
import com.merchantvessel.core.persistence.model.ObjRole;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.repository.RoleRepo;

@Component
public class DemoDataLoader {
	@Autowired
	private RoleSvc roleSvc;

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private LogSvc logSvc;

	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// CREATE PARAM DATA
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------

	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// CREATE DEMO DATA
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	public void createDemoData() {
		roleSvc.createRoles();
		userSvc.createUsers();
		System.err.println("Demo data loaded");
	}

}
