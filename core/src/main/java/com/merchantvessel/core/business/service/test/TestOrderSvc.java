package com.merchantvessel.core.business.service.test;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.business.service.ControlSvc;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.OrderSvc;
import com.merchantvessel.core.business.service.UserSvcImpl;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;

@Service
public class TestOrderSvc {

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private UserSvcImpl userSvc;

	public TestOrderSvc() {
		// TODO Auto-generated constructor stub
	}

	public void testOrders() {
		createOrderToCreateObj();

	}

	private void createOrderToCreateObj() {

		ObjUser objUser = userSvc.getByEnum(EUser.TECHNICAL_USER);
		LocalDateTime valueDate = controlSvc.getFinDate();

		Order order = orderSvc.createOrder(EBusinessType.OBJ_BASE, EPrcAction.OBJ_BASE_INIT_CREATE, objUser, valueDate,
				null);

		if (order.getId() == null) {
			logSvc.write("TestOrderSvc.createOrderToCreateObj()", "Could not persist new order");
		}

	}

//	// CREATE USER USING ORDER
//	// GET USER
//	ObjUser objUser = userRepo.findByUsername(EUser.TECHNICAL_USER.toString());
//
//	System.err.println(objUser.getName());
//
//	// CREATE ORDER
//	OrderObjUser orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER,
//			EPrcAction.OBJ_BASE_INIT_CREATE, objUser, null,  null);
//	orderUser.setAdvText("Create new user called James Madison");
//	orderUser.setObjName("James Madison");
//	orderUser.setUsername("JAMES_MADISON");
//	orderUser.setPassword(encoder.encode("JAMES_MADISON"));
////	orderUser.setValueDate(controlSvc.getMinDateLocalDateTime());
//	// VFY ORDER (persisting object
//	orderUser = orderUserSvc.<ObjUser, OrderObjUser>execAction(orderUser, EPrcAction.OBJ_BASE_CREATE_VFY,
//			ObjUser.class);
//	Obj createdUser = orderUser.getObj();
//	orderUser = null;
//
//	// OPEN USER AND MODIFY HIS NAME
//	orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER,
//			EPrcAction.OBJ_BASE_INIT_MDF, objUser, null, createdUser);
//	orderUser.setAdvText("Change name of user 'James Madison' to 'James Miller'");
//	orderUser.setObjName("James Miller");
//	orderUser.setUsername("JAMES_MILLER");
////	orderUser.setValueDate(controlSvc.getFinDate());
//	orderUserSvc.execAction(orderUser, EPrcAction.OBJ_BASE_MDF_VFY, ObjUser.class);

}
