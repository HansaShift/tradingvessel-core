package com.merchantvessel.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;

@Repository
public interface OrderObjUserRepo extends JpaRepository<OrderObjUser, Long> {

	List<OrderObjUser> findByObjUser(ObjUser objUser);

}