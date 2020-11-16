package com.merchantvessel.core.business.service;

import java.lang.reflect.InvocationTargetException;
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

/**
 * TODO: Enable business type specific method overrides to set Business type
 * specific fields allowing for @NotNull fields in child classes for objects and
 * orders
 * 
 * @author Daniel
 *
 */
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

	public Order transOrder(Order order, EPrcAction prcAction) {
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

	public <ObjType extends Obj, OrderClassType extends Order> OrderClassType execAction(OrderClassType order,
			EPrcAction prcAction, Class<?> objClass) {

		ObjType obj;
		try {
			obj = (ObjType) objClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

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

		// MASTER DATA ORDERS
		if (order.getOrderType() == EOrderType.MASTER_DATA) {

			// PERSIST OBJECT
			if (prcAction.isPersistObj()) {
				// VALIDATE OBJECT
				if (!validateObjOrder(order)) {
					logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
							"Order Validation failed! Order ID: " + order.getId());
					order = (OrderClassType) transOrder(order, EPrcAction.OBJ_BASE_ERR);
					return null;
				}

				obj = (ObjType) persistOrderObj(order);
				if (obj == null) {
					logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
							"Object could not be persisted! Order ID: " + order.getId());
					order = (OrderClassType) transOrder(order, EPrcAction.OBJ_BASE_ERR);
					return null;

				}
			}

			obj = (ObjType) order.getObj();
			if (obj != null && obj.getId() != null) {

				// LOCK OBJECT
				if (prcAction.isLockObj()) {
					obj.setOrder(order);
					objSvc.saveNoHist(obj);
				}

				// RELEASE OBJECT
				if (prcAction.isReleaseObj()) {
					obj.setOrder(null);
					objSvc.saveNoHist(obj);
				}
			}

		}

		// DOCUMENT ORDER TRANSITION
		order = (OrderClassType) transOrder(order, prcAction);
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

	public <ObjType extends Obj, OrderClassType extends Order> ObjType setObjFieldsGeneric(ObjType obj,
			OrderClassType order) {
		obj.setName(order.getObjName());
		obj.setBusinessType(order.getBusinessType());
		obj.setName(order.getObjName());
		obj.setOrderCreate(order);
		obj.setOrderMdf(order);
		return obj;
	}

	public <ObjType extends Obj, OrderClassType extends Order> ObjType setObjFields(ObjType obj, OrderClassType order) {
		return obj;
	}

	private <ObjType extends Obj> ObjType persistOrderObj(Order order) {

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

		ObjType obj = (ObjType) order.getObj();

		if (obj == null) {

			// CREATE NEW OBJECT
			try {
				obj = (ObjType) order.getBusinessType().getObjClass().getDeclaredConstructor().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			obj = setObjFieldsGeneric(obj, order);
			obj = setObjFields(obj, order);
			obj = (ObjType) objSvc.save(obj, order);
			order.setObj(obj);

		} else {

			// UPDATE EXISTING OBJECT
			obj.setName(order.getObjName());
			obj.setCloseDate(order.getObjCloseDate());
			obj = (ObjType) objSvc.save(obj, order);
		}
		return obj;

	}

	public Order getById(Long id) {
		return orderRepo.getOne(id);
	}

	public LocalDateTime getValueDate(Order order, LocalDateTime valueDate) {
		if (valueDate == null) {
			valueDate = order.getOrderType() == EOrderType.MASTER_DATA ? controlSvc.getMinDateLocalDateTime()
					: controlSvc.getFinDate();
		}
		return valueDate;
	}

	public <OrderClassType extends Order> OrderClassType createOrder(EOrderType orderType, EBusinessType businessType,
			EPrcAction prcAction, ObjUser objUser, LocalDateTime valueDate, Class<?> orderClass) {

		OrderClassType order = null;

		try {
			order = (OrderClassType) orderClass.getDeclaredConstructor().newInstance();

			order.setOrderType(orderType);
			order.setBusinessType(businessType);
			order.setObjUser(objUser);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		order.setValueDate(getValueDate(order, valueDate));
		order = orderRepo.save(order);
		order = execAction(order, prcAction, order.getBusinessType().getObjClass());
		return order;
	}

	public void setValueDate(Order order, int year, int month, int day) {
		Date valueDateAsDate = new GregorianCalendar(year, month, day).getTime();
		LocalDateTime valueDate = valueDateAsDate.toInstant().atZone(ZoneId.of(controlSvc.getGlobalTimeZone()))
				.toLocalDateTime();
		order.setValueDate(valueDate);
	}
}
