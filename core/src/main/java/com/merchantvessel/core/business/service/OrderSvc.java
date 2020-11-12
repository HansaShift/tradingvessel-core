package com.merchantvessel.core.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EOrderType;
import com.merchantvessel.core.business.enumeration.EPrcAction;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderTrans;
import com.merchantvessel.core.persistence.repository.OrderRepo;
import com.merchantvessel.core.persistence.repository.OrderTransRepo;

@Service
public class OrderSvc {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;

	@Autowired
	private LogSvc logSvc;

	public Order execAction(Order order, EPrcAction prcAction) {
		List<EBusinessType> businessTypes = new ArrayList<EBusinessType>();
		businessTypes.add(order.getOrderType() == EOrderType.MASTER_DATA ? EBusinessType.OBJ_BASE : null);
		businessTypes.add(order.getBusinessType());

		if (!businessTypes.contains(prcAction.getBusinessType())) {
			logSvc.write("Order Business Type: '" + order.getBusinessType() + "' and PrcAction Business Type: '"
					+ prcAction.getBusinessType() + "' are different. Order cannot proceed!");
			return null;
		}
		if (order.getPrcStatus() == prcAction.getNewStatus()) {
			logSvc.write("Old WFC Status and New WFC Status are identical! Order cannot proceed");
			return null;
		}
		if (prcAction.getNewStatus().isDiscarded() && !order.getPrcStatus().isDiscardeable()) {
			logSvc.write("Order Status does not allow the order to be discarded!");
			return null;
		}
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

	public Order getById(Long id) {
		return orderRepo.getOne(id);
	}

	public Order createOrder(EOrderType orderType, EBusinessType businessType, EPrcAction prcAction, ObjUser objUser) {
		Order order = new Order(orderType, businessType, objUser);
		order = orderRepo.save(order);
		order = execAction(order, prcAction);
		return order;
	}
}
