package com.merchantvessel.core.persistence.demo_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merchantvessel.core.business.enumeration.ECtrlVar;
import com.merchantvessel.core.business.service.ControlSvc;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.RoleSvc;
import com.merchantvessel.core.business.service.UserSvc;
import com.merchantvessel.core.persistence.model.CtrlVar;

@Component
public class DemoDataLoader {
	@Autowired
	private RoleSvc roleSvc;

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private ControlSvc controlSvc;

	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// CREATE DEMO DATA
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	private void createCtrlVars() {

		controlSvc.create(ECtrlVar.FIN_DATE, controlSvc.getCurrentDate());
		controlSvc.create(ECtrlVar.DEMO_DATA_CREATED, false);
	}

	/*
	 * Creates Demo Data if not yet done
	 */
	private void setCtrlVars() {
		CtrlVar demoDataCreate = controlSvc.getByEnum(ECtrlVar.DEMO_DATA_CREATED);
		if (demoDataCreate == null) {
			createCtrlVars();
			// LOAD FIN DATE FROM DATABASE
			controlSvc.getFinDate();
		}
	}

	public void createDemoData() {
		setCtrlVars();
		if (controlSvc.getByEnum(ECtrlVar.DEMO_DATA_CREATED).isValBool()) {
			System.out.println("Demo data was already loaded in a previous run.");
			return;
		}
		roleSvc.createRoles();
		userSvc.createUsers();
		
		if (logSvc.getAll().size() > 0) {
			System.err.println("There are logs in the database:");
		} else {
			controlSvc.setVal(ECtrlVar.DEMO_DATA_CREATED, true);
			System.err.println("Demo data loaded successfully");
		}
		


		
	}

}
