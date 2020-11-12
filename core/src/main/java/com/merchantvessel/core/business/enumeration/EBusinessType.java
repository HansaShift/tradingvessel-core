package com.merchantvessel.core.business.enumeration;

public enum EBusinessType {

	OBJ_BASE("Base"), OBJ_USER("User"), OBJ_ROLE("Role");

	public final String name;

	private EBusinessType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
