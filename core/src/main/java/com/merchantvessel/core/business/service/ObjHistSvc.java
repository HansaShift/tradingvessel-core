package com.merchantvessel.core.business.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.repository.ObjHistRepo;

@Service
public class ObjHistSvc {

	@Autowired
	private ObjHistRepo objHistRepo;

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private OrderSvc orderSvc;

	public ObjHistSvc() {
	}

	public List<ObjHist> getObjHistList(Obj obj, LocalDateTime validFrom) {
		List<ObjHist> objHistList = new ArrayList<ObjHist>();

		if (validFrom == null) {
			objHistList = getObjHistByObj(obj);
		} else {
			objHistList = getObjHistByObjAndValidFrom(obj, validFrom);
		}
		return objHistList;
	}

	// -------------------------------------------------------------
	// INSTANTIATE OBJECT HISTORY
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <ObjHistClassType extends ObjHist> ObjHistClassType instantiateObjHist(Obj obj) {
		ObjHistClassType objHist = null;

		try {
			objHist = (ObjHistClassType) obj.getBusinessType().getObjHistClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		if (objHist == null) {
			logSvc.write("ObjHistSvc.instantiateObjHist()",
					"Object historization failed ofr object with ID: " + obj.getId());
		}
		return objHist;
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
