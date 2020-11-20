package com.merchantvessel.core.business.enumeration;

import com.merchantvessel.core.persistence.model.ObjUser;

public enum EBusinessType {

	OBJ_BASE("Base", EDataKind.MASTER_DATA, null), OBJ_USER("User", EDataKind.MASTER_DATA, ObjUser.class),
	OBJ_ASSET("Base", EDataKind.MASTER_DATA, null), OBJ_PARTY("Base", EDataKind.MASTER_DATA, null),
	OBJ_ROLE("Role", EDataKind.MASTER_DATA, null);

	public final String name;
	public final EDataKind dataKind;
	public final Class objClass;

	private EBusinessType(String name, EDataKind dataKind, Class objClass) {
		this.name = name;
		this.dataKind = dataKind;
		this.objClass = objClass;
	}

	public String getName() {
		return name;
	}

	public Class getObjClass() {
		return objClass;
	}

}
