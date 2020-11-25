package com.merchantvessel.core.business.enumeration;

import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.ObjHist;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.ObjUserHist;
import com.merchantvessel.core.persistence.model.Order;
import com.merchantvessel.core.persistence.model.OrderObjUser;

public enum EBusinessType {

	OBJ_BASE("Base", EDataKind.MASTER_DATA, Obj.class, ObjHist.class, Order.class),
	OBJ_USER("User", EDataKind.MASTER_DATA, ObjUser.class, ObjUserHist.class, OrderObjUser.class),
	OBJ_ASSET("Base", EDataKind.MASTER_DATA, null, null, null),
	OBJ_PARTY("Base", EDataKind.MASTER_DATA, null, null, null),
	OBJ_ROLE("Role", EDataKind.MASTER_DATA, null, null, null);

	public final String name;
	public final EDataKind dataKind;
	public final Class<?> objClass;
	public final Class<?> objHistClass;
	public final Class<?> orderClass;

	private EBusinessType(String name, EDataKind dataKind, Class<?> objClass, Class<?> objHistClass,
			Class<?> orderClass) {
		this.name = name;
		this.dataKind = dataKind;
		this.objClass = objClass;
		this.objHistClass = objHistClass;
		this.orderClass = orderClass;
	}

	public String getName() {
		return name;
	}

	public Class<?> getObjClass() {
		return objClass;
	}

	public EDataKind getDataKind() {
		return dataKind;
	}

	public Class<?> getOrderClass() {
		return orderClass;
	}

	public Class<?> getObjHistClass() {
		return objHistClass;
	}

}
