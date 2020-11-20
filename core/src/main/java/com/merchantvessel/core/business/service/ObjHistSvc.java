package com.merchantvessel.core.business.service;

import java.time.LocalDateTime;
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

	public ObjHistSvc() {
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
