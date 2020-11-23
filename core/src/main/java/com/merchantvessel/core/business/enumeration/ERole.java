package com.merchantvessel.core.business.enumeration;

public enum ERole {
	ROLE_TRADER("TRADER"), ROLE_BROKER("BROKER"), ROLE_ADMIN("ADMIN");

	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
