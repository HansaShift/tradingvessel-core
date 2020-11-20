package com.merchantvessel.core.business.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.repository.ObjHistRepo;
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

	// TODO: create OBJ_HIST entries using reflection for business type specific object history
	public ObjHist historizeObj(Obj obj, Order order) {

		List<ObjHist> objHistList = new ArrayList<ObjHist>();
		ObjHist objHistNew = null;
		LocalDateTime validFrom = order.getValueDate();

		if (validFrom == null) {
			objHistList = objHistSvc.getObjHistByObj(obj);
		} else {
			objHistList = objHistSvc.getObjHistByObjAndValidFrom(obj, validFrom);
		}

		if (objHistList.size() == 0) {
			// CASE 1: HISTORY DOES NOT EXIST
			validFrom = validFrom != null ? validFrom : controlSvc.getMinDateLocalDateTime();
			objHistNew = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime(), true);
			objHistNew = objHistSvc.save(objHistNew);
		} else {

			// CASE 2: HISTORY EXISTS
			validFrom = validFrom != null ? validFrom : controlSvc.getFinDate();

			// ADJUST EXISTING OBJ_HIST ENTRIES
			for (ObjHist objHist : objHistList) {

				objHist.setValidTo(validFrom.minusSeconds(1));

				// INVALIDATE OVERRIDEN HISTORY ENTRIES
				if (objHist.getValidFrom().isAfter(objHist.getValidTo())) {
					objHist.setValid(false);
				}
				objHistSvc.save(objHist);
			}

			objHistNew = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime(), true);
			objHistNew = objHistSvc.save(objHistNew);
		}

		return objHistNew;
	}

	public Obj save(Obj obj, Order order) {

		obj = objRepo.save(obj);

		if (obj.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Order)", "Object could not be saved");
		}

		if (order == null) {
			return obj;
		}

		ObjHist objHist = historizeObj(obj, order);
		if (objHist.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Order)", "Object with ID: " + obj.getId() + " could not be historized!");
			return null;
		}

		return obj;
	}

	public boolean validateObjName(String objName) {
		if (objName == null || objName.trim() == "") {
			logSvc.write("ObjSvc.validateObjName()", "Validation Failed: Order has no object name.");
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
