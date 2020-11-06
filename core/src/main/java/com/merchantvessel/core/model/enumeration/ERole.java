package com.merchantvessel.core.model.enumeration;

public enum ERole {
	ROLE_TRADER("Trader"), ROLE_BROKER("Broker"), ROLE_ADMIN("Administrator");

	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
