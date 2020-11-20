package com.merchantvessel.core.business.enumeration;

public enum EDataKind {
	MASTER_DATA("Master Data"), TRX_DATA("Transaction Data");

	public final String name;

	private EDataKind(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
