package com.merchantvessel.core.business.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjRole;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;
import com.merchantvessel.core.persistence.repository.RoleRepo;

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

	@Autowired
	RoleRepo roleRepo;

	@SuppressWarnings("unchecked")
	@Override
	public <ObjClassType extends Obj, OrderClassType extends Order> ObjClassType setObjFields(ObjClassType obj,
			OrderClassType order) {
		ObjUser objUser = (ObjUser) obj;
		OrderObjUser orderUser = (OrderObjUser) order;
		objUser.setUsername(orderUser.getUsername());
		objUser.setPassword(orderUser.getPassword());
		// ---------------------------------------------------------------------
		// ADD ROLES
		// ---------------------------------------------------------------------
		Set<String> enumRoles = orderUser.getEnumRoles();
		Set<ObjRole> roles = new HashSet<>();
		if (enumRoles == null) {
			// DEFAULT ROLE: TRADER
			ObjRole userRole = roleRepo.findByName(ERole.ROLE_TRADER.getName());
			roles.add(userRole);
		} else {
			enumRoles.forEach(enumRole -> {
				ObjRole role = roleRepo.findByName(enumRole);
				if (role != null) {
					roles.add(role);
				}
			});
		}
		objUser.setRoles(roles);
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
		orderUser.setEnumRolesFromObjRole(objUser.getRoles());
		return (OrderClassType) orderUser;
	}

}
