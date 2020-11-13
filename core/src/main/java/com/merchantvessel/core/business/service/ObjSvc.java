package com.merchantvessel.core.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.repository.ObjRepo;

@Service
public class ObjSvc {

	@Autowired
	private ObjRepo objRepo;

	@Autowired
	private LogSvc logSvc;
	
	public Obj save(Obj obj) {
		return objRepo.save(obj);
	}

	public boolean validateObjName(String objName) {
		if (objName == null || objName.trim() == "") {
			logSvc.write("ObjSvc.validateObjName()", "Validation Failed: Order has no object name.");
			return false;
		}
		return true;
	}

	public boolean validateObjCloseDate(Order order) {
		System.out.println(order.getObjCloseDate() );
		if (order.getObjCloseDate() != null) {
			if (order.getObj() == null) {
				logSvc.write("ObjSvc.validateObjCloseDate()", "Validation Failed: Order has no object. Cannot set close date.");
				return false;
			}
		}
		return true;
	}
	

}
