package com.merchantvessel.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.CtrlVar;

@Repository
public interface CtrlVarRepo extends JpaRepository<CtrlVar, Long> {

	CtrlVar findByKey(String key);

}