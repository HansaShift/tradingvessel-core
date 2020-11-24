package com.merchantvessel.core.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.repository.ObjRepo;

@Service
public class ObjSvc {

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private ObjRepo objRepo;

	@Autowired
	private ObjHistSvc objHistSvc;

	@Autowired
	private LogSvc logSvc;

	public Obj saveNoHist(Obj obj) {
		return objRepo.save(obj);

	}
	
	public boolean objIsLocked(Obj obj) {
		if(obj == null || obj.getOrder() == null) {
			return false;
		}
		return true;
	}

	public Obj save(Obj obj, Order order) {

		obj = objRepo.save(obj);

		if (obj.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Order)", "Object could not be saved");
			return null;
		}

		if (order == null) {
			return obj;
		}

		// Only historize object if there is an order
		ObjHist objHist = objHistSvc.historizeObj(obj, order);

		if (objHist.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Order)", "Object with ID: " + obj.getId() + " could not be historized!");
			return null;
		}

		return obj;
	}

	public boolean validateObjName(String objName) {

		if (objName == null || objName.trim().length() == 0) {
			logSvc.write("ObjSvc.validateObjName()", "Validation Failed: Object name is missing.");
			return false;
		}
		return true;
	}

	public boolean validateObjCloseDate(Order order) {
		if (order.getObjCloseDate() != null) {
			if (order.getObj() == null) {
				logSvc.write("ObjSvc.validateObjCloseDate()",
						"Validation Failed: Order has no object. Cannot set close date.");
				return false;
			}
		}
		return true;
	}

}
