package com.merchantvessel.core.model.enumeration;

public enum EUser {
	ADMIN_1("Barack Obama", ERole.ROLE_ADMIN), TRADER_1("John Doe", ERole.ROLE_TRADER);

	public final String name;
	public final ERole role;

	private EUser(String name, ERole eRole) {
		this.name = name;
		this.role = eRole;
	}

	public String getName() {
		return name;
	}

	public ERole getRole() {
		return role;
	}

}
