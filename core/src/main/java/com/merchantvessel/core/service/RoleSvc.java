package com.merchantvessel.core.service;

import com.merchantvessel.core.model.enumeration.ERole;
import com.merchantvessel.core.model.jpa.ObjRole;

public interface RoleSvc {

	public abstract void save(ObjRole role);

	public abstract void createRoles();

}
