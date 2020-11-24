package com.merchantvessel.core.persistence.demo_data;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merchantvessel.core.business.enumeration.ECtrlVar;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.business.service.ControlSvc;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.UserSvc;
import com.merchantvessel.core.business.service.test.TestOrderSvc;
import com.merchantvessel.core.persistence.model.CtrlVar;
import com.merchantvessel.core.persistence.model.Log;

@Component
public class DemoDataLoader {

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private TestOrderSvc testOrderSvc;

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
			LocalDateTime valueDate = controlSvc.getFinDate();
			if (valueDate == null) {
				logSvc.write("DemoDataLoader.setCtrlVars()", "Could not retrieve Fin Date", true);

			}
		}
	}

	public void createDemoData() {
		setCtrlVars();
		if (controlSvc.getByEnum(ECtrlVar.DEMO_DATA_CREATED).isValBool()) {
			System.out.println("Demo data was already loaded in a previous run.");
			return;
		}
		userSvc.registerUser(EUser.TECHNICAL_USER); // create Technical User via manual insert
		testOrderSvc.testOrders();
		userSvc.createUsersFromEnum(); // create remaining demo users via orders

		List<Log> logs = logSvc.getAll();
		if (logs.size() > 0) {
			System.err.println("There are logs in the database:");
			for (Log log : logs) {
				System.err.println(log.getLocation() + " " + log.getMessage());
			}
		} else {
			controlSvc.setVal(ECtrlVar.DEMO_DATA_CREATED, true);
			System.err.println("Demo data loaded successfully");
		}

	}

}
