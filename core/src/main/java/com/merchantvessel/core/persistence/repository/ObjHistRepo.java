package com.merchantvessel.core.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;

@Repository
public interface ObjHistRepo extends JpaRepository<ObjHist, Long> {

	List<ObjHist> findByObjIdAndValidToGreaterThanEqual(Obj obj, LocalDateTime valueDate);

	<ObjTypeHist extends ObjHist, ObjClassType extends Obj>  List<ObjTypeHist> findByObjId(ObjClassType obj);
	<ObjTypeHist extends ObjHist, ObjClassType extends Obj>  List<ObjTypeHist> findByObjIdAndValid(ObjClassType obj, boolean validEntries);

}