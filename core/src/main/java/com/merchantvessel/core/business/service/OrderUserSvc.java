package com.merchantvessel.core.business.service;

import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderUser;

/**
 * TODO: Enable business type specific method overrides to set Business type
 * specific fields allowing for @NotNull fields in child classes for objects and
 * orders
 * 
 * @author Daniel
 *
 */
@Service
public class OrderUserSvc extends OrderSvc {

	@Override
	public <ObjClassType extends Obj, OrderClassType extends Order> ObjClassType setObjFields(ObjClassType obj, OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderUser orderUser = (OrderUser) order;
		objUser.setUsername(orderUser.getUserName());
		objUser.setPassword(orderUser.getPassword());
		return (ObjClassType) objUser;
	}
	
	@Override
	public <ObjClassType extends Obj, OrderClassType extends Order> ObjClassType setOrderFields(ObjClassType obj, OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderUser orderUser = (OrderUser) order;
		objUser.setUsername(orderUser.getUserName());
		objUser.setPassword(orderUser.getPassword());
		return (ObjClassType) objUser;
	}

}
