package com.merchantvessel.core.business.service.test;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.business.service.ControlSvc;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.OrderObjUserSvc;
import com.merchantvessel.core.business.service.OrderSvc;
import com.merchantvessel.core.business.service.UserSvcImpl;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;

@Service
public class TestOrderSvc {

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private OrderObjUserSvc orderUserSvc;

	@Autowired
	private UserSvcImpl userSvc;
	@Autowired
	PasswordEncoder encoder;

	public TestOrderSvc() {
		// TODO Auto-generated constructor stub
	}

	public void testOrders() {
		createOrderToCreateObj();

	}

	private void createOrderToCreateObj() {

		ObjUser objUser = userSvc.getByEnum(EUser.TECHNICAL_USER);
		if (objUser == null) {
			logSvc.write("TestOrderSvc.createOrderToCreateObj()", "Could not retrieve 'TECHNICAL_USER'", true);
		}
		LocalDateTime valueDate = controlSvc.getFinDate();
		if (valueDate == null) {
			logSvc.write("TestOrderSvc.createOrderToCreateObj()", "Could not retrieve 'Financial Date'", true);
		}
		Order order = orderSvc.createOrder(EBusinessType.OBJ_BASE, EPrcAction.OBJ_BASE_INIT_CREATE, objUser, valueDate,
				null);

		if (order.getId() == null) {
			logSvc.write("TestOrderSvc.createOrderToCreateObj()", "Could not persist new order", true);
		}

		order.setObjName("Test Obj Name");
		order = orderSvc.execAction(order, EPrcAction.OBJ_BASE_CREATE_VFY);

		// CREATE ORDER
		OrderObjUser orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_CREATE,
				objUser, null, null);
		orderUser.setAdvText("Create new user called James Madison");
		orderUser.setObjName("Interactive Brokers");
		orderUser.setUsername("INTERACTIVE_BROKERS");
		orderUser.setPassword(encoder.encode("INTERACTIVE_BROKERS"));
		orderUser.addEnumRole(ERole.ROLE_BROKER);

		// VFY ORDER (persisting object
		orderUser = orderUserSvc.<ObjUser, OrderObjUser>execAction(orderUser, EPrcAction.OBJ_BASE_CREATE_VFY);
		Obj createdUser = orderUser.getObj();
		orderUser = null;

		// OPEN USER AND MODIFY HIS NAME
		orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_MDF, objUser, null,
				createdUser);
		orderUser.setAdvText("Change name of user 'James Madison' to 'James Miller'");
		orderUser.setObjName("Superdry Brokers");
		orderUser.setUsername("SUPERDRY_BROKERS");
		orderUserSvc.execAction(orderUser, EPrcAction.OBJ_BASE_MDF_VFY);
	}

}
