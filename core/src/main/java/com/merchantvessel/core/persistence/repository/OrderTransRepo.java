package com.merchantvessel.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.OrderTrans;

@Repository
public interface OrderTransRepo extends JpaRepository<OrderTrans, Long> {

}