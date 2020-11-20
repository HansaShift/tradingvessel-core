package com.merchantvessel.core.business.enumeration;

public enum EUser {
	TECHNICAL_USER("Technical User", ERole.ROLE_ADMIN), ADMIN_GEORGE("George Washington", ERole.ROLE_ADMIN),
	TRADER_THOMAS("Thomas Jefferson", ERole.ROLE_TRADER);

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
