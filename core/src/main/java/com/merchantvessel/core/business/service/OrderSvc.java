package com.merchantvessel.core.business.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EOrderType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderTrans;
import com.merchantvessel.core.persistence.repository.OrderRepo;
import com.merchantvessel.core.persistence.repository.OrderTransRepo;

@Service
public class OrderSvc {

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private ObjSvc objSvc;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;

	@Autowired
	private LogSvc logSvc;

	public Order execAction(Order order, EPrcAction prcAction) {
		System.err.println("Begin Monitoring");

		List<EBusinessType> businessTypes = new ArrayList<EBusinessType>();
		businessTypes.add(order.getOrderType() == EOrderType.MASTER_DATA ? EBusinessType.OBJ_BASE : null);
		businessTypes.add(order.getBusinessType());

		// PRC ACTION BUSINESS TYPE COMPATIBLE WITH ORDER BUSINESS TYPE
		if (!businessTypes.contains(prcAction.getBusinessType())) {
			logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
					"Order Business Type: '" + order.getBusinessType() + "' and PrcAction Business Type: '"
							+ prcAction.getBusinessType() + "' are different. Order cannot proceed!");
			return null;
		}
		// OLD PRC STATUS != NEW PRC STATUS
		if (order.getPrcStatus() == prcAction.getNewStatus()) {
			logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
					"Old WFC Status and New WFC Status are identical! Order cannot proceed");
			return null;
		}

		// CURRENT PRC STATUS MUST BE DISCARDEABLE
		if (prcAction.getNewStatus().isDiscarded() && !order.getPrcStatus().isDiscardeable()) {
			logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
					"Order Status does not allow the order to be discarded!");
			return null;
		}

		// PERSIST OBJECT
		if (order.getOrderType() == EOrderType.MASTER_DATA && prcAction.isPersistObj()) {
			// VALIDATE OBJECT
			if (!validateObjOrder(order)) {
				logSvc.write("OrderSvc.execAction(Order, EPrcAction)", "Order Validation failed!");
				return null;
			}
			persistOrderObj(order);
		}
		Obj obj = order.getObj();

		// LOCK OBJECT
		if (obj != null && prcAction.isLockObj()) {
			obj.setOrder(order);
			objSvc.save(obj);
		}

		// RELEASE OBJECT
		if (obj != null && prcAction.isReleaseObj()) {
			obj.setOrder(null);
			objSvc.save(obj);
		}

		// DOCUMENT ORDER TRANSITION
		OrderTrans orderTrans = new OrderTrans();
		orderTrans.setOrderStatusOld(order.getPrcStatus());
		order.setPrcStatus(prcAction.getNewStatus());
		orderTrans.setOrderStatusNew(order.getPrcStatus());
		orderTrans.setOrderAction(prcAction);
		order = orderRepo.save(order);
		orderTrans.setOrder(order);
		orderTransRepo.save(orderTrans);
		return order;
	}

	private boolean validateObjOrder(Order order) {

		// VALIDATE OBJ CLOSE DATE
		if (!objSvc.validateObjCloseDate(order))
			return false;

		// VALIDATE OBJ NAME
		if (!objSvc.validateObjName(order.getObjName()))
			return false;

		return true;
	}

	private Order persistOrderObj(Order order) {

		// ENSURE ORDER IS PERSISTED
		if (order.getId() == null) {
			logSvc.write("OrderSvc.persistOrderObj(Order)", "Order needs an ID before its object can be persisted!");
			return null;
		}

		// ENSURE ORDER IS OF TYPE MASTER_DATA
		if (order.getOrderType() != EOrderType.MASTER_DATA) {
			logSvc.write("OrderSvc.persistOrderObj(Order)", "Order must be of type '" + EOrderType.MASTER_DATA.getName()
					+ "' in order for its Object to be persisted!");
			return null;
		}

		Obj obj = order.getObj();

		if (obj == null) {

			// CREATE NEW OBJECT
			obj = new Obj(order);
			order.setObj(objSvc.save(obj));
		} else {

			// UPDATE EXISTING OBJECT
			obj.setName(order.getObjName());
			obj.setCloseDate(order.getObjCloseDate());
			objSvc.save(obj);
		}
		return null;

	}

	public Order getById(Long id) {
		return orderRepo.getOne(id);
	}

	public Order createOrder(EOrderType orderType, EBusinessType businessType, EPrcAction prcAction, ObjUser objUser) {
		Order order = new Order(orderType, businessType, objUser);
		order.setValueDate(controlSvc.getFinDate());
		order = orderRepo.save(order);
		order = execAction(order, prcAction);
		return order;
	}

	public void setValueDate(Order order, int year, int month, int day) {
		Date valueDateAsDate = new GregorianCalendar(year, month, day).getTime();
		LocalDateTime valueDate = valueDateAsDate.toInstant().atZone(ZoneId.of(controlSvc.getGlobalTimeZone()))
				.toLocalDateTime();
		order.setValueDate(valueDate);
	}
}
