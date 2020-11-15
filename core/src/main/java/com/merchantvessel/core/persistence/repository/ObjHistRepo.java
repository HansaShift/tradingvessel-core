package com.merchantvessel.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;

@Repository
public interface ObjHistRepo extends JpaRepository<ObjHist, Long> {

//	ObjHist findTopByObjIdByOrderByTimestampCreateDesc(Long objId);
	ObjHist findTopByObjId(Obj obj);
}