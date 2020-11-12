package com.merchantvessel.core.business.enumeration;

import javax.validation.constraints.NotNull;

public enum EPrcAction {

	// SHOW OBJECT
	OBJ_BASE_INIT_SHOW("Show Object", "OBJ_BASE_INIT_SHOW", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT, EPrcStatus.OBJ_BASE_SHOW),
	OBJ_BASE_SHOW_DISC("Discard Object", "OBJ_BASE_SHOW_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_SHOW, EPrcStatus.OBJ_BASE_DISC),
	
	// CREATE OBJECT
	OBJ_BASE_INIT_CREATE("Create Object", "OBJ_BASE_INIT_CREATE", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT, EPrcStatus.OBJ_BASE_CREATE),
	OBJ_BASE_CREATE_VFY("Verify Object Creation", "OBJ_BASE_CREATE_VFY", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_DONE),
	OBJ_BASE_CREATE_HOLD("Hold Object Creation", "OBJ_BASE_CREATE_HOLD", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_HOLD),
	OBJ_BASE_CREATE_DISC("Discard Object Creation", "OBJ_BASE_CREATE_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_DISC),
	
	// MODIFY OBJECT
	OBJ_BASE_INIT_MDF("Modify Object", "OBJ_BASE_INIT_MDF", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT, EPrcStatus.OBJ_BASE_MDF),
	OBJ_BASE_MDF_VFY("Verify Object Modification", "OBJ_BASE_MDF_VFY", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_DONE),
	OBJ_BASE_MDF_HOLD("Hold Object Modification", "OBJ_BASE_MDF_HOLD", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_HOLD),
	OBJ_BASE_MDF_DISC("Discard Object Modification", "OBJ_BASE_MDF_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_DISC),
	
	// HOLD OBJECT ORDER
	OBJ_BASE_HOLD_VFY("Verify Order", "OBJ_BASE_HOLD_VFY", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_DISC),
	OBJ_BASE_HOLD_DISC("Discard Order", "OBJ_BASE_HOLD_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_DISC);
	

	private final String name;
	private final String key;
	private final EBusinessType businessType;
	private final EPrcStatus oldStatus;
	private final EPrcStatus newStatus;

	private EPrcAction(@NotNull String name, @NotNull String key, @NotNull EBusinessType businessType,
			EPrcStatus oldStatus, EPrcStatus newStatus) {
		this.name = name;
		this.key = key;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.businessType = businessType;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public EPrcStatus getNewStatus() {
		return newStatus;
	}

	public EPrcStatus getOldStatus() {
		return oldStatus;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

}