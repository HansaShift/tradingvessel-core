package com.merchantvessel.core.business.service;

import java.time.LocalDateTime;

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
	private ObjHistRepo objHistRepo;

	@Autowired
	private LogSvc logSvc;

	public ObjHist getLatestObjHist(Obj obj) {
		ObjHist objHist = objHistRepo.findTopByObjId(obj);
		return objHist;
	}
	

	public Obj saveNoHist(Obj obj) {
		return objRepo.save(obj);
		
	}

	public Obj save(Obj obj, Order order) {
		obj = objRepo.save(obj);
		
		if (order == null) {
			return obj;
		}

		ObjHist objHist = getLatestObjHist(obj);
		LocalDateTime validFrom = order.getValueDate();
		if (objHist == null) {
			// CASE 1: History DOES NOT exist
			// Ensure Valid From is defined
			validFrom = validFrom != null ? validFrom : controlSvc.getMinDateLocalDateTime();
			objHist = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime());
			objHistRepo.save(objHist);
		} else {
			// CASE 2: History exists
			// Ensure Valid From is defined
			validFrom = validFrom == null ? validFrom : controlSvc.getFinDate();
			objHist.setValidTo(validFrom.minusSeconds(1));
			objHistRepo.save(objHist);
			objHist = null;
			objHist = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime());
			objHistRepo.save(objHist);
		}

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
