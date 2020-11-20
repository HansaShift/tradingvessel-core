package com.merchantvessel.core.business.service;

import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;

/**
 * TODO: Enable business type specific method overrides to set Business type
 * specific fields allowing for @NotNull fields in child classes for objects and
 * orders
 * 
 * @author Daniel
 *
 */
@Service
public class OrderObjUserSvc extends OrderSvc {

	@SuppressWarnings("unchecked")
	@Override
	public <ObjClassType extends Obj, OrderClassType extends Order> ObjClassType setObjFields(ObjClassType obj,
			OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderObjUser orderUser = (OrderObjUser) order;
		objUser.setUsername(orderUser.getUsername());
		objUser.setPassword(orderUser.getPassword());
		return (ObjClassType) objUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <ObjType extends Obj, OrderClassType extends Order> OrderClassType setOrderFields(ObjType obj,
			OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderObjUser orderUser = (OrderObjUser) order;
		orderUser.setUsername(objUser.getUsername());
		orderUser.setPassword(objUser.getPassword());
		return (OrderClassType) orderUser;
	}

}
