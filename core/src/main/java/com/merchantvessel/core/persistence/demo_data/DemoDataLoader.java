package com.merchantvessel.core.persistence.demo_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.OrderSvc;
import com.merchantvessel.core.business.service.RoleSvc;
import com.merchantvessel.core.business.service.UserSvc;

@Component
public class DemoDataLoader {
	@Autowired
	private RoleSvc roleSvc;

	@Autowired
	private UserSvc userSvc;
//
	@Autowired
	private LogSvc logSvc;

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
