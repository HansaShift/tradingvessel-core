package com.merchantvessel.core.business.enumeration;

public enum ERole {
	ROLE_TRADER("Trader"), ROLE_BROKER("Broker"), ROLE_ADMIN("Admin");

	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
