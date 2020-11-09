package com.merchantvessel.core.business.service;

import com.merchantvessel.core.business.enumeration.ERole;
import com.merchantvessel.core.persistence.model.ObjRole;

public interface RoleSvc {

	public abstract void save(ObjRole role);

	public abstract void createRoles();

}
