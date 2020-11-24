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

@Service
public class ObjHistSvc {

	@Autowired
	private ObjHistRepo objHistRepo;
	@Autowired
	private ControlSvc controlSvc;

	public ObjHistSvc() {
	}

	// TODO: create OBJ_HIST entries using reflection for business type specific
	// object history
	public ObjHist historizeObj(Obj obj, Order order) {

		List<ObjHist> objHistList = new ArrayList<ObjHist>();
		ObjHist objHistNew = null;
		LocalDateTime validFrom = order.getValueDate();

		if (validFrom == null) {
			objHistList = getObjHistByObj(obj);
		} else {
			objHistList = getObjHistByObjAndValidFrom(obj, validFrom);
		}

		if (objHistList.size() == 0) {
			// CASE 1: HISTORY DOES NOT EXIST
			validFrom = validFrom != null ? validFrom : controlSvc.getMinDateLocalDateTime();
			objHistNew = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime(), true);
			objHistNew = save(objHistNew);
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
				save(objHist);
			}

			objHistNew = new ObjHist(obj, order, validFrom, controlSvc.getMaxDateLocalDateTime(), true);
			objHistNew = save(objHistNew);
		}

		return objHistNew;
	}

	public ObjHist save(ObjHist objHist) {
		return objHistRepo.save(objHist);
	}

	public List<ObjHist> getObjHistByObj(Obj obj) {
		return objHistRepo.findByObjId(obj);
	}

	public List<ObjHist> getObjHistByObjAndValidFrom(Obj obj, LocalDateTime valueDate) {
		return objHistRepo.findByObjIdAndValidToGreaterThanEqual(obj, valueDate);
	}

}
