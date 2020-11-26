package com.merchantvessel.core.business.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EDataKind;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderTrans;
import com.merchantvessel.core.persistence.repository.OrderRepo;
import com.merchantvessel.core.persistence.repository.OrderTransRepo;

/**
 * @author Daniel Methner
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

	@Autowired
	private ObjHistSvc objHistSvc;

	// -------------------------------------------------------------
	// ORDER TRANSITION IN PROCESS
	// -------------------------------------------------------------
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

	// -------------------------------------------------------------
	// EXECUTE PROCESS ACTION
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <ObjType extends Obj, OrderClassType extends Order> OrderClassType execAction(OrderClassType order,
			EPrcAction prcAction) {

		ObjType obj = objSvc.instantiateObj(order);

		List<EBusinessType> businessTypesForWorkflow = new ArrayList<EBusinessType>();
		businessTypesForWorkflow.add(order.getDataKind() == EDataKind.MASTER_DATA ? EBusinessType.OBJ_BASE : null);
		businessTypesForWorkflow.add(order.getBusinessType());

		// PRC ACTION BUSINESS TYPE COMPATIBLE WITH ORDER BUSINESS TYPE
		if (!businessTypesForWorkflow.contains(prcAction.getBusinessType())) {
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
		if (order.getDataKind() == EDataKind.MASTER_DATA) {

			// DOES ORDER PERSIST OBJECT?
			if (prcAction.isPersistObj()) {

				// VALIDATE OBJECT
				if (!validateObjOrder(order)) {
					logSvc.write("OrderSvc.execAction(Order, EPrcAction)",
							"Order Validation failed! Order ID: " + order.getId());
					order = (OrderClassType) transOrder(order, EPrcAction.OBJ_BASE_ERR);
					return null;
				}

				// PERSIST OBJECT
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

	// -------------------------------------------------------------
	// INSTANTIATE ORDER
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private <OrderClassType extends Order> OrderClassType instantiateOrder(EBusinessType businessType) {
		OrderClassType order = null;

		// CREATE ORDER
		try {
			order = (OrderClassType) businessType.getOrderClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return order;
	}

	// -------------------------------------------------------------
	// CREATE OBJECT
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private <ObjType extends Obj> ObjType createObj(Order order) {

		ObjType obj = objSvc.instantiateObj(order);

		obj = setObjFieldsGeneric(obj, order);
		obj = setObjFields(obj, order);
		obj = (ObjType) objSvc.save(obj);
		// HISTORIZE OBJECT
		historizeObj(obj, order);

		return obj;

	}

	private <ObjHistClassType extends ObjHist, ObjClassType extends Obj> ObjHistClassType historizeNewObj(
			ObjHistClassType objHistNew, LocalDateTime orderValueDate, ObjClassType obj, Order order) {

		if (orderValueDate == null) {
			logSvc.write("OrderSvc.historizeObj()", "Order Value Date is missing. Order ID: " + order.getId());
			return null;
		}

		objHistNew = setObjHistFieldsGeneric(objHistNew, obj, order, orderValueDate,
				controlSvc.getMaxDateLocalDateTime());
		objHistNew = setObjHistFields(objHistNew, obj, order);
		// create ObjHist Object based on ObjType
		objHistNew = (ObjHistClassType) objHistSvc.save(objHistNew);
		return objHistNew;
	}

	// -------------------------------------------------------------
	// HISTORIZE OBJECT
	// -------------------------------------------------------------
	public <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType historizeObj(
			ObjClassType obj, OrderClassType order) {

		ObjHistClassType objHistNew = objHistSvc.instantiateObjHist(obj);
		ObjHistClassType currentObjHist = null;
		LocalDateTime orderValueDate = order.getValueDate();
		boolean noHist = false;

		if (orderValueDate == null) {
			logSvc.write("OrderSvc.historizeObj()", "Order Value Date is missing. Order ID: " + order.getId());
			return null;
		}
		LocalDateTime validTo = controlSvc.getMaxDateLocalDateTime();

		// ADJUST HISTORIC OBJ_HIST ENTRIES
		List<ObjHistClassType> objHistListPast = objHistSvc.getPastObjHistByObj(obj, orderValueDate, true);

		for (ObjHistClassType objHist : objHistListPast) {

			// ADJUST HISTORIC ENTRIES IF VALID_TO IS AFTER ORDER VALUE DATE
			if (objHist.getValidTo().isAfter(orderValueDate) || objHist.getValidTo().isEqual(orderValueDate)) {
				// "CUT" HISTORIC PERIODS
				objHist.setValidTo(orderValueDate.minusSeconds(1));
				objHistSvc.save(objHist);
				currentObjHist = objHist;
			}

		}

		// HISTORY DOES NOT EXIST
		if (currentObjHist != null) {
			currentObjHist = historizeNewObj(objHistNew, orderValueDate, obj, order);
		}

		// DETERMINE DELTA OF CURRENT ORDER
		ObjHistClassType objHistDelta = objHistSvc.instantiateObjHist(obj); // instantiate empty hist obj
		objHistDelta = populateObjHistDeltaGeneric(currentObjHist, objHistDelta, order);
		objHistDelta = populateObjHistDelta(currentObjHist, objHistDelta, order);

		// TODO: reuse first future OBJ_HIST entry if its valid from date is equal to order value date
		// TODO: create new OBJ_HIST entry if first future obj_hist entry has a valid_from date greater than order value date
		// ADJUST FUTURE OBJ_HIST ENTRIES
		List<ObjHistClassType> objHistListFuture = objHistSvc.getFutureObjHistByObj(obj, orderValueDate, true);
		ObjHistClassType previousObjHist = currentObjHist;

		for (ObjHistClassType objHistNext : objHistListFuture) {
			objHistDelta = adjustFutureObjHistGeneric(objHistDelta, objHistNext, previousObjHist, order);
			objHistDelta = adjustFutureObjHist(objHistDelta, objHistNext, previousObjHist, order);
			previousObjHist = objHistNext;
		}
		objHistNew = setObjHistFieldsGeneric(objHistNew, obj, order, orderValueDate, validTo);
		objHistNew = setObjHistFields(objHistNew, obj, order);
		objHistNew = (ObjHistClassType) objHistSvc.save(objHistNew);

		if (objHistNew.getId() == null) {
			logSvc.write("ObjSvc.save(Obj, Order)", "Object with ID: " + obj.getId() + " could not be historized!");
			return null;
		}

		return objHistNew;
	}

	private <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType adjustFutureObjHist(
			ObjHistClassType objHistDelta, ObjHistClassType objHistNext, ObjHistClassType previousObjHist,
			OrderClassType order) {
		// TODO Auto-generated method stub
		return objHistDelta;
	}

	private <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType adjustFutureObjHistGeneric(
			ObjHistClassType orderDelta, ObjHistClassType objHistNext, ObjHistClassType previousObjHist,
			OrderClassType order) {
		ObjHistClassType objHist = previousObjHist;
		objHist.setValidFrom(previousObjHist.getValidTo().plusSeconds(1));
		objHist.setValidTo(objHistNext.getValidFrom().minusSeconds(1));
		objHist.setName(orderDelta.getName() != null ? orderDelta.getName() : objHist.getName());
		objHist.setCloseDate(orderDelta.getCloseDate() != null ? orderDelta.getCloseDate() : objHist.getCloseDate());

		return objHist;
	}

	// -------------------------------------------------------------
	// POPULATE OBJECT HISTORY DELTA - GENERIC
	// -------------------------------------------------------------
	private <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType populateObjHistDeltaGeneric(
			ObjHistClassType currentObjHist, ObjHistClassType objHistDelta, OrderClassType order) {

		objHistDelta.setName(currentObjHist.getName() != order.getObjName() ? order.getObjName() : null);
		objHistDelta.setCloseDate(
				currentObjHist.getCloseDate() != order.getObjCloseDate() ? order.getObjCloseDate() : null);

		return objHistDelta;
	}

	// -------------------------------------------------------------
	// POPULATE OBJECT HISTORY DELTA - BUSINESS TYPE
	// -------------------------------------------------------------
	private <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType populateObjHistDelta(
			ObjHistClassType currentObjHist, ObjHistClassType objHistDelta, OrderClassType order) {
		return objHistDelta;
	}

	// -------------------------------------------------------------
	// PERSIST OBJECT
	// -------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private <ObjType extends Obj> ObjType persistOrderObj(Order order) {

		// ENSURE ORDER IS PERSISTED
		if (order.getId() == null) {
			logSvc.write("OrderSvc.persistOrderObj(Order)", "Order needs an ID before its object can be persisted!");
			return null;
		}

		// ENSURE ORDER IS OF TYPE MASTER_DATA
		if (order.getDataKind() != EDataKind.MASTER_DATA) {
			logSvc.write("OrderSvc.persistOrderObj(Order)", "Order must be of type '" + EDataKind.MASTER_DATA.getName()
					+ "' in order for its Object to be persisted!");
			return null;
		}

		ObjType obj = (ObjType) order.getObj();

		if (obj == null) {

			// CREATE NEW OBJECT
			obj = createObj(order);
			order.setObj(obj);

		} else {

			// UPDATE EXISTING OBJECT
			obj = setObjFieldsGeneric(obj, order);
			obj = setObjFields(obj, order);
			obj = (ObjType) objSvc.save(obj);

			// HISTORIZE OBJECT
			historizeObj(obj, order);

		}
		return obj;

	}

	public Order getById(Long id) {
		return orderRepo.getOne(id);
	}

	// -------------------------------------------------------------
	// VALIDATE OBJECT EXISTENCE
	// -------------------------------------------------------------
	private boolean validateObjExistence(@NotNull EPrcAction prcAction, @NotNull ObjUser objUser,
			@NotNull EBusinessType businessType, Obj obj) {
		// ENSURE OBJ EXISTS, UNLESS ORDER CREATES NEW OBJECT
		if (((prcAction.isLockObj() && !prcAction.isCreateObj()) || prcAction.isPersistObj()
				|| prcAction.isReleaseObj()) && obj == null) {
			logSvc.write("OrderSvc.createOrder()", "Order cannot be created: Object is missing! User: "
					+ objUser.getName() + " : " + businessType.getName());
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------
	// VALIDATE OBJECT ORDER
	// -------------------------------------------------------------
	public boolean validateObjOrder(Order order) {

		// VALIDATE OBJ CLOSE DATE
		if (!objSvc.validateObjCloseDate(order))
			return false;

		// VALIDATE OBJ NAME
		if (!objSvc.validateObjName(order.getObjName()))
			return false;

		return true;
	}

	// -------------------------------------------------------------
	// CREATE ORDER
	// -------------------------------------------------------------
	public <OrderClassType extends Order> OrderClassType createOrder(@NotNull EBusinessType businessType,
			@NotNull EPrcAction prcAction, @NotNull ObjUser objUser, LocalDateTime valueDate, Obj obj) {

		EDataKind dataKind = businessType.getDataKind();

		if (dataKind == EDataKind.MASTER_DATA) {

			if (!validateObjExistence(prcAction, objUser, businessType, obj)) {
				return null;
			}

			// ENSURE OBJ IS NOT LOCKED
			if (objSvc.objIsLocked(obj)) {
				logSvc.write("OrderSvc.createOrder()", "Order cannot proceed: Object with ID: " + obj.getId()
						+ " is locked by Order ID: " + obj.getOrder().getId());
				return null;
			}
		}

		// CREATE ORDER
		OrderClassType order = instantiateOrder(businessType);

		order.setDataKind(dataKind);
		order.setBusinessType(businessType);
		order.setObjUser(objUser);
		order.setValueDate(generateValueDate(order, obj, valueDate));
		String advText = prcAction.getName();

		if (obj != null) {
			advText = advText + ": Object Name: " + obj.getName();
			order = setOrderFieldsGeneric(obj, order);
			order = setOrderFields(obj, order);
		}
		order.setAdvText(advText);

		order = orderRepo.save(order);
		order = execAction(order, prcAction);
		return order;
	}

	// -------------------------------------------------------------
	// CREATE ORDER VALUE DATE
	// -------------------------------------------------------------
	public LocalDateTime generateValueDate(Order order, Obj obj, LocalDateTime valueDate) {

		if (valueDate != null) {
			return valueDate;
		}

		if (order.getDataKind() == EDataKind.MASTER_DATA) {
			if (obj == null) {
				return controlSvc.getMinDateLocalDateTime();
			} else {
				return controlSvc.getFinDate();
			}
		} else {
			return controlSvc.getFinDate();
		}
	}

	// -------------------------------------------------------------
	// SET ORDER VALUE DATE
	// -------------------------------------------------------------
	public void setValueDate(Order order, int year, int month, int day) {
		Date valueDateAsDate = new GregorianCalendar(year, month, day).getTime();
		LocalDateTime valueDate = valueDateAsDate.toInstant().atZone(ZoneId.of(controlSvc.getGlobalTimeZone()))
				.toLocalDateTime();
		order.setValueDate(valueDate);
	}

	// -------------------------------------------------------------
	// SET ORDER FIELDS - GENERIC
	// -------------------------------------------------------------
	public <ObjType extends Obj, OrderClassType extends Order> OrderClassType setOrderFieldsGeneric(ObjType obj,
			OrderClassType order) {
		order.setObj(obj);
		order.setObjName(obj.getName());
		order.setObjCloseDate(obj.getCloseDate());
		return order;
	}

	// -------------------------------------------------------------
	// SET ORDER FIELDS - BUSINESS TYPE
	// -------------------------------------------------------------
	public <ObjType extends Obj, OrderClassType extends Order> OrderClassType setOrderFields(ObjType obj,
			OrderClassType order) {
		return order;
	}

	// -------------------------------------------------------------
	// SET OBJECT FIELDS - GENERIC
	// -------------------------------------------------------------
	public <ObjType extends Obj, OrderClassType extends Order> ObjType setObjFieldsGeneric(ObjType obj,
			OrderClassType order) {
		obj.setName(order.getObjName());
		obj.setBusinessType(order.getBusinessType());
		obj.setName(order.getObjName());
		obj.setOrderCreate(obj.getOrderCreate() == null ? order : null);
		obj.setOrderMdf(order);
		return obj;
	}

	// -------------------------------------------------------------
	// SET OBJECT FIELDS - BUSINESS TYPE
	// -------------------------------------------------------------
	public <ObjType extends Obj, OrderClassType extends Order> ObjType setObjFields(ObjType obj, OrderClassType order) {
		return obj;
	}

	// -------------------------------------------------------------
	// SET OBJECT HIST FIELDS - GENERIC
	// -------------------------------------------------------------
	public <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType setObjHistFieldsGeneric(
			ObjHistClassType objHist, ObjClassType obj, OrderClassType order, @NotNull LocalDateTime validFrom,
			@NotNull LocalDateTime validTo) {
		objHist.setOrderId(order);
		objHist.setObjId(obj);
		objHist.setBusinessType(obj.getBusinessType());
		objHist.setObjTsIns(obj.getTimestampCreate());
		objHist.setObjTsLastMdf(obj.getTimestampModified());
		objHist.setName(obj.getName());
		objHist.setCloseDate(obj.getCloseDate());
		objHist.setValidFrom(validFrom);
		objHist.setValidTo(validTo);
		objHist.setValid(true);
		return objHist;
	}

	// -------------------------------------------------------------
	// SET OBJECT HIST FIELDS - BUSINESS TYPE
	// -------------------------------------------------------------
	public <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType setObjHistFields(
			ObjHistClassType objHist, ObjClassType obj, OrderClassType order) {
		return objHist;
	}

}
