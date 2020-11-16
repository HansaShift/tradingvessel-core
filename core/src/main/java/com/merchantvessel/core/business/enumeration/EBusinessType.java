package com.merchantvessel.core.business.enumeration;

import com.merchantvessel.core.persistence.model.ObjUser;

public enum EBusinessType {

	OBJ_BASE("Base", null), OBJ_USER("User", ObjUser.class), OBJ_ROLE("Role", null);

	public final String name;
	public final Class objClass;

	private EBusinessType(String name, Class objClass) {
		this.name = name;
		this.objClass = objClass;
	}

	public String getName() {
		return name;
	}

	public Class getObjClass() {
		return objClass;
	}
	
	
}
