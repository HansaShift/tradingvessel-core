package com.merchantvessel.core.business.enumeration;

public enum EObjType {
	OBJ_USER("User"), OBJ_ROLE("Role");

	public final String name;

	private EObjType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
