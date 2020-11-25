package com.merchantvessel.core.business.service;

import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.ObjUserHist;
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
	public <ObjHistClassType extends ObjHist, ObjClassType extends Obj, OrderClassType extends Order> ObjHistClassType setObjHistFields(
			ObjHistClassType objHist, ObjClassType obj, OrderClassType order) {
		ObjUserHist objUserHist = (ObjUserHist) objHist;
		ObjUser objUser = (ObjUser) obj;
		objUserHist.setPassword(objUser.getPassword());
		objUserHist.setUsername(objUser.getUsername());
		objUserHist.setRoleSet(objUser.getRoleSet());
		return (ObjHistClassType) objUserHist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <ObjClassType extends Obj, OrderClassType extends Order> ObjClassType setObjFields(ObjClassType obj,
			OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderObjUser orderUser = (OrderObjUser) order;
		objUser.setUsername(orderUser.getUsername());
		objUser.setPassword(orderUser.getPassword());
		objUser.setRoleSet(orderUser.getObjRoleSet());
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
		orderUser.setObjRoleSet(objUser.getRoleSet());
		return (OrderClassType) orderUser;
	}

}
