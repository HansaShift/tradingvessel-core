package com.merchantvessel.core.model.demo_data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merchantvessel.core.model.enumeration.ERole;
import com.merchantvessel.core.model.enumeration.EUser;
import com.merchantvessel.core.model.jpa.ObjRole;
import com.merchantvessel.core.model.jpa.ObjUser;
import com.merchantvessel.core.repository.RoleRepo;
import com.merchantvessel.core.service.LogSvc;
import com.merchantvessel.core.service.RoleSvc;
import com.merchantvessel.core.service.UserSvc;

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
