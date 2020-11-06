package com.merchantvessel.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.model.jpa.ObjRole;


@Repository
public interface RoleRepo extends JpaRepository<ObjRole, Long> {
	ObjRole findByName(String strRole);

	boolean existsByName(String strRole);
}
