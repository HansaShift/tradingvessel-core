package com.merchantvessel.core.business.enumeration;

public enum EOrderType {

	MASTER_DATA("Master Data"), TRX_DATA("Transaction Data");

	public final String name;

	private EOrderType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}