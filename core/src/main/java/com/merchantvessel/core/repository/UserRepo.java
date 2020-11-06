package com.merchantvessel.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.model.jpa.ObjUser;


@Repository
public interface UserRepo extends JpaRepository<ObjUser, Long> {
	
	ObjUser findByUsername(String username);

	Boolean existsByUsername(String username);

}