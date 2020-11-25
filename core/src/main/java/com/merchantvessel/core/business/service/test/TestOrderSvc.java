package com.merchantvessel.core.business.service.test;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.business.enumeration.EUser;
import com.merchantvessel.core.business.service.ControlSvc;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.service.ObjHistSvc;
import com.merchantvessel.core.business.service.OrderObjUserSvc;
import com.merchantvessel.core.business.service.OrderSvc;
import com.merchantvessel.core.business.service.UserSvcImpl;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.ObjUserHist;
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

	@Autowired
	private ObjHistSvc objHistSvc;

	public TestOrderSvc() {
		// TODO Auto-generated constructor stub
	}

	public void testOrders() {
		createOrderToCreateObj();
		createOrderToModifyObj();
		createOrderToModifyObjInvalidateLastHist();
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

		// VFY ORDER, persisting object
		orderUser = orderUserSvc.<ObjUser, OrderObjUser>execAction(orderUser, EPrcAction.OBJ_BASE_CREATE_VFY);
	}

	private void createOrderToModifyObj() {
		ObjUser technicalUser = userSvc.getByEnum(EUser.TECHNICAL_USER);
		ObjUser objUser = userSvc.getByUsername("INTERACTIVE_BROKERS");
		// assert: User is NOT blocked
		if (objUser.getOrderMdf() != null) {
			logSvc.write("TestOrderSvc.createOrderToModifyObj()", "Object should currently not be locked. Object ID: "
					+ objUser.getId() + " Order ID: " + objUser.getOrderMdf().getId());
		}
		// OPEN USER AND MODIFY HIS NAME
		OrderObjUser orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_MDF,
				technicalUser, null, objUser);
		// assert: User is blocked
		if (objUser.getOrderMdf() == null) {
			logSvc.write("TestOrderSvc.createOrderToModifyObj()",
					"Object should be locked but is not. Object ID: " + objUser.getId());
		}
		objUser = userSvc.getByUsername("INTERACTIVE_BROKERS");
		orderUser.setAdvText("Change name of user 'Interactive Brokers' to 'Superdry Brokers'");
		orderUser.setObjName("Superdry Brokers");
		orderUser.setUsername("SUPERDRY_BROKERS");
		orderUserSvc.execAction(orderUser, EPrcAction.OBJ_BASE_MDF_VFY);

		// assert: User is NOT blocked
		objUser = userSvc.getByUsername("SUPERDRY_BROKERS");
		if (objUser.getOrderMdf() != null) {
			logSvc.write("TestOrderSvc.createOrderToModifyObj()", "Object should currently not be locked. Object ID: "
					+ objUser.getId() + " Order ID: " + objUser.getOrderMdf().getId());
		}

		// assert: UserHist has 2 entries
		List<ObjUserHist> objUserList = objHistSvc.getObjHistByObj(objUser);
		if (objUserList.size() != 2) {
			logSvc.write("TestOrderSvc.createOrderToModifyObj()",
					"Object History should have exactly 2 entries at this point. Object ID: " + objUser.getId());
		}
	}

	/*
	 * Invalidate latest history entry for given object by setting the value date of
	 * the modifciation order to a date earlier than the previous entry's VALID FROM
	 * date.
	 */
	private void createOrderToModifyObjInvalidateLastHist() {
		ObjUser technicalUser = userSvc.getByEnum(EUser.TECHNICAL_USER);
		ObjUser objUser = userSvc.getByUsername("SUPERDRY_BROKERS");
		// OPEN USER AND MODIFY HIS NAME
		OrderObjUser orderUser = orderUserSvc.createOrder(EBusinessType.OBJ_USER, EPrcAction.OBJ_BASE_INIT_MDF,
				technicalUser, controlSvc.getFinDate().minusDays(1), objUser);
		orderUser.setAdvText("Change password of user 'SUPERDRY_BROKERS'");
		orderUser.setPassword(encoder.encode("some random password"));
		orderUserSvc.execAction(orderUser, EPrcAction.OBJ_BASE_MDF_VFY);
		// assert: exactly one invalid entry in the history of that object
		List<ObjUserHist> objUserList = objHistSvc.getObjHistByObj(objUser, false);
		for (ObjUserHist objUserHist : objUserList) {
			System.err.println(objUserHist.getValidFrom() + " : " + objUserHist.getValidTo());
		}
		if (objUserList.size() != 1) {
			logSvc.write("TestOrderSvc.createOrderToModifyObj()",
					"Object History should have exactly 1 invalid entry at this point. Object ID: " + objUser.getId());
		}
	}
}
