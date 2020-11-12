package com.merchantvessel.core.business.enumeration;

public enum EPrcStatus {
	OBJ_BASE_INIT("Init", "INIT", EBusinessType.OBJ_BASE, true, false),
	OBJ_BASE_CREATE("Create", "CREATE", EBusinessType.OBJ_BASE, true, false),
	OBJ_BASE_MDF("Modify", "OBJ_BASE_MDF", EBusinessType.OBJ_BASE, true, false),
	OBJ_BASE_SHOW("Show", "SHOW", EBusinessType.OBJ_BASE, false, false),
	OBJ_BASE_DISC("Discarded", "DISCARDED", EBusinessType.OBJ_BASE, false, true),
	OBJ_BASE_HOLD("Hold", "HOLD", EBusinessType.OBJ_BASE, true, false),
	OBJ_BASE_DONE("Done", "DONE", EBusinessType.OBJ_BASE, false, false);

	private final String name;
	private final String key;
	private EBusinessType businessType;
	private final boolean discardeable;
	private final boolean discarded;

	private EPrcStatus(String name, String key, EBusinessType businessType, boolean discardeable, boolean discarded) {
		this.name = name;
		this.key = key;
		this.businessType = businessType;
		this.discardeable = discardeable;
		this.discarded = discarded;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public boolean isDiscardeable() {
		return discardeable;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(EBusinessType businessType) {
		this.businessType = businessType;
	}

	public boolean isDiscarded() {
		return discarded;
	}

}