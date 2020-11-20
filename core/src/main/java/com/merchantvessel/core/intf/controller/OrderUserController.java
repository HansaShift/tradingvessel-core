package com.merchantvessel.core.intf.controller;

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

import com.merchantvessel.core.business.service.UserDetailsImpl;
import com.merchantvessel.core.intf.dto.DtoOrder;
import com.merchantvessel.core.intf.dto.DtoOrderObjUser;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;
import com.merchantvessel.core.persistence.repository.OrderObjUserRepo;
import com.merchantvessel.core.persistence.repository.OrderRepo;
import com.merchantvessel.core.persistence.repository.UserRepo;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/OBJ_USER")
public class OrderUserController extends OrderController {

	@Autowired
	private OrderObjUserRepo orderObjUserRepo;

	@Autowired
	UserRepo userRepo;
	
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
		List<OrderObjUser> jpaOrders = orderObjUserRepo.findAll();
		List<DtoOrderObjUser> dtoOrders = new ArrayList<DtoOrderObjUser>();
		for (OrderObjUser order : jpaOrders) {
			DtoOrderObjUser dtoOrder = new DtoOrderObjUser(order);
			dtoOrders.add(dtoOrder);
		}
		return dtoOrders;
	}

//	@GetMapping("/new/newStatus/{newStatus}/orderType/{orderType}")
//	public ResponseEntity<?> createNewOrder(Authentication authentication, @PathVariable EPrcStatus newStatus,
//			@PathVariable EOrderType orderType) {
//		return null;
//	}
}