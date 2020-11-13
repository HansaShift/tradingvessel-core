package com.merchantvessel.core.business.enumeration;

import javax.validation.constraints.NotNull;

public enum EPrcAction {

	// SHOW OBJECT
	OBJ_BASE_INIT_SHOW("Show Object", "OBJ_BASE_INIT_SHOW", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT,
			EPrcStatus.OBJ_BASE_SHOW, false, false, false),
	OBJ_BASE_SHOW_DISC("Discard Object", "OBJ_BASE_SHOW_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_SHOW,
			EPrcStatus.OBJ_BASE_DISC, false, false, false),

	// CREATE OBJECT
	OBJ_BASE_INIT_CREATE("Create Object", "OBJ_BASE_INIT_CREATE", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT,
			EPrcStatus.OBJ_BASE_CREATE, true, false, false),
	OBJ_BASE_CREATE_VFY("Verify Object Creation", "OBJ_BASE_CREATE_VFY", EBusinessType.OBJ_BASE,
			EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_DONE, false, true, true),
	OBJ_BASE_CREATE_HOLD("Hold Object Creation", "OBJ_BASE_CREATE_HOLD", EBusinessType.OBJ_BASE,
			EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_HOLD, false, false, false),
	OBJ_BASE_CREATE_DISC("Discard Object Creation", "OBJ_BASE_CREATE_DISC", EBusinessType.OBJ_BASE,
			EPrcStatus.OBJ_BASE_CREATE, EPrcStatus.OBJ_BASE_DISC, false, false, true),

	// MODIFY OBJECT
	OBJ_BASE_INIT_MDF("Modify Object", "OBJ_BASE_INIT_MDF", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_INIT,
			EPrcStatus.OBJ_BASE_MDF, true, false, false),
	OBJ_BASE_MDF_VFY("Verify Object Modification", "OBJ_BASE_MDF_VFY", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF,
			EPrcStatus.OBJ_BASE_DONE, false, true, true),
	OBJ_BASE_MDF_HOLD("Hold Object Modification", "OBJ_BASE_MDF_HOLD", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF,
			EPrcStatus.OBJ_BASE_HOLD, false, false, false),
	OBJ_BASE_MDF_DISC("Discard Object Modification", "OBJ_BASE_MDF_DISC", EBusinessType.OBJ_BASE,
			EPrcStatus.OBJ_BASE_MDF, EPrcStatus.OBJ_BASE_DISC, false, false, true),

	// HOLD OBJECT ORDER
	OBJ_BASE_HOLD_VFY("Verify Order", "OBJ_BASE_HOLD_VFY", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF,
			EPrcStatus.OBJ_BASE_DONE, false, false, true),
	OBJ_BASE_HOLD_DISC("Discard Order", "OBJ_BASE_HOLD_DISC", EBusinessType.OBJ_BASE, EPrcStatus.OBJ_BASE_MDF,
			EPrcStatus.OBJ_BASE_DISC, false, false, true);

	private final String name;
	private final String key;
	private final EBusinessType businessType;
	private final EPrcStatus oldStatus;
	private final EPrcStatus newStatus;
	private final boolean lockObj;
	private final boolean persistObj;
	private final boolean releaseObj;

	private EPrcAction(@NotNull String name, @NotNull String key, @NotNull EBusinessType businessType,
			EPrcStatus oldStatus, EPrcStatus newStatus, boolean lockObj, boolean persistObj, boolean releaseObj) {
		this.name = name;
		this.key = key;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.businessType = businessType;
		this.lockObj = lockObj;
		this.persistObj = persistObj;
		this.releaseObj = releaseObj;
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

	public boolean isPersistObj() {
		return persistObj;
	}

	public boolean isReleaseObj() {
		return releaseObj;
	}

	public boolean isLockObj() {
		return lockObj;
	}

}