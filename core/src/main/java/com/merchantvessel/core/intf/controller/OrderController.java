package com.merchantvessel.core.intf.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.service.OrderSvc;
import com.merchantvessel.core.business.service.UserDetailsImpl;
import com.merchantvessel.core.intf.dto.DtoOrder;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.repository.OrderRepo;
import com.merchantvessel.core.persistence.repository.UserRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private OrderSvc orderSvc;

//	@Autowired
//	GuiSvc guiSvc;

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	public ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	@GetMapping("/all")
	public List<?> getAll() {
		List<Order> jpaOrders = orderRepo.findAll();
		List<DtoOrder> dtoOrders = new ArrayList<DtoOrder>();
		for (Order order : jpaOrders) {
			DtoOrder dtoOrder = new DtoOrder(order);
			dtoOrders.add(dtoOrder);
		}
		return dtoOrders;
	}

	@GetMapping("/new/businessType/{businessType}/action/{prcAction}")
	public ResponseEntity<?> createNewOrder(Authentication authentication, @PathVariable EBusinessType businessType,
			@PathVariable EPrcAction prcAction) {
		ObjUser objUser = getByAuthentication(authentication);
		LocalDateTime valueDate = null;
		orderSvc.createOrder(businessType, prcAction, objUser, valueDate, null);
		return null;
	}
}