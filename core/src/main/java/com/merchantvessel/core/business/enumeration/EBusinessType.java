package com.merchantvessel.core.business.enumeration;

import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.OrderObjUser;

public enum EBusinessType {

	OBJ_BASE("Base", EDataKind.MASTER_DATA, null, null),
	OBJ_USER("User", EDataKind.MASTER_DATA, ObjUser.class, OrderObjUser.class),
	OBJ_ASSET("Base", EDataKind.MASTER_DATA, null, null), OBJ_PARTY("Base", EDataKind.MASTER_DATA, null, null),
	OBJ_ROLE("Role", EDataKind.MASTER_DATA, null, null);

	public final String name;
	public final EDataKind dataKind;
	public final Class<?> objClass;
	public final Class<?> orderClass;

	private EBusinessType(String name, EDataKind dataKind, Class<?> objClass, Class<?> orderClass) {
		this.name = name;
		this.dataKind = dataKind;
		this.objClass = objClass;
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

}
