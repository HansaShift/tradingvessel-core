package com.merchantvessel.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByObjUser(ObjUser objUser);

}