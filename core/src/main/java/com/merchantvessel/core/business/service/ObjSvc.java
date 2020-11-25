package com.merchantvessel.core.business.service;

import java.lang.reflect.InvocationTargetException;

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
	private LogSvc logSvc;

	public Obj saveNoHist(Obj obj) {
		return objRepo.save(obj);

	}

	public boolean objIsLocked(Obj obj) {
		if (obj == null || obj.getOrder() == null) {
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------
	// INSTANTIATE OBJECT
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <ObjType extends Obj> ObjType instantiateObj(Order order) {
		ObjType obj = null;

		try {
			obj = (ObjType) order.getBusinessType().getObjClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Obj save(Obj obj, Order order) {

		obj = objRepo.save(obj);

		if (obj == null || obj.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Ord er)", "Object could not be saved");
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
